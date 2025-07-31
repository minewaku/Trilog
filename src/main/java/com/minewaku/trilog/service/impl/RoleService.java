package com.minewaku.trilog.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.entity.Permission;
import com.minewaku.trilog.entity.Role;
import com.minewaku.trilog.entity.RolePermission;
import com.minewaku.trilog.entity.Role_;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.entity.UserRole;
import com.minewaku.trilog.entity.EmbededId.RolePermissionId;
import com.minewaku.trilog.entity.EmbededId.UserRoleId;
import com.minewaku.trilog.mapper.RoleMapper;
import com.minewaku.trilog.repository.PermissionRepository;
import com.minewaku.trilog.repository.RoleRepository;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.service.IRoleService;
import com.minewaku.trilog.specification.RoleSpecification;
import com.minewaku.trilog.specification.SpecificationBuilder;
import com.minewaku.trilog.util.ErrorUtil;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.metamodel.SingularAttribute;

@Service
public class RoleService implements IRoleService {
	
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleMapper mapper;
    
    @Autowired
    private ErrorUtil errorUtil;
    
    @Autowired
    private SpecificationBuilder<Role> specBuilder;
    
    private Set<SingularAttribute<Role, ?> > allowedFieldsForFetch = new HashSet<>();

    @PostConstruct
    public void init() {
        allowedFieldsForFetch.add(Role_.name);
        allowedFieldsForFetch.add(Role_.description);
        @SuppressWarnings("unchecked")
        SingularAttribute<Role, ?> createdDate = (SingularAttribute<Role, ?>) (SingularAttribute<?, ?>) Role_.createdDate;
        allowedFieldsForFetch.add(createdDate);
    }
    
	@Override
	public Page<RoleDTO> findAll(Map<String, String> params, Pageable pageable) {
        try {
        	Specification<Role> spec = specBuilder.buildSpecification(params, allowedFieldsForFetch);
            Page<Role> roles = roleRepository.findAll(spec, pageable);
            return roles.map(role -> mapper.entityToDto(role));
        } catch(IllegalArgumentException e) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
	}

    @Override
	public void	addRolesToUser(Integer userId, List<Integer> roleIds) {
		try {
			User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
			Set<Role> roles = roleRepository.findAllById(roleIds).stream().collect(Collectors.toSet());

			if (user.getUserRoles() == null) {
				user.setUserRoles(new ArrayList<>());
			}

			user.getUserRoles()
				.addAll(roles.stream()
						.map(role -> UserRole.builder().id(new UserRoleId(user.getId(), role.getId())).build())
						.collect(Collectors.toSet()));
			
			userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

    @Override
    public void removeRolesFromUser(Integer userId, List<Integer> roleIds) {
		User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
		user.setUserRoles(new ArrayList<>());
		userRepository.save(user);
    }
    
    @Override
    public RoleDTO findById(Integer id) {
        try {
            Role role = roleRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.ROLE_NOT_FOUND)); 
            return mapper.entityToDto(role);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
	public void addPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
		try {
			Role role = roleRepository.findById(roleId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.ROLE_NOT_FOUND));
			Set<Permission> permissions = permissionRepository.findAllById(permissionIds).stream().collect(Collectors.toSet());

			if (role.getRolePermissions() == null) {
				role.setRolePermissions(new ArrayList<>());
			}

			role.getRolePermissions().addAll(permissions.stream()
					.map(permisison -> RolePermission.builder().id(new RolePermissionId(role.getId(), permisison.getId())).build())
					.collect(Collectors.toSet()));
			roleRepository.save(role);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
    
	@Override
	public void removePermissionsFromRole(Integer roleId, List<Integer> permissionIds) {
		Role role = roleRepository.findById(roleId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.ROLE_NOT_FOUND));
		role.setRolePermissions(new ArrayList<>());
		roleRepository.save(role);
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
    public RoleDTO update(Integer id, RoleDTO role) {
        try {
            roleRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.ROLE_NOT_FOUND)); 
            Role savedRole = roleRepository.save(mapper.dtoToEntity(role)); 
            return mapper.entityToDto(savedRole);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(List<Integer> ids) {
        try {
        	roleRepository.deleteAllById(ids);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
