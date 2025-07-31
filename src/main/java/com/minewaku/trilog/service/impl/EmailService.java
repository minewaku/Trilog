package com.minewaku.trilog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.minewaku.trilog.service.IEmailService;
import com.minewaku.trilog.util.ErrorUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService implements IEmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private ErrorUtil errorUtil;

	@Override
	public void sendEmail(String to, String subject, String templateName, Context context) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			String emailContent = templateEngine.process(templateName, context);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(emailContent, true);

			mailSender.send(message);
		} catch (MessagingException e) {
			throw errorUtil.ERROR_DETAILS.get(errorUtil.UNABLE_SEND_EMAIL_VERIFICATION);
		}
	}
}