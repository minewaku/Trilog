package com.minewaku.trilog.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.entity.Role;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.exception.UserNotFoundException;
import com.minewaku.trilog.repository.RoleRepository;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.util.JwtUtil;
import com.minewaku.trilog.util.LogUtil;
import com.minewaku.trilog.util.MessageUtil;

@Service
public class AuthenicationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenicationManager;
    
    public ResponseEntity<AuthenicationResponse> register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(s -> {
            throw new UserNotFoundException(MessageUtil.MESSAGES.getString("register.error.email.exists"));
        });

        Role defaultRole = roleRepository.findByName("user").orElseThrow(() -> new UserNotFoundException("Internal Error! Unable to create account"));
        var user = User.builder().email(request.getEmail())
                                .role(defaultRole)
                                .name(request.getName())
                                .hashed_password(passwordEncoder.encode(request.getPassword()))
                                .birthdate(request.getBirthdate())
                                .phone(request.getPhone())
                                .address(request.getAddress())
                                .build();
                    
        userRepository.save(user);
        var jwtToken = jwtUtil.generateJwtToken(user);
        var refreshToken = jwtUtil.generateRefreshToken();
        jwtUtil.saveRefreshToken(refreshToken, jwtToken, user);
        
        LogUtil.LOGGER.info("returning JWT: " + jwtToken); 
        LogUtil.LOGGER.info("returning refresh token: " + refreshToken);
        AuthenicationResponse response = new AuthenicationResponse(jwtToken);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshToken.toString());
        return ResponseEntity.ok().headers(headers).body(response);
    }
    

    public ResponseEntity<AuthenicationResponse> authenicate(AuthenicationRequest request) {
        authenicationManager.authenticate(
           new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException(MessageUtil.MESSAGES.getString("error.getting.user")));
        var jwtToken = jwtUtil.generateJwtToken(user);
        var refreshToken = jwtUtil.generateRefreshToken();
        jwtUtil.saveRefreshToken(refreshToken, jwtToken, user);
        
        LogUtil.LOGGER.info("returning JWT: " + jwtToken); 
        LogUtil.LOGGER.info("returning refresh token: " + refreshToken);
        AuthenicationResponse response = new AuthenicationResponse(jwtToken);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshToken.toString());
        return ResponseEntity.ok().headers(headers).body(response);
    }
    
    
	public ResponseEntity<AuthenicationResponse> refresh(String refreshToken) {
		if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
			throw new RuntimeException(MessageUtil.MESSAGES.getString("refresh.token.invalid") + " - " + refreshToken);
		} else if(jwtUtil.isRefreshTokenExpired(refreshToken)) {
			 jwtUtil.deleteRefreshToken(refreshToken);
			 throw new RuntimeException(MessageUtil.MESSAGES.getString("refresh.token.expired") + " - " + refreshToken);
		}
		
		Map<Object, Object> refreshData = jwtUtil.getRefreshData(refreshToken);
		var user = userRepository.findByEmail((String) refreshData.get("email")).orElseThrow(() -> new UsernameNotFoundException(MessageUtil.MESSAGES.getString("error.getting.user")));
		var newJwtToken = jwtUtil.generateJwtToken(user);
        var newRefreshToken = jwtUtil.generateRefreshToken();
        jwtUtil.saveRefreshToken(newRefreshToken, newJwtToken, user);
        jwtUtil.deleteRefreshToken(refreshToken);
        
        LogUtil.LOGGER.info("returning JWT: " + newJwtToken); 
        LogUtil.LOGGER.info("returning refresh token: " + newRefreshToken);
        AuthenicationResponse response = new AuthenicationResponse(newJwtToken);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, newRefreshToken.toString());
        return ResponseEntity.ok().headers(headers).body(response);
	}
    
}
