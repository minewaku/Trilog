package com.minewaku.trilog.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.LikeDTO;
import com.minewaku.trilog.entity.Like;
import com.minewaku.trilog.entity.EmbededId.LikeId;

public interface ILikeService {
	Page<LikeDTO> findAll(Pageable pageable, Map<String, String> params);
	void cachedLikePost(Integer postId);
	void uncachedLikePost(Integer postId);
	List<Like> saveAll(List<Like> entities);
	void deleteAll(List<LikeId> ids);
}
