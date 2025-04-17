package com.minewaku.trilog.service;


import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.RoleDTO;

public interface IRoleService {
    Page<RoleDTO> findAll(Map<String, String> params, Pageable pageable);
    Page<RoleDTO> search(Map<String, String> params, Pageable pageable);

    RoleDTO findById(int id);
    RoleDTO create(RoleDTO role);
    RoleDTO update(int id, RoleDTO role);
    void delete(int id);
}
