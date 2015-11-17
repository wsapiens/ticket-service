package com.walmart.ticketservice.consts;

public class ServiceConsts {
	/** running application via command line for test*/
	public static final String MODE_TEST = "test";
	public static final String CODE_GENERATOR_SEED_PREFIX = "TicketService";
	/** after this number of seconds, seat hold will be expired, it should be at least 60 or more */
	public static final Long SEAT_HOLD_EXPIRATION_TIME_IN_SECONDS	= 120L;		// 2 minute
	/** cron expression (every minute) for scheduler to clean up expired seatHold */
	public static final String SCHEDULER_TRIGGER_CRON = "0 */1 * * * *";

	public static final String EMPTY_STRING = "";
	public static final String CODE_DELIMITER = ":";
}
