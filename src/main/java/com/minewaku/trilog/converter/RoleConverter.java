package com.minewaku.trilog.converter;

import org.springframework.stereotype.Component;

import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.entity.Role;

@Component
public class RoleConverter extends BaseConverter<RoleDTO, Role>{

    public RoleConverter() {
        super(RoleDTO.class, Role.class);
    }
}
