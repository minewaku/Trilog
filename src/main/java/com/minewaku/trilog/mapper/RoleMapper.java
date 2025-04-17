package com.minewaku.trilog.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.entity.Role;

@Mapper(componentModel = "spring", uses = BaseMapper.class)
public interface RoleMapper {
    
    @InheritConfiguration
    RoleDTO entityToDto(Role entity);

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role dtoToEntity(RoleDTO dto);
}
