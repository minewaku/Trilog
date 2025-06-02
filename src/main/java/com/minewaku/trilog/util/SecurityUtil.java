package com.minewaku.trilog.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
	
	private static Authentication getAuthentication() {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
		if (authContext == null) {
			throw new IllegalStateException("No authentication context found");
		}
        
		return authContext;
    }

    public static Object getPrincipal() {
        Authentication authContext = getAuthentication();
        return authContext.getPrincipal();
    }
}
