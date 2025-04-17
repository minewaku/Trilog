package com.minewaku.trilog.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.minewaku.trilog.dto.PermissionDTO;
import com.minewaku.trilog.entity.Permission;

@Mapper(componentModel = "spring", uses = BaseMapper.class)
public interface PermissionMapper {
        
    @InheritConfiguration
    PermissionDTO entityToDto(Permission entity);
    
    @Mapping(target = "roles", ignore = true)
    Permission dtoToEntity(PermissionDTO dto);
}
