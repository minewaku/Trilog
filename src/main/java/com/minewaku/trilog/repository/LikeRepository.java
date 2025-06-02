package com.minewaku.trilog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minewaku.trilog.entity.Like;
import com.minewaku.trilog.entity.EmbededId.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

}
