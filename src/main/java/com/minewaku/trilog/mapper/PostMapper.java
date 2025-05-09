package com.minewaku.trilog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.minewaku.trilog.dto.PostDTO;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.search.document.ESPost;

@Mapper(componentModel = "spring", 
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
	
	@Mapping(source = "userId", target = "user.id")
	@Mapping(target = "views", ignore = true)
	@Mapping(target = "likes", ignore = true)
	@Mapping(target = "comments", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "modifiedDate", ignore = true)
	@Mapping(source = "lat", target = "locationLat")
	@Mapping(source = "lon", target = "locationLon")
	@Mapping(target = "user", ignore = true)
	Post dtoToEntity(PostDTO dto);
	
	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "locationLat", target = "lat")
	@Mapping(source = "locationLon", target = "lon")
	PostDTO entityToDto(Post entity);
	
	
	ESPost dtoToESPost(PostDTO dto);
	
}


