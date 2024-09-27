package com.minewaku.trilog.converter;

import com.minewaku.trilog.dto.PermissionDTO;
import com.minewaku.trilog.entity.Permission;

import org.springframework.stereotype.Component;

@Component
public class PermissionConverter extends BaseConverter<PermissionDTO, Permission> {

    public PermissionConverter() {
        super(PermissionDTO.class, Permission.class);
    }
}
