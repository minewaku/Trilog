package com.minewaku.trilog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.entity.Role;
import com.minewaku.trilog.entity.UserRole;
import com.minewaku.trilog.entity.EmbededId.UserRoleId;
import com.minewaku.trilog.repository.RoleRepository;
import com.minewaku.trilog.repository.UserRoleRepository;
import com.minewaku.trilog.service.IUserRoleService;
import com.minewaku.trilog.util.MessageUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserRoleService implements IUserRoleService {
	
	@Value("${app.default-role}")
	private String defaultRoleName;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public UserRole createDefaultUserRole(Integer userId) {

		Role defaultRole = roleRepository.findByName(defaultRoleName).orElseThrow(() -> new EntityNotFoundException(MessageUtil.getMessage("error.get.role"))); 
		UserRoleId id = new UserRoleId(userId, defaultRole.getId());
		UserRole userRole = UserRole.builder().id(id).build();
		return userRoleRepository.save(userRole);
	}
}
