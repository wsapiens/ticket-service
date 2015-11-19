package com.walmart.ticketservice.domain;

import java.util.ArrayList;
import java.util.List;

public class SeatHoldResult {
	private Integer holdId;
	private String customerEmail;
	private List<SeatHoldDetail> details = new ArrayList<>();

	public SeatHoldResult() {}

	public SeatHoldResult(Integer holdId, String customerEmail) {
		this.holdId = holdId;
		this.customerEmail = customerEmail;
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
	public List<SeatHoldDetail> getDetails() {
		return details;
	}
}
