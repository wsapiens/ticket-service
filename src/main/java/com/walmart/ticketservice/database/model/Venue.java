package com.walmart.ticketservice.database.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name ="VENUE")
public class Venue implements Serializable {

	private static final long serialVersionUID = 2703093699428542040L;

	private Integer levelId;
	private String levelName;
	private BigDecimal price;
	private Integer numberOfRow;
	private Integer seatsInRow;
	private Set<SeatOrder> seatOrders = new HashSet<>();

	public Venue() {}

	public Venue(Integer levelId, String levelName, BigDecimal price, Integer numberOfRow, Integer seatsInRow) {
		this.levelId = levelId;
		this.levelName = levelName;
		this.price = price;
		this.numberOfRow = numberOfRow;
		this.seatsInRow = seatsInRow;
	}

	@Id
	@Column(name="LEVEL_ID")
	public Integer getLevelId() {
		return levelId;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	@Column(name="LEVEL_NAME", nullable=false)
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	@Column(name="PRICE", nullable=false)
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name="NUMBER_OF_ROW", nullable=false)
	public Integer getNumberOfRow() {
		return numberOfRow;
	}
	public void setNumberOfRow(Integer numberOfRow) {
		this.numberOfRow = numberOfRow;
	}

	@Column(name="SEATS_IN_ROW", nullable=false)
	public Integer getSeatsInRow() {
		return seatsInRow;
	}
	public void setSeatsInRow(Integer seatsInRow) {
		this.seatsInRow = seatsInRow;
	}

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="venue", fetch=FetchType.LAZY)
	public Set<SeatOrder> getSeatOrders() {
		return seatOrders;
	}
	public void setSeatOrders(Set<SeatOrder> seatOrders) {
		this.seatOrders = seatOrders;
	}
}
