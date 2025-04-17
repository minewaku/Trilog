package com.minewaku.trilog.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.PermissionDTO;

public interface IPermissionService {
    Page<PermissionDTO> findAll(Map<String, String> params, Pageable pageable);
    Page<PermissionDTO> search(Map<String, String> params, Pageable pageable);

    PermissionDTO findById(int id);
    PermissionDTO create(PermissionDTO Permission);
    PermissionDTO update(int id, PermissionDTO Permission);
    void delete(int id);

    PermissionDTO findByName(String name);
    
}
