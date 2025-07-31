package com.minewaku.trilog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minewaku.trilog.entity.MediaPost;

@Repository
public interface MediaPostRepository extends JpaRepository<MediaPost, Integer> {

}
