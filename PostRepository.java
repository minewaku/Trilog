package com.example.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Post;
import com.example.entity.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    
    List<Post> findByUserId(Integer userId);
    
    List<Post> findByUser(User user);
    
    List<Post> findByStatus(Integer status);
    
    Page<Post> findByStatusOrderByCreatedDateDesc(Integer status, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.status = :status ORDER BY p.createdDate DESC")
    List<Post> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") Integer status);
    
    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword% AND p.status = 1")
    List<Post> findByContentContainingAndPublished(@Param("keyword") String keyword);
    
    @Query("SELECT p FROM Post p JOIN p.mediaPosts mp WHERE mp.media.id = :mediaId")
    Post findByMediaId(@Param("mediaId") Integer mediaId);
    
    @Query("SELECT p FROM Post p WHERE p.createdDate BETWEEN :startDate AND :endDate ORDER BY p.createdDate DESC")
    List<Post> findByCreatedDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId AND p.status = 1")
    Long countPublishedPostsByUser(@Param("userId") Integer userId);
}