package com.minewaku.trilog.service;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;

public interface IRoleService {
    Page<RoleDTO> findAll(Map<String, String> params, Pageable pageable);
    
    void addRolesToUser(Integer userId, List<Integer> roleIds);
    void removeRolesFromUser(Integer userId, List<Integer> roleIds);
    
    void addPermissionsToRole(Integer roleId, List<Integer> permissionIds);
    void removePermissionsFromRole(Integer roleId, List<Integer> permissionIds);

    RoleDTO findById(Integer id);
    
    RoleDTO create(RoleDTO role);
    RoleDTO update(Integer id, RoleDTO role);
    void delete(List<Integer> ids);
}
