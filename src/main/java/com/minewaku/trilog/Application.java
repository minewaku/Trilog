package com.minewaku.trilog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableMethodSecurity(securedEnabled = true)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	/* LOVELY NOTE */
	
	//add more variant of dto like return user, save user, update user,...
	//add checking if role is already assigned to any user before deleting
	//check valid phone because the jakarta validation doesn't support it
	//add view, comment counting logic
	//improve like, view, comment counting logic
	//

}
