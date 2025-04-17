package com.minewaku.trilog.util;

import java.util.ResourceBundle;

public class MessageUtil {

	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");

	public static String getMessage(String key) {
		return MESSAGES.getString(key);
	}
}