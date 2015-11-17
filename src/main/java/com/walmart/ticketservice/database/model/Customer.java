package com.walmart.ticketservice.database.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name ="CUSTOMER")
public class Customer implements Serializable {

	private static final long serialVersionUID = -2087752945780504575L;

	private Long id;
	private String email;
	private String firstName;
	private String lastName;
	private Set<SeatHold> seatHolds = new HashSet<>();

	public Customer() {}

	public Customer(String email) {
		this.email = email;
	}

	@Id
	@Column(name="ID")
	@SequenceGenerator(name="customer_seq", sequenceName="CUSTOMER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="customer_seq")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="EMAIL", nullable=false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="LAST_NAME")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="customer", fetch=FetchType.LAZY)
	public Set<SeatHold> getSeatHolds() {
		return seatHolds;
	}
	public void setSeatHolds(Set<SeatHold> seatHolds) {
		this.seatHolds = seatHolds;
	}

}
