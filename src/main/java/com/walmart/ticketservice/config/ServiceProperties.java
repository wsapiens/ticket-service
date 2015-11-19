package com.walmart.ticketservice.config;

import org.springframework.util.StringUtils;

/**
 * Class for having global variables for service
 * 
 * @author spark
 *
 */
public class ServiceProperties {
	private Long seatHoldExpireTime = 120L;	// default

	public final Long getSeatHoldExpireTime() {
		return seatHoldExpireTime;
	}

	public void setSeatHoldExpireTime(String expireTimeString) {
		if( !StringUtils.isEmpty(expireTimeString) ) {
			this.seatHoldExpireTime = Long.valueOf(expireTimeString);
		}
	}
}
