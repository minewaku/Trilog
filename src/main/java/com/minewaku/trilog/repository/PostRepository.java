package com.minewaku.trilog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.minewaku.trilog.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.status = 3 WHERE p.id IN :ids")
    int softDeletePosts(@Param("ids") List<Integer> ids);
}
