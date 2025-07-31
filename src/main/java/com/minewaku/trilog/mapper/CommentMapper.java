package com.minewaku.trilog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.minewaku.trilog.dto.Comment.CommentDTO;
import com.minewaku.trilog.entity.Comment;

@Mapper(componentModel = "spring", 
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
	
	@Mapping(target = "content", source = "content")
	@Mapping(target = "postId", source = "post.id")
	CommentDTO entityToDto(Comment entity);
}
