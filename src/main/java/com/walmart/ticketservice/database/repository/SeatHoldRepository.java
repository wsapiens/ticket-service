package com.walmart.ticketservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.walmart.ticketservice.database.model.SeatHold;
import java.util.Date;
import java.util.List;
import com.walmart.ticketservice.database.model.Customer;

@Repository
public interface SeatHoldRepository extends JpaRepository<SeatHold, Integer> {
	List<SeatHold> findByConfirmationCodeIsNullAndHoldTimeBefore(Date date);
	List<SeatHold> findByCustomer(Customer customer);
}
