package com.minewaku.trilog.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.minewaku.trilog.dto.Role.RoleDTO;
import com.minewaku.trilog.dto.Role.UpdatedRoleDTO;
import com.minewaku.trilog.entity.Role;

@Mapper(componentModel = "spring", uses = BaseMapper.class, 
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    
    @InheritConfiguration
    RoleDTO entityToDto(Role entity);

    Role dtoToEntity(RoleDTO dto);
    
    Role updateFromDtoToEntity(UpdatedRoleDTO source, @MappingTarget Role target);
    
}
