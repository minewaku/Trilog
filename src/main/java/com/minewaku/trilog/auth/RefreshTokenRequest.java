package com.minewaku.trilog.auth;

import jakarta.servlet.http.Cookie;

public class RefreshTokenRequest {
	private Cookie refreshToken;
	
	public RefreshTokenRequest(Cookie refreshToken) {
		this.refreshToken = refreshToken;
	}	
	
	public Cookie getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(Cookie refreshToken) {
		this.refreshToken = refreshToken;
	}
}
