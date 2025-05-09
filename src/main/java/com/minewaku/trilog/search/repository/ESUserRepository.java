package com.minewaku.trilog.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.minewaku.trilog.search.document.ESUser;

@Repository
public interface ESUserRepository extends ElasticsearchRepository<ESUser, Integer> {

}
