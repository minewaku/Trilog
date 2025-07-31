package com.minewaku.trilog.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.PermissionDTO;

public  interface IPermissionService {
    Page<PermissionDTO> findAll(Map<String, String> params, Pageable pageable);

    PermissionDTO findById(Integer id);
    PermissionDTO create(PermissionDTO Permission);
    PermissionDTO update(Integer id, PermissionDTO Permission);
    void delete(List<Integer> ids);

    PermissionDTO findByName(String name);
    
}
