package com.minewaku.trilog.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.Comment.CommentDTO;

public interface ICommentService {
	List<CommentDTO> findAllByPostId(Integer postId, Pageable pageable);
	List<CommentDTO> findReplyComments(Integer commentId, Pageable pageable);
}
