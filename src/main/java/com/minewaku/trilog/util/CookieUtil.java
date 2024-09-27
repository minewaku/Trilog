package com.minewaku.trilog.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {

	public Cookie createCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		return cookie;
	}
	
	public HttpServletResponse setCookie(HttpServletResponse response, Cookie[] cookies) {
		for(Cookie cookie : cookies) {
            response.addCookie(cookie);
        }
		
		return response;
	}
}