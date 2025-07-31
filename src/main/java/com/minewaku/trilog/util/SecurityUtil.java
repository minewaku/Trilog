package com.minewaku.trilog.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class SecurityUtil {
	
	@Transactional
	private static Authentication getAuthentication() {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
		if (authContext == null) {
			throw new IllegalStateException("No authentication context found");
		}
        
		return authContext;
    }

	@Transactional
    public static Object getPrincipal() {
        Authentication authContext = getAuthentication();
        return authContext.getPrincipal();
    }
}
