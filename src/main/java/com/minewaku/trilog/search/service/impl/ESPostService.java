package com.minewaku.trilog.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.mapper.PostMapper;
import com.minewaku.trilog.repository.PostRepository;
import com.minewaku.trilog.search.repository.custom.ESPostRepositoryCustom;
import com.minewaku.trilog.search.service.ESIPostService;

@Service
public class ESPostService implements ESIPostService {
	
	@Autowired
	private ESPostRepositoryCustom postRepositoryCustom;
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private PostRepository postRepository;
	
	@Override
	public CursorPage<PostDTO> searchByHashtag(String hashtag, Cursor cursor) {
		try {
			CursorPage<Integer> esResult = postRepositoryCustom.searchByHashtag(hashtag, cursor);
			List<Post> result = postRepository.findAllById(esResult.getRecords());

			return CursorPage.<PostDTO>builder().
					after(esResult.getAfter())
					.before(esResult.getBefore())
					.limit(esResult.getLimit()).total(result.size())
					.records(result.stream().map(postMapper::entityToDto).toList())
					.build();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public CursorPage<PostDTO> searchByContent(String keyword, Cursor cursor) {
		try {
			CursorPage<Integer> esResult = postRepositoryCustom.searchByContent(keyword, cursor);
			List<Post> result = postRepository.findAllById(esResult.getRecords());
			
			return CursorPage.<PostDTO>builder()
					.after(esResult.getAfter())
					.before(esResult.getBefore())
					.limit(esResult.getLimit())
					.total(result.size())
					.records(result.stream().map(postMapper::entityToDto).toList())
					.build();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public CursorPage<PostDTO> findByUserId(Integer userId, Cursor cursor) {
		try {
			CursorPage<Integer> esResult = postRepositoryCustom.findByUserIdOrderByCreatedDateDesc(userId, cursor);
			List<Post> result = postRepository.findAllById(esResult.getRecords());
			
			return CursorPage.<PostDTO>builder()
					.after(esResult.getAfter())
					.before(esResult.getBefore())
					.limit(esResult.getLimit())
					.total(result.size())
					.records(result.stream().map(postMapper::entityToDto).toList())
					.build();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
