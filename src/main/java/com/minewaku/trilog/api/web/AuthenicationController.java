package com.minewaku.trilog.api.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minewaku.trilog.dto.common.request.AuthenicationRequest;
import com.minewaku.trilog.dto.common.request.RegisterRequest;
import com.minewaku.trilog.dto.common.request.SendVerifyEmailRequest;
import com.minewaku.trilog.dto.common.response.AuthenicationResponse;
import com.minewaku.trilog.dto.common.response.StatusResponse;
import com.minewaku.trilog.service.impl.AuthenicationService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenicationController {

    @Autowired
    private AuthenicationService authenicationService;
    
    @PostMapping("/register")
    public ResponseEntity<StatusResponse> register(@RequestBody RegisterRequest request) {
        return authenicationService.register(request);
    }

    @PostMapping("/authenicate")
    public ResponseEntity<Map<String, Object>> authenication(@RequestBody AuthenicationRequest request) {
        return authenicationService.authenicate(request);
    }

    @PostMapping("/refresh")
	public ResponseEntity<AuthenicationResponse> refreshToken(@CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {
		return authenicationService.refresh(refreshToken);
	}
    
    @PostMapping("/send-verify-email")
    public ResponseEntity<StatusResponse> sendVerifyEmail(@RequestBody SendVerifyEmailRequest request) {
        return authenicationService.sendVerifyEmail(request);
    }
    
    @GetMapping("/verify")	
	public ResponseEntity<String> verifyEmail(@RequestParam String token) {
		return authenicationService.verifyEmail(token);
	}
}
