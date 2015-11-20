package com.walmart.ticketservice.database.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SEAT_HOLD")
public class SeatHold implements Serializable {

	private static final long serialVersionUID = -655586672728569618L;

	private Integer id;
	/** timestamp when seatHold made */
	private Date holdTime;
	private Customer customer;
	private String confirmationCode;
	private Date reservationTime;
	private Set<SeatOrder> seatOrders = new HashSet<>();

	@Id
	@Column(name="ID")
	@SequenceGenerator(name="hold_seq", sequenceName="SEAT_HOLD_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="hold_seq")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="HOLD_TIME", insertable=true, updatable=false)
	public Date getHoldTime() {
		return holdTime;
	}
	public void setHoldTime(Date holdTime) {
		this.holdTime = holdTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_ID", nullable=false)
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name="RESERVATION_CONFIRMATION_CODE")
	public String getConfirmationCode() {
		return confirmationCode;
	}
	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	@Column(name="RESERVATION_TIME")
	public Date getReservationTime() {
		return reservationTime;
	}
	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
	}

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="seatHold", fetch=FetchType.LAZY)
	public Set<SeatOrder> getSeatOrders() {
		return seatOrders;
	}
	public void setSeatOrders(Set<SeatOrder> seatOrders) {
		this.seatOrders = seatOrders;
	}

}
