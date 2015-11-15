package com.walmart.ticketservice.consts;

public enum VenueLevel {
	ORCHESTRA("Orchestra", 1),
	MAIN("Main", 2),
	BALCONY1("Balcony 1", 3),
	BALCONY2("Balcony 2", 4);

	private final Integer id;
	private final String name;

	private VenueLevel(String name, Integer id) {
		this.name = name;
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public boolean equalsName(String otherName){
		return (otherName == null) ? false : name.equalsIgnoreCase(otherName);
	}

	public String toString() {
		return name;
	}

}
