package com.walmart.ticketservice.domain;

public class AvailableSeatsResult {
	private Integer venueLevel;
	private Integer numberOfAvailableSeats;

	public AvailableSeatsResult() {}

	public AvailableSeatsResult(Integer venueLevel, Integer numberOfAvailableSeats) {
		this.venueLevel = venueLevel;
		this.numberOfAvailableSeats = numberOfAvailableSeats;
	}

	public Integer getVenueLevel() {
		return venueLevel;
	}
	public void setVenueLevel(Integer venueLevel) {
		this.venueLevel = venueLevel;
	}
	public Integer getNumberOfAvailableSeats() {
		return numberOfAvailableSeats;
	}
	public void setNumberOfAvailableSeats(Integer numberOfAvailableSeats) {
		this.numberOfAvailableSeats = numberOfAvailableSeats;
	}
}
