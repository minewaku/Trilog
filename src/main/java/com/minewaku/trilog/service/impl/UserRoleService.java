package com.minewaku.trilog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.entity.Role;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.entity.UserRole;
import com.minewaku.trilog.entity.EmbededId.UserRoleId;
import com.minewaku.trilog.repository.RoleRepository;
import com.minewaku.trilog.repository.UserRoleRepository;
import com.minewaku.trilog.service.IUserRoleService;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.LogUtil;

@Service
public class UserRoleService implements IUserRoleService {
	
	@Value("${app.default-role}")
	private String defaultRoleName;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ErrorUtil errorUtil;
	
	@Override
	public UserRole createDefaultUserRole(User user) {

		Role defaultRole = roleRepository.findByName(defaultRoleName).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
		LogUtil.LOGGER.error(defaultRole.toString());
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(defaultRole);
		userRole.setId(new UserRoleId(user.getId(), defaultRole.getId()));
		
		return userRoleRepository.save(userRole);
	}
}
