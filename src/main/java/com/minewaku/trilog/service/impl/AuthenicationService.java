package com.minewaku.trilog.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.common.request.AuthenicationRequest;
import com.minewaku.trilog.dto.common.request.RegisterRequest;
import com.minewaku.trilog.dto.common.request.SendVerifyEmailRequest;
import com.minewaku.trilog.dto.common.response.AuthenicationResponse;
import com.minewaku.trilog.dto.common.response.StatusResponse;
import com.minewaku.trilog.entity.ConfirmationToken;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.event.EmailVerificationEvent;
import com.minewaku.trilog.mapper.UserMapper;
import com.minewaku.trilog.repository.ConfirmationTokenRepository;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.service.IAuthenticationService;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.JwtUtil;
import com.minewaku.trilog.util.LogUtil;
import com.minewaku.trilog.util.MessageUtil;
import com.minewaku.trilog.util.RedisUtil;

import jakarta.transaction.Transactional;

@Service
public class AuthenicationService implements IAuthenticationService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ErrorUtil errorUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public ResponseEntity<StatusResponse> register(RegisterRequest request) {
		
		// Validating if the email already exists
		userRepository.findByEmail(request.getEmail()).ifPresent(s -> {
			throw errorUtil.ERROR_DETAILS.get(errorUtil.EMAIL_ALREADY_EXISTS);
		});

		// Creating and saving the user with explicit field settings
		User user = User.builder().email(request.getEmail())
				.name(request.getName())
				.hashed_password(passwordEncoder.encode(request.getPassword()))
				.birthdate(request.getBirthdate()).phone(request.getPhone())
				.address(request.getAddress()).isActive(true).isEnabled(false)
				.isLocked(false)
				.isDeleted(false)
				.build();
		user = userRepository.save(user);
		
		// Creating default Role for the user
		userRoleService.createDefaultUserRole(user);

		// Sending email verification event
		eventPublisher.publishEvent(new EmailVerificationEvent(this, user));
		
		return ResponseEntity.ok().body(new StatusResponse(MessageUtil.getMessage("verify.email.success.sent"), ZonedDateTime.now(ZoneId.of("Z"))));
	}
	
	
	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> authenicate(AuthenicationRequest request) {
		try {
			// Authenticating the user with email and password
			Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
	        );
			
			// If authentication is successful, we can retrieve the user details
			var user = (User) authentication.getPrincipal();
			LogUtil.LOGGER.error("what the fuck does the principal returned: " + authentication.getPrincipal().toString());
			LogUtil.LOGGER.error("what the fuck does the principal returned: " + user.getAuthorities());
			
			// Checking if the user account is enabled, locked, or deleted
			if (!user.getIsEnabled()) {
				throw errorUtil.ERROR_DETAILS.get(errorUtil.ACCOUNT_NOT_VERIFIED);
			} else if (user.getIsLocked()) {
				throw errorUtil.ERROR_DETAILS.get(errorUtil.ACCOUNT_LOCKED);
			} else if (user.getIsDeleted()) {
				throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_CREDENTIALS);
			}
			
			// Generating JWT and Refresh Token
			var jwtToken = jwtUtil.generateJwtToken(null, null, user);
			var refreshToken = jwtUtil.generateRefreshToken();
			jwtUtil.saveRefreshToken(refreshToken, jwtToken, user);

			LogUtil.LOGGER.info("returning JWT: " + jwtToken);
			LogUtil.LOGGER.info("returning refresh token: " + refreshToken);
			Map<String, Object> response = new HashMap<>();
			response.put("token", jwtToken);
			response.put("user", userMapper.entityToDto(user));

			// Setting the refresh token in the response headers
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.SET_COOKIE, refreshToken.toString());
			
			return ResponseEntity.ok().headers(headers).body(response);
	    } catch (BadCredentialsException e) {
	        throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_CREDENTIALS);
	    } catch (InternalAuthenticationServiceException e) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_CREDENTIALS);
	    }
	}

	@Override
	public ResponseEntity<AuthenicationResponse> refresh(String refreshToken) {
	
		LogUtil.LOGGER.info("old refresh token: " + refreshToken);
		Map<Object, Object> refreshData = jwtUtil.getRefreshData(RedisUtil.CACHE_PREFIX.REFRESH_TOKENS + refreshToken);
		
		if (refreshData == null) {
			throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_REFRESH_TOKEN);
		}
		
		LogUtil.LOGGER.info("refresh data: " + refreshData);
		
//		} else if (jwtUtil.isRefreshTokenExpired(refreshToken)) {
//			jwtUtil.deleteRefreshToken(refreshToken);
//			throw errorUtil.ERROR_DETAILS.get(errorUtil.REFRESH_TOKEN_EXPIRED);
//		}

		var user = userRepository.findByEmail((String) refreshData.get("email")).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
		var newJwtToken = jwtUtil.generateJwtToken(null, null, user);
		jwtUtil.deleteRefreshToken(refreshToken);

		LogUtil.LOGGER.info("returning JWT: " + newJwtToken);
		AuthenicationResponse response = new AuthenicationResponse(newJwtToken);

		HttpHeaders headers = new HttpHeaders();
		return ResponseEntity.ok().headers(headers).body(response);
	}

	@Override
	public ResponseEntity<String> verifyEmail(String token) {
		ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElse(null);

		if (confirmationToken == null) {
			return ResponseEntity.badRequest().body(MessageUtil.getMessage("verify.email.error.invalid.token"));
		} else if (confirmationToken.getConfirmedDate() != null) {
			return ResponseEntity.badRequest().body(MessageUtil.getMessage("verify.email.error.invalid.verified.token"));
		} else if (confirmationToken.getExpiredDate().isBefore(ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime())) {
			return ResponseEntity.badRequest().body(MessageUtil.getMessage("verify.email.error.expired.token"));
		} else {
			confirmationToken.setConfirmedDate(ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime());
			confirmationTokenRepository.save(confirmationToken);
			userRepository.findByEmail(confirmationToken.getUser().getEmail()).ifPresent(user -> {
				user.setIsEnabled(true);
				userRepository.save(user);
			});

			return ResponseEntity.ok().body(MessageUtil.getMessage("verify.email.success"));
		}
	}

	@Override
	public ResponseEntity<StatusResponse> sendVerifyEmail(SendVerifyEmailRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));

		if (user != null) {
			if (user.getIsEnabled()) {
				throw errorUtil.ERROR_DETAILS.get(errorUtil.ACTION_NOT_ALLOWED);
			}

			eventPublisher.publishEvent(new EmailVerificationEvent(this, user));
		} else {
			throw errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND);
		}

		return ResponseEntity.ok().body(new StatusResponse(MessageUtil.getMessage("verify.email.success.sent"), ZonedDateTime.now(ZoneId.of("Z"))));
	}

}
