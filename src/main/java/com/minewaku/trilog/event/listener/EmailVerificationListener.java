package com.minewaku.trilog.event.listener;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.minewaku.trilog.entity.ConfirmationToken;
import com.minewaku.trilog.event.EmailVerificationEvent;
import com.minewaku.trilog.repository.ConfirmationTokenRepository;
import com.minewaku.trilog.service.impl.EmailService;

@Component
public class EmailVerificationListener implements ApplicationListener<EmailVerificationEvent> {
	
	private int TOKEN_EXPIRATION_DURATION = 5;
	private TimeUnit TOKEN_EXPIRATION_UNIT = TimeUnit.MINUTES;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Value("${app.context.path}")
	private String contextPath;

	@Override
	public void onApplicationEvent(EmailVerificationEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(EmailVerificationEvent event) {
		String verifyToken = UUID.randomUUID().toString();
		String verifyUrl = contextPath + "/api/v1/auth/verify?token=" + verifyToken;
		
		ConfirmationToken confirmationToken = ConfirmationToken.builder()
			    .user(event.getUser())
			    .token(verifyToken)
			    .expiredDate(ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime().plus(TOKEN_EXPIRATION_DURATION, ChronoUnit.MINUTES))
			    .build();
		confirmationTokenRepository.save(confirmationToken);

		// Prepare Thymeleaf context
		Context context = new Context();
		context.setVariable("verifyUrl", verifyUrl);

		// Send the email
		emailService.sendEmail(event.getUser().getEmail(), "Trilog Email Verification", "verifyEmailTemplate", context);
	}
}
