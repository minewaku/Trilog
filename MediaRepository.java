package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {
    
    Optional<Media> findByPublicId(String publicId);
    
    Optional<Media> findBySecureUrl(String secureUrl);
    
    List<Media> findByFileType(String fileType);
    
    @Query("SELECT m FROM Media m WHERE m.mediaPost IS NULL")
    List<Media> findUnusedMedia();
    
    @Query("SELECT m FROM Media m WHERE m.fileSize > :size")
    List<Media> findByFileSizeGreaterThan(@Param("size") Long size);
}