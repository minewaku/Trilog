package com.minewaku.trilog.service;

import org.thymeleaf.context.Context;

public interface IEmailService {
	void sendEmail(String to, String subject, String templateName, Context context);
}
