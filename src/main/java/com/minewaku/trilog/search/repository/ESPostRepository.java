package com.minewaku.trilog.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.minewaku.trilog.search.document.ESPost;

@Repository
public interface ESPostRepository extends ElasticsearchRepository<ESPost, Integer> {
	
}
