package com.minewaku.trilog.search.repository.custom.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.minewaku.trilog.search.document.ESUser;
import com.minewaku.trilog.search.repository.custom.ESUserRepositoryCustom;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

public class ESUserRepositoryImpl implements ESUserRepositoryCustom {
	
    @Autowired
    private ElasticsearchClient elasticsearchClient;

	@Override
	public List<Integer> suggestUsers(String input) throws IOException {
		
		var searchBuilder = new SearchRequest.Builder()
			.index("user")
			.size(10)
			.query(q -> q
				.match(m -> m
				  .field("username.prefix")
				  .query(input)
				)
			)
	        .source(src -> src
                .filter(f -> f
                    .includes("id")
                )
            );
		
		SearchResponse<Integer> response = elasticsearchClient.search(searchBuilder.build(), Integer.class);

		List<Integer> list = response.hits().hits().stream()
            .map(Hit::source)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
		
		return list;
	}
}
