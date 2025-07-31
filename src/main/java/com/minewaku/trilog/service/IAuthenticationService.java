package com.minewaku.trilog.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.minewaku.trilog.dto.common.request.AuthenicationRequest;
import com.minewaku.trilog.dto.common.request.RegisterRequest;
import com.minewaku.trilog.dto.common.request.SendVerifyEmailRequest;
import com.minewaku.trilog.dto.common.response.AuthenicationResponse;
import com.minewaku.trilog.dto.common.response.StatusResponse;

public interface IAuthenticationService {
	ResponseEntity<StatusResponse> register(RegisterRequest request);
	ResponseEntity<Map<String, Object>> authenicate(AuthenicationRequest request);
	ResponseEntity<AuthenicationResponse> refresh(String refreshToken);
	ResponseEntity<String> verifyEmail(String token);
	ResponseEntity<StatusResponse> sendVerifyEmail(SendVerifyEmailRequest request);
}
