package com.minewaku.trilog.mapper;

import org.mapstruct.Mapper;

import com.minewaku.trilog.dto.LikeDTO;
import com.minewaku.trilog.entity.Like;

@Mapper(componentModel = "spring")
public interface LikeMapper {
	 LikeDTO entityToDto(Like entity);
	 Like dtoToEntity(LikeDTO dto);
}
