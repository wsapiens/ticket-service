package com.walmart.ticketservice.error;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	private static Logger log = Logger.getLogger(AsyncExceptionHandler.class);

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		log.error("Exception message - " + ex.getMessage());
		log.error("Method name - " + method.getName());
		for (Object param : params) {
			log.error("Parameter value - " + param);
		}
	}

}
