package com.minewaku.trilog.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenicationController {

    @Autowired
    private AuthenicationService authenicationService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthenicationResponse> register(@RequestBody RegisterRequest request) {
        return authenicationService.register(request);
    }

    @PostMapping("/authenicate")
    public ResponseEntity<AuthenicationResponse> authenication(@RequestBody AuthenicationRequest request) {
        return authenicationService.authenicate(request);
    }

    @PostMapping("/refresh")
	public ResponseEntity<AuthenicationResponse> refreshToken(@CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {
		return authenicationService.refresh(refreshToken);
	}
}
