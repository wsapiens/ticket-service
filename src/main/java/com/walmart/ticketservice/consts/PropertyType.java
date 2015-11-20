package com.walmart.ticketservice.consts;

public enum PropertyType {

	ACTION("action"),
	LEVEL("level"),
	MIN_LEVEL("minLevel"),
	MAX_LEVEL("maxLevel"),
	HOLD_ID("holdId"),
	NUM_SEATS("numSeats"),
	EMAIL("email");

	private final String name;

	private PropertyType(String name) {
		this.name = name;
	}

	public boolean equalsName(String otherName){
		return (otherName == null) ? false : name.equalsIgnoreCase(otherName);
	}

	public String toString() {
		return name;
	}
}
