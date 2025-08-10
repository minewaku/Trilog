package com.minewaku.trilog.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.LikeDTO;
import com.minewaku.trilog.entity.Like;
import com.minewaku.trilog.entity.EmbededId.LikeId;

public interface ILikeService {
	Page<LikeDTO> findAll(Map<String, String> params, Pageable pageable);
	void cachedLikePost(Integer postId);
	List<Like> saveAll(List<Like> entities);
}
