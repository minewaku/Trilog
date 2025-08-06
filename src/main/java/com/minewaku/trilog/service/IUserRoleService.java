package com.minewaku.trilog.service;

import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.entity.UserRole;

public interface IUserRoleService {	
	UserRole createDefaultUserRole(User user);
}
