package com.minewaku.trilog.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.PermissionDTO;
import com.minewaku.trilog.entity.Permission;
import com.minewaku.trilog.entity.Permission_;
import com.minewaku.trilog.mapper.PermissionMapper;
import com.minewaku.trilog.repository.PermissionRepository;
import com.minewaku.trilog.service.IPermissionService;
import com.minewaku.trilog.specification.SpecificationBuilder;
import com.minewaku.trilog.util.ErrorUtil;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.metamodel.SingularAttribute;

@Service
public class PermissionService implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private SpecificationBuilder<Permission> specBuilder;
    
    @Autowired
    private ErrorUtil errorUtil;
    
    private Set<SingularAttribute<Permission, ?> > allowedFieldsForFetch = new HashSet<>();

    @PostConstruct
    public void init() {
        allowedFieldsForFetch.add(Permission_.name);
        allowedFieldsForFetch.add(Permission_.description);
        @SuppressWarnings("unchecked")
        SingularAttribute<Permission, ?> createdDate = (SingularAttribute<Permission, ?>) (SingularAttribute<?, ?>) Permission_.createdDate;
        allowedFieldsForFetch.add(createdDate);
    }

	@Override
	public Page<PermissionDTO> findAll(Map<String, String> params, Pageable pageable) {
        try {
        	Specification<Permission> spec = specBuilder.buildSpecification(params, allowedFieldsForFetch);
            Page<Permission> permissions = permissionRepository.findAll(spec, pageable);
            Page<PermissionDTO> permissionDTOs = permissions.map(permission -> permissionMapper.entityToDto(permission));
            return permissionDTOs;
        } catch(IllegalArgumentException e) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
	}

    @Override
    public PermissionDTO findById(Integer id) {
        try {
            Permission permission = permissionRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.PERMISSION_NOT_FOUND));
            return permissionMapper.entityToDto(permission);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public PermissionDTO create(PermissionDTO permission) {
        try {
            Permission savedPermission = permissionRepository.save(permissionMapper.dtoToEntity(permission)); 
            return permissionMapper.entityToDto(savedPermission);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public PermissionDTO update(Integer id, PermissionDTO permission) {
        try {
            permissionRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.PERMISSION_NOT_FOUND)); 
            Permission savedPermission = permissionRepository.save(permissionMapper.dtoToEntity(permission));
            return permissionMapper.entityToDto(savedPermission);
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
            Permission permission = permissionRepository.findByName(name).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.PERMISSION_NOT_FOUND));
            return permissionMapper.entityToDto(permission);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
