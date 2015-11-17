package com.walmart.ticketservice.database.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 * SeatOrder entity is for detail order of each venue and number of seats
 * 
 * @author spark
 *
 */
@Entity
@Table(name = "SEAT_ORDER")
public class SeatOrder implements Serializable {

	private static final long serialVersionUID = -6624470455179014870L;

	private Long id;
	private SeatHold seatHold;
	private Venue venue;
	private Integer numberOfSeats;

	public SeatOrder() {}

	public SeatOrder(SeatHold seatHold, Venue venue, Integer numberOfSeats) {
		this.seatHold = seatHold;
		this.venue = venue;
		this.numberOfSeats = numberOfSeats;
	}

	@Id
	@Column(name="ID")
	@SequenceGenerator(name="order_seq", sequenceName="SEAT_ORDER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="order_seq")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOLD_ID", nullable=false)
	public SeatHold getSeatHold() {
		return seatHold;
	}
	public void setSeatHold(SeatHold seatHold) {
		this.seatHold = seatHold;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="VENUE_ID", nullable=false)
	public Venue getVenue() {
		return venue;
	}
	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	@Column(name="NUMBER_OF_SEATS", nullable=false)
	public Integer getNumberOfSeats() {
		return numberOfSeats;
	}
	public void setNumberOfSeats(Integer numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

}
