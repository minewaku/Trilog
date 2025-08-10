package com.minewaku.trilog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.Comment.CommentDTO;
import com.minewaku.trilog.mapper.CommentMapper;
import com.minewaku.trilog.repository.CommentRepository;
import com.minewaku.trilog.service.ICommentService;

@Service
public class CommentService implements ICommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentMapper commentMapper;
	
	@Override
	public List<CommentDTO> findAllByPostId(Integer postId, Pageable pageable) {
	    return commentRepository.findByPostIdAndReplyToIsNullOrderByIdAsc(postId, pageable)
	            .stream()
	            .map(comment -> commentMapper.entityToDto(comment))
	            .toList();
	}
	
	@Override
	public List<CommentDTO> findReplyComments(Integer commentId, Pageable pageable) {
		return commentRepository.findByReplyToIdOrderByIdAsc(commentId, pageable).stream().map(comment -> commentMapper.entityToDto(comment)).toList();
	}

}
