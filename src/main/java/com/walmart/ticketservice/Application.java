package com.walmart.ticketservice;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.walmart.ticketservice.config.ApplicationConfig;

public class Application {

	static Logger log = Logger.getLogger(Application.class);

	public static void main(String [] args) {
		SpringApplication application = new SpringApplication(ApplicationConfig.class);
		@SuppressWarnings("unused")
		ApplicationContext ctx = application.run(args);
	}
}
