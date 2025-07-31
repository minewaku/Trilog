package com.minewaku.trilog.service;

import com.minewaku.trilog.entity.UserRole;

public interface IUserRoleService {	
	UserRole createDefaultUserRole(Integer userId);
}
