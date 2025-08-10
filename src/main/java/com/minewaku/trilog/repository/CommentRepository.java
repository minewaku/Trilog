package com.minewaku.trilog.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.minewaku.trilog.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	List<Comment> findByPostIdAndReplyToIsNullOrderByIdAsc(Integer postId, Pageable pageable);
	List<Comment> findByReplyToIdOrderByIdAsc(Integer replyToId, Pageable pageable);
}
