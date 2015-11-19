package com.walmart.ticketservice.config;

/**
 * Class for having global variables for service
 * 
 * @author spark
 *
 */
public final class ServiceProperties {
	private final Long seatHoldExpireTime;	// default

	public ServiceProperties(Long seatHoldExpireTime) {
		this.seatHoldExpireTime = seatHoldExpireTime;
	}

	public final Long getSeatHoldExpireTime() {
		return seatHoldExpireTime;
	}

}
