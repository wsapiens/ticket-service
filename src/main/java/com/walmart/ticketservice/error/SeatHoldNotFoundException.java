package com.walmart.ticketservice.error;

public class SeatHoldNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 910335327840813620L;

	public SeatHoldNotFoundException() { super(); }
	public SeatHoldNotFoundException(String s) { super(s); }
}
