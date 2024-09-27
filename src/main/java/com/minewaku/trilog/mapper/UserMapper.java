package com.minewaku.trilog.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.entity.User;

@Mapper(componentModel = "spring", uses = BaseMapper.class)
public interface UserMapper {

    @InheritConfiguration
    UserDTO entityToDto(User entity);

    @Mapping(target = "hashed_password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "cover", ignore = true)
    User dtoToEntity(UserDTO dto);
}
