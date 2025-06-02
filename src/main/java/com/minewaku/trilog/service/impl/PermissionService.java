package com.minewaku.trilog.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.minewaku.trilog.dto.PermissionDTO;
import com.minewaku.trilog.entity.Permission;
import com.minewaku.trilog.mapper.PermissionMapper;
import com.minewaku.trilog.repository.PermissionRepository;
import com.minewaku.trilog.service.IPermissionService;
import com.minewaku.trilog.specification.PermissionSpecification;
import com.minewaku.trilog.util.MessageUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PermissionService implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private PermissionMapper mapper;

	@Override
	public Page<PermissionDTO> findAll(Map<String, String> params, Pageable pageable) {
        try {
            Specification<Permission> spec = Specification.where(null);
            Set<String> allowedParams = new HashSet<>(Arrays.asList("name", "email", "phone"));

            if (!params.keySet().stream().allMatch(allowedParams::contains)) {
                throw new IllegalArgumentException();
            }

            if(StringUtils.hasLength(params.get("name"))) {
                spec = spec.and(PermissionSpecification.hasName(params.get("name")));
            }

            if(StringUtils.hasLength(params.get("description"))) {
                spec = spec.and(PermissionSpecification.hasDescription(params.get("description")));
            }

            Page<Permission> permissions = permissionRepository.findAll(spec, pageable);
            
            if(permissions.isEmpty()) {
                throw new EntityNotFoundException(MessageUtil.getMessage("error.get.permission"));
            }

            return permissions.map(permission -> mapper.entityToDto(permission));
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage("invalid.params.search"));
        } catch(EntityNotFoundException e) {
            throw new EntityNotFoundException(MessageUtil.getMessage("error.get.permission"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
	}

    @Override
    public Page<PermissionDTO> search(Map<String, String> params, Pageable pageable) {
        try {
            Specification<Permission> spec = Specification.where(null);
            Set<String> allowedParams = new HashSet<>(Arrays.asList("name", "email", "phone"));

            if (!params.keySet().stream().allMatch(allowedParams::contains)) {
                throw new IllegalArgumentException();
            }

            if(StringUtils.hasLength(params.get("name"))) {
                spec = spec.and(PermissionSpecification.containsName(params.get("name")));
            }

            if(StringUtils.hasLength(params.get("description"))) {
                spec = spec.and(PermissionSpecification.containsDescription(params.get("description")));
            }

            Page<Permission> permissions = permissionRepository.findAll(spec, pageable);
            return permissions.map(permission -> mapper.entityToDto(permission));
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage("invalid.params.search"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public PermissionDTO findById(int id) {
        try {
            Permission permission = permissionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageUtil.getMessage("error.get.permission")));
            return mapper.entityToDto(permission);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public PermissionDTO create(PermissionDTO permission) {
        try {
            Permission savedPermission = permissionRepository.save(mapper.dtoToEntity(permission)); 
            return mapper.entityToDto(savedPermission);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public PermissionDTO update(int id, PermissionDTO permission) {
        try {
            permissionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageUtil.getMessage("error.get.permission"))); 
            Permission savedPermission = permissionRepository.save(mapper.dtoToEntity(permission));
            return mapper.entityToDto(savedPermission);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(List<Integer> ids) {
        try {
            permissionRepository.deleteAllById(ids);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public PermissionDTO findByName(String name) {
        try {
            Permission permission = permissionRepository.findByName(name).orElseThrow(() -> new Exception(MessageUtil.getMessage("error.get.permission")));
            return mapper.entityToDto(permission);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
