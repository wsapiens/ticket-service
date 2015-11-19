package com.walmart.ticketservice.domain;

public class SeatHoldDetail {
	private Integer venueLevel;
	private Integer numOfSeats;

	public SeatHoldDetail(){}

	public SeatHoldDetail(Integer venueLevel, Integer numOfSeats) {
		this.venueLevel = venueLevel;
		this.numOfSeats = numOfSeats;
	}

	public Integer getVenueLevel() {
		return venueLevel;
	}
	public void setVenueLevel(Integer venueLevel) {
		this.venueLevel = venueLevel;
	}
	public Integer getNumOfSeats() {
		return numOfSeats;
	}
	public void setNumOfSeats(Integer numOfSeats) {
		this.numOfSeats = numOfSeats;
	}
}
