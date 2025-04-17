package com.minewaku.trilog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minewaku.trilog.entity.Media;

public interface MediaRepository extends JpaRepository<Media, Integer> {
    
}
