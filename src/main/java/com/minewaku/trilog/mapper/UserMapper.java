package com.minewaku.trilog.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.minewaku.trilog.dto.User.UpdatedUserDTO;
import com.minewaku.trilog.dto.User.UserDTO;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.entity.UserRole;
import com.minewaku.trilog.util.LogUtil;

@Mapper(componentModel = "spring", uses = BaseMapper.class, 
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
//Ignore null values in the source
public interface UserMapper {

    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "userRolesToStrings")
    @Mapping(target = "imageUrl", source = "image", qualifiedByName = "mediaToSecureUrl")
    @Mapping(target = "coverUrl", source = "cover", qualifiedByName = "mediaToSecureUrl")
    UserDTO entityToDto(User entity);

    @Mapping(target = "hashed_password", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "cover", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    User dtoToEntity(UserDTO dto);
    
    User updateFromDtoToEntity(UpdatedUserDTO source, @MappingTarget User target);

    @Named("userRolesToStrings")
    default List<String> rolesToStrings(List<UserRole> userRoles) {
        if (userRoles == null) {
            return null;
        }
        LogUtil.LOGGER.error("Converting user roles to strings: {}", userRoles);
        return userRoles.stream()
            .map(userRole -> userRole.getRole().getName())
            .toList();
    }

    @Named("mediaToSecureUrl")
    default String mediaToSecureUrl(Media media) {
    	return media != null ? media.getSecureUrl() : null;
    }
}