package com.minewaku.trilog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minewaku.trilog.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
