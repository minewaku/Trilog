package com.minewaku.trilog.service.impl;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.entity.Role;
import com.minewaku.trilog.mapper.RoleMapper;
import com.minewaku.trilog.repository.RoleRepository;
import com.minewaku.trilog.service.IRoleService;
import com.minewaku.trilog.specification.RoleSpecification;
import com.minewaku.trilog.util.MessageUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RoleService implements IRoleService {
	
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper mapper;

	@Override
	public Page<RoleDTO> findAll(Map<String, String> params, Pageable pageable) {
        try {
            Specification<Role> spec = Specification.where(null);
            Set<String> allowedParams = new HashSet<>(Arrays.asList("name", "email", "phone"));

            if (!params.keySet().stream().allMatch(allowedParams::contains)) {
                throw new IllegalArgumentException();
            }

            if(StringUtils.hasLength(params.get("name"))) {
                spec = spec.and(RoleSpecification.hasName(params.get("name")));
            }

            if(StringUtils.hasLength(params.get("description"))) {
                spec = spec.and(RoleSpecification.hasDescription(params.get("description")));
            }

            Page<Role> roles = roleRepository.findAll(spec, pageable);
            return roles.map(role -> mapper.entityToDto(role));
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtil.MESSAGES.getString("invalid.params.search"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
	}

    @Override
    public Page<RoleDTO> search(Map<String, String> params, Pageable pageable) {
        try {
            Specification<Role> spec = Specification.where(null);
            Set<String> allowedParams = new HashSet<>(Arrays.asList("name", "email", "phone"));

            if (!params.keySet().stream().allMatch(allowedParams::contains)) {
                throw new IllegalArgumentException();
            }

            if(StringUtils.hasLength(params.get("name"))) {
                spec = spec.and(RoleSpecification.containsName(params.get("name")));
            }

            if(StringUtils.hasLength(params.get("description"))) {
                spec = spec.and(RoleSpecification.containsDescription(params.get("description")));
            }

            Page<Role> roles = roleRepository.findAll(spec, pageable);
            return roles.map(role -> mapper.entityToDto(role));
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtil.MESSAGES.getString("invalid.params.search"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public RoleDTO findById(int id) {
        try {
            Role role = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageUtil.MESSAGES.getString("error.get.role"))); 
            return mapper.entityToDto(role);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public RoleDTO create(RoleDTO role) {
        try {
            Role savedRole = roleRepository.save(mapper.dtoToEntity(role)); 
            return mapper.entityToDto(savedRole);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public RoleDTO update(int id, RoleDTO role) {
        try {
            roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageUtil.MESSAGES.getString("error.get.role"))); 
            Role savedRole = roleRepository.save(mapper.dtoToEntity(role)); 
            return mapper.entityToDto(savedRole);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageUtil.MESSAGES.getString("error.get.role"))); 
            roleRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}