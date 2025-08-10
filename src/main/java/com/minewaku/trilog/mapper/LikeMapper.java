package com.minewaku.trilog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.minewaku.trilog.dto.LikeDTO;
import com.minewaku.trilog.entity.Like;

@Mapper(componentModel = "spring")
public interface LikeMapper {
	
	@Mapping(target = "postId", source = "post.id")
	@Mapping(target = "userId", source = "user.id")
	 LikeDTO entityToDto(Like entity);
	 Like dtoToEntity(LikeDTO dto);
}
