package com.minewaku.trilog.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.minewaku.trilog.search.document.Post;

@Repository
public interface PostRepository extends ElasticsearchRepository<Post, Integer> {
	
	Page<Post> findByUsernameKeywordOrderByCreatedDateDesc(String username, Pageable pageable);
}
