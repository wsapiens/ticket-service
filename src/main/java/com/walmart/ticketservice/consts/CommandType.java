package com.walmart.ticketservice.consts;

public enum CommandType {

	SEARCH("search"),
	HOLD("hold"),
	RESERVE("reserve"),
	RESET("reset");

	private final String name;

	private CommandType(String name) {
		this.name = name;
	}

	public boolean equalsName(String otherName){
		return (otherName == null) ? false : name.equalsIgnoreCase(otherName);
	}

	public String toString() {
		return name;
	}
}
