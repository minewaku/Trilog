package com.minewaku.trilog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.Media.MediaPostDTO;
import com.minewaku.trilog.dto.Media.SavedMediaDTO;
import com.minewaku.trilog.dto.Media.SavedMediaPostDTO;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.entity.MediaPost;

@Mapper(componentModel = "spring", 
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MediaMapper {
    MediaDTO entityToDto(Media entity);

    Media dtoToEntity(MediaDTO dto);
    
    Media savedMediaDtoToEntity(SavedMediaDTO dto);
    
    
    @Mapping(source = "id", target = "media.id")
    @Mapping(source = "publicId", target = "media.publicId")
    @Mapping(source = "secureUrl", target = "media.secureUrl")
	@Mapping(source = "displayOrder", target = "displayOrder")
	@Mapping(source = "isThumbnail", target = "isThumbnail")
    MediaPost savedMediaPostDtoToEntity(SavedMediaPostDTO dto);
    
    @Mapping(source = "media.id", target = "id")
    @Mapping(source = "media.publicId", target = "publicId")
    @Mapping(source = "media.secureUrl", target = "secureUrl")
	@Mapping(source = "displayOrder", target = "displayOrder")
	@Mapping(source = "isThumbnail", target = "isThumbnail")
    MediaPostDTO mediaPostEntityToMediaPostDto(MediaPost entity);
    
    @Mapping(source = "publicId", target = "media.publicId")
    @Mapping(source = "secureUrl", target = "media.secureUrl")
	@Mapping(source = "displayOrder", target = "displayOrder")
	@Mapping(source = "isThumbnail", target = "isThumbnail")
    MediaPost mediaPostDtoToMediaPostEntity(MediaPostDTO dto);
}
