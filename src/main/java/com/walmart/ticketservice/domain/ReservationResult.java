package com.walmart.ticketservice.domain;

public class ReservationResult {
	private Integer holdId;
	private String customerEmail;
	private String confirmationCode;

	public ReservationResult() {}

	public ReservationResult(Integer holdId, String customerEmail, String confirmationCode) {
		this.holdId = holdId;
		this.customerEmail = customerEmail;
		this.confirmationCode = confirmationCode;
	}

	public Integer getHoldId() {
		return holdId;
	}

	public void setHoldId(Integer holdId) {
		this.holdId = holdId;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

}
