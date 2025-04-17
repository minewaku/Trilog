package com.minewaku.trilog.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.search.document.Post;
import com.minewaku.trilog.search.repository.PostRepository;
import com.minewaku.trilog.search.repository.custom.PostRepositoryCustom;
import com.minewaku.trilog.search.service.IPostService;

@Service
public class PostService implements IPostService {
	
	@Autowired
	private PostRepositoryCustom postRepositoryCustom;
	
	@Override
	public List<Post> searchByContent(String keyword, Cursor cursor) {
		try {
			return postRepositoryCustom.searchByContent(keyword, cursor);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Post> findByUsername(String username, Cursor cursor) {
		try {
			return postRepositoryCustom.findByUsernameKeywordOrderByCreatedDateDesc(username, cursor);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
