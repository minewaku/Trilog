package com.minewaku.trilog.mapper;

import org.mapstruct.Mapper;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.entity.Media;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    MediaDTO entityToDto(Media entity);

    Media dtoToEntity(MediaDTO dto);
}
