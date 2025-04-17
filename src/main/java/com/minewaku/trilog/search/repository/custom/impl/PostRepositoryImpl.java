package com.minewaku.trilog.search.repository.custom.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.search.document.Post;
import com.minewaku.trilog.search.repository.custom.PostRepositoryCustom;
import com.minewaku.trilog.util.ErrorUtil;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;

public class PostRepositoryImpl implements PostRepositoryCustom {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private ErrorUtil errorUtil;
    
    //pagination via another index
    @Override
    public List<Integer> searchByHashtag(String hashtag) throws IOException {
        if (hashtag == null || hashtag.isEmpty()) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        }

        SearchResponse<Post> response = elasticsearchClient.search(s -> s
        	.index("post")
                .query(q -> q
                    .functionScore(fs -> fs
                        .query(inner -> inner
                            .bool(b -> b
                                .must(m -> m
                                    .term(t -> t
                                        .field("hashtags")
                                        .value(hashtag)
                                    )
                                )
                                .should(sh -> sh
                                    .rankFeature(rf -> rf
                                        .field("likes")
                                        .saturation(sat -> sat.pivot(1000.0f))
                                        .boost(0.6f)
                                    )
                                )
                                .should(sh -> sh
                                    .rankFeature(rf -> rf
                                        .field("views")
                                        .log(l -> l.scalingFactor(5.0f))
                                        .boost(0.1f)
                                    )
                                )
                                .should(sh -> sh
                                    .rankFeature(rf -> rf
                                        .field("comments")
                                        .sigmoid(sg -> sg
                                            .pivot(50.0f)
                                            .exponent(2.5f)
                                        )
                                        .boost(0.1f)
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
                            ).weight(2.0)
                        )
                    )
                )
                .sort(sort -> sort
                    .field(f -> f
                        .field("id")
                        .order(SortOrder.Desc)
                    )
                ), Post.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .filter(post -> post != null && post.getId() != null)
                .map(Post::getId)
                .collect(Collectors.toList());
    }


    //pagination via search after
    @Override
    public List<Post> searchByContent(String keyword, Cursor cursor) throws IOException {
    	
        if (keyword == null || keyword.isEmpty()) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        }
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be null");
        }
        if (cursor.getSize() <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        List<FieldValue> searchAfterValues = List.of(FieldValue.of(cursor.getCursor()));

        SearchResponse<Post> response = elasticsearchClient.search(s -> s
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
                                .should(sh -> sh
                                    .rankFeature(rf -> rf
                                        .field("likes")
                                        .saturation(sat -> sat.pivot(1000.0f))
                                        .boost(0.6f)
                                    )
                                )
                                .should(sh -> sh
                                    .rankFeature(rf -> rf
                                        .field("views")
                                        .log(l -> l.scalingFactor(5.0f))
                                        .boost(0.1f)
                                    )
                                )
                                .should(sh -> sh
                                    .rankFeature(rf -> rf
                                        .field("comments")
                                        .sigmoid(sg -> sg
                                            .pivot(50.0f)
                                            .exponent(2.5f)
                                        )
                                        .boost(0.3f)
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
                            ).weight(2.0)
                        )
                    )
                )
                .sort(sort -> sort
                    .field(f -> f
                        .field("id")
                    )
                )
                .searchAfter(searchAfterValues)
                .size(cursor.getSize())
                , Post.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Post> findByUsernameKeywordOrderByCreatedDateDesc(String username, Cursor cursor) throws IOException {
        if (username == null || username.isEmpty()) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        }
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be null");
        }
        if (cursor.getSize() <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        List<FieldValue> searchAfterValues = List.of(FieldValue.of(cursor.getCursor()));
        
        SearchResponse<Post> response = elasticsearchClient.search(s -> s
        	.index("post")
        	.size(cursor.getSize())
			.sort(sort -> sort
				.field(f -> f
					.field("postId")
					.order(SortOrder.Desc)
				)
			)
			.query(q -> q
				.term(t -> t
					.field("username")
					.value(username)
				)
			)
			.searchAfter(searchAfterValues)
        	, Post.class
        );

        return response.hits().hits().stream()
            .map(Hit::source)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}