package com.minewaku.trilog.mapper;

import org.mapstruct.Mapper;

import com.minewaku.trilog.dto.BaseDTO;
import com.minewaku.trilog.entity.BaseEntity;

@Mapper(componentModel = "spring")
public interface BaseMapper {
    BaseDTO entityToDto(BaseEntity entity);
}