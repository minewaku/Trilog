package com.minewaku.trilog.search.repository.custom.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.search.document.ESUser;
import com.minewaku.trilog.search.repository.custom.CursorPageBuilder;
import com.minewaku.trilog.search.repository.custom.ESUserRepositoryCustom;
import com.minewaku.trilog.util.ErrorUtil;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

public class ESUserRepositoryImpl implements ESUserRepositoryCustom, CursorPageBuilder {
	
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    
    @Autowired
    private ErrorUtil errorUtil;

	@Override
	public CursorPage<Integer> suggestUsers(String keyword, Cursor cursor) throws IOException {
		
        if (keyword == null || keyword.isEmpty()) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        }
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be null");
        }
        if (cursor.getLimit() <= 0) {
            throw new IllegalArgumentException("limit must be positive");
        }
		
		List<FieldValue> sortValues = new ArrayList<>();
		if(cursor != null && cursor.getAfter() != null) {
			sortValues.add(FieldValue.of(cursor.getAfter()));
		}
		
		var searchBuilder = new SearchRequest.Builder()
			.index("user")
			.size(cursor != null && cursor.getLimit() != null ? cursor.getLimit() : 10)
			.sort(s -> s
		        .field(f -> f
		            .field("id")
		            .order(SortOrder.Asc)
		        )
		    )
			.query(q -> q
				.multiMatch(m -> m
					.type(TextQueryType.BoolPrefix)
					.fields("name", "name._2gram", "name._3gram")
					.query(keyword)
				)
			)
	        .source(src -> src
                .filter(f -> f
                    .includes("id")
                )
            );
		
		if (cursor != null && cursor.getAfter() != null) {
		    searchBuilder.searchAfter(FieldValue.of(cursor.getAfter()));
		}

		SearchResponse<ESUser> response = elasticsearchClient.search(searchBuilder.build(), ESUser.class);


		List<Integer> list = response.hits().hits().stream()
			    .map(Hit::source)
			    .map(source -> source.getId())
			    .collect(Collectors.toList());

			
        return CursorPageBuilder.buildCursorResponse(list, cursor);
	}
}
