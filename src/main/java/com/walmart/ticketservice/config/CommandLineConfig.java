package com.walmart.ticketservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(
		basePackages = {"com.walmart.ticketservice"}, 
		excludeFilters = @ComponentScan.Filter(value = {ApplicationConfig.class}, 
														type = FilterType.ASSIGNABLE_TYPE))
@PropertySource("classpath:application.properties")
public class CommandLineConfig {

	@Value("${seat.hold.expire.second:120}")
	private String seatHoldExpireTime;

	@Bean
	public ServiceProperties serviceProperties() {
		return new ServiceProperties(Long.valueOf(seatHoldExpireTime));
	}
}
