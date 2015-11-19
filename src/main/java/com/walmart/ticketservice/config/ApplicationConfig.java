package com.walmart.ticketservice.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.walmart.ticketservice.error.AsyncExceptionHandler;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
@ComponentScan("com.walmart.ticketservice")
public class ApplicationConfig extends SpringBootServletInitializer implements AsyncConfigurer {

	@Value("${seat.hold.expire.second:120}")
	private String seatHoldExpireTime;

	@Bean
	public ServiceProperties serviceProperties() {
		return new ServiceProperties(Long.valueOf(seatHoldExpireTime));
	}

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(100);
		executor.setQueueCapacity(200);
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExceptionHandler();
	}

}
