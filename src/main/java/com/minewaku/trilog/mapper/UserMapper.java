package com.minewaku.trilog.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.entity.Role;
import com.minewaku.trilog.entity.User;

@Mapper(componentModel = "spring", uses = BaseMapper.class, 
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
//Ignore null values in the source
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
    @Mapping(target = "imageUrl", source = "image", qualifiedByName = "mediaToSecureUrl")
    @Mapping(target = "coverUrl", source = "cover", qualifiedByName = "mediaToSecureUrl")
    UserDTO entityToDto(User entity);

    @Mapping(target = "hashed_password", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "cover", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User dtoToEntity(UserDTO dto);
    
    @Mapping(target = "roles", ignore = true)
    void update(@MappingTarget User target, UserDTO source);

    @Named("rolesToStrings")
    default List<String> rolesToStrings(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
            .map(Role::getName)
            .toList();
    }

    @Named("mediaToSecureUrl")
    default String mediaToSecureUrl(Media media) {
    	return media != null ? media.getSecureUrl() : null;
    }
}