package com.walmart.ticketservice.error;

public class TicketServiceException extends RuntimeException {

	private static final long serialVersionUID = 6170439427812189115L;

	public TicketServiceException() { super(); }
	public TicketServiceException(String s) { super(s); }
	public TicketServiceException(String s, Throwable throwable) { super(s, throwable); }
	public TicketServiceException(Throwable throwable) { super(throwable); }
}
