package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.MediaPost;
import com.example.entity.Post;

@Repository
public interface MediaPostRepository extends JpaRepository<MediaPost, Integer> {
    
    List<MediaPost> findByPostId(Integer postId);
    
    List<MediaPost> findByPost(Post post);
    
    List<MediaPost> findByPostOrderByDisplayOrder(Post post);
    
    List<MediaPost> findByIsThumbnailTrue();
    
    @Query("SELECT mp FROM MediaPost mp WHERE mp.post.id = :postId ORDER BY mp.displayOrder ASC")
    List<MediaPost> findByPostIdOrderByDisplayOrder(@Param("postId") Integer postId);
    
    @Query("SELECT mp FROM MediaPost mp WHERE mp.post.id = :postId AND mp.isThumbnail = true")
    List<MediaPost> findThumbnailsByPostId(@Param("postId") Integer postId);
    
    void deleteByPost(Post post);
}