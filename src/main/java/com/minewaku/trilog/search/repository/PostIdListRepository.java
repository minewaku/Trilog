package com.minewaku.trilog.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.minewaku.trilog.search.document.PostIdList;

public interface PostIdListRepository extends ElasticsearchRepository<PostIdList, Integer> {

}
