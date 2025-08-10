package com.minewaku.trilog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.entity.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>,  JpaSpecificationExecutor<Post>{
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.status = 3 WHERE p.id IN :ids")
    int softDeletePosts(@Param("ids") List<Integer> ids);
    
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + :count WHERE p.id = :postId")
    void incrementLikeCount(@Param("postId") Integer postId, @Param("count") int count);

}
