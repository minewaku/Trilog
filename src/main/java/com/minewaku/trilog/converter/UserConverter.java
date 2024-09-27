package com.minewaku.trilog.converter;

import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.entity.User;

import org.springframework.stereotype.Component;

@Component
public class UserConverter extends BaseConverter<UserDTO, User> {
    public UserConverter() {
        super(UserDTO.class, User.class);
    }
    
}
