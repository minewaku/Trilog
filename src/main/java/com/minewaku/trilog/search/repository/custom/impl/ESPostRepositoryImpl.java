package com.minewaku.trilog.search.repository.custom.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.search.repository.custom.CursorPageBuilder;
import com.minewaku.trilog.search.repository.custom.ESPostRepositoryCustom;
import com.minewaku.trilog.util.ErrorUtil;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;

public class ESPostRepositoryImpl implements ESPostRepositoryCustom, CursorPageBuilder {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private ErrorUtil errorUtil;
    
    //pagination via another index
    @Override
    public CursorPage<Integer> searchByHashtag(String keyword, Cursor cursor) throws IOException {
    	
        if (keyword == null || keyword.isEmpty()) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        }
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be null");
        }
        if (cursor.getLimit() <= 0) {
            throw new IllegalArgumentException("limit must be positive");
        }
        
        String scriptSource = 
                // Base score from matching
                "1.0 + " +
                // Likes with saturation (equivalent to your saturation function with pivot=1000)
                "(doc['likes'].value / (doc['likes'].value + 1000.0)) * 0.6 + " +
                // Comments with sigmoid function (equivalent to your sigmoid with pivot=50, exponent=2.5)
                "(1.0 / (1.0 + Math.exp(-2.5 * (doc['comments'].value - 50.0) / 50.0))) * 0.3";

        var searchBuilder = new SearchRequest.Builder()
        	.index("post")
            .query(q -> q
                .functionScore(fs -> fs
                    .query(inner -> inner
                        .bool(b -> b
                    		.must(m -> m
                                .matchPhrase(mp -> mp
                                    .field("content")
                                    .query("#" + keyword)
                                )
                            )
                        )
                    )
                    .query(inner -> inner
                    	.scriptScore(sc -> sc
                			.script(script -> script
                                .inline(i -> i
                                    .source(scriptSource)
                                )
                			)
                    	)
                    )
                    .boostMode(FunctionBoostMode.Sum)
                    .scoreMode(FunctionScoreMode.Sum)
                    .functions(f -> f
                        .gauss(gauss -> gauss
                            .field("createdDate")
                            .placement(pl -> pl
                                .origin(JsonData.of("now"))
                                .scale(JsonData.of("7d"))
                                .decay(0.5)
                            )
                        )
                        .weight(2.0)
                    )
                )
            )
            .sort(sort -> sort
                .field(f -> f
                    .field("id")
                    .order(SortOrder.Desc)
                )
            )
            .source(src -> src
                .filter(f -> f
                    .includes("id")
                )
            );

        if (cursor.getAfter() != null) {
            List<FieldValue> searchAfterValues = List.of(FieldValue.of(cursor.getAfter()));
            searchBuilder.searchAfter(searchAfterValues);
        }

        // Execute search
        SearchResponse<Integer> response = elasticsearchClient.search(searchBuilder.build(), Integer.class);

        // Process results
        List<Integer> list = response.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        return CursorPageBuilder.buildCursorResponse(list, cursor);
    }


    //pagination via search after
    @Override
    public CursorPage<Integer> searchByContent(String keyword, Cursor cursor) throws IOException {
        
        if (keyword == null || keyword.isEmpty()) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        }
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be null");
        }
        if (cursor.getLimit() <= 0) {
            throw new IllegalArgumentException("limit must be positive");
        }
        
        String scriptSource = 
                // Base score from matching
                "1.0 + " +
                // Likes with saturation (equivalent to your saturation function with pivot=1000)
                "(doc['likes'].value / (doc['likes'].value + 1000.0)) * 0.6 + " +
                // Comments with sigmoid function (equivalent to your sigmoid with pivot=50, exponent=2.5)
                "(1.0 / (1.0 + Math.exp(-2.5 * (doc['comments'].value - 50.0) / 50.0))) * 0.3";

        // Create search request builder with common parameters
        var searchBuilder = new SearchRequest.Builder()
            .index("post")
            .query(q -> q
                .functionScore(fs -> fs
                    .query(inner -> inner
                        .bool(b -> b
                            .must(m -> m
                                .matchPhrase(mp -> mp
                                    .field("content")
                                    .query(keyword)
                                    .slop(2)
                                )
                            )
                            .must(m -> m
                            	.term(t -> t
                        			.field("status")
                                    .value(0)
                            	)
                            )
                        )
                    )
                    .functions(f -> f
                        .scriptScore(sc -> sc
                            .script(script -> script
                                .inline(i -> i
                                    .source(scriptSource)
                                )
                            )
                        )
                        .weight(1.0)
                    )
                    .boostMode(FunctionBoostMode.Sum)
                    .scoreMode(FunctionScoreMode.Sum)
                    .functions(f -> f
                        .gauss(gauss -> gauss
                            .field("created_date")
                            .placement(pl -> pl
                                .origin(JsonData.of("now"))
                                .scale(JsonData.of("7d"))
                                .decay(0.5)
                            )
                        )
                        .weight(2.0)
                    )
                )
            )
            .sort(sort -> sort
                .field(f -> f
                    .field("id")
                )
            )
            .source(src -> src
                .filter(f -> f
                    .includes("id")
                )
            )
            .size(cursor.getLimit() + 1); // give an extra row to determine if there are more results

        // Add searchAfter only if we have an "after" cursor value
        if (cursor.getAfter() != null) {
            List<FieldValue> searchAfterValues = List.of(FieldValue.of(cursor.getAfter()));
            searchBuilder.searchAfter(searchAfterValues);
        }

        // Execute search
        SearchResponse<Integer> response = elasticsearchClient.search(searchBuilder.build(), Integer.class);

        // Process results
        List<Integer> list = response.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        return CursorPageBuilder.buildCursorResponse(list, cursor);
    }
    
    @Override
    public CursorPage<Integer> findByUserIdOrderByCreatedDateDesc(Integer userId, Cursor cursor) throws IOException {
        
    	if (userId == null) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        }
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be null");
        }
        if (cursor.getLimit() <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        
        var searchBuilder = new SearchRequest.Builder()
	    	.index("post")
	    	.size(cursor.getLimit() + 1) //give an extra row in order to get the after cursor position
			.sort(sort -> sort
				.field(f -> f
					.field("postId")
					.order(SortOrder.Desc)
				)
			)
			.query(q -> q
				.term(t -> t
					.field("userId")
					.value(userId)
				)
			);
        
        if (cursor.getAfter() != null) {
            List<FieldValue> searchAfterValues = List.of(FieldValue.of(cursor.getAfter()));
            searchBuilder.searchAfter(searchAfterValues);
        }
        
        SearchResponse<Integer> response = elasticsearchClient.search(searchBuilder.build(), Integer.class);

        List<Integer> list = response.hits().hits().stream()
            .map(Hit::source)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        
        return CursorPageBuilder.buildCursorResponse(list, cursor);
    }
}