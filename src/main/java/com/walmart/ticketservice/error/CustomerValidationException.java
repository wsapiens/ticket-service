package com.walmart.ticketservice.error;

public class CustomerValidationException extends RuntimeException {

	private static final long serialVersionUID = -591087136973384382L;

	public CustomerValidationException() { super(); }
	public CustomerValidationException(String s) { super(s); }
}
