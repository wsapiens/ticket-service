package com.walmart.ticketservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.walmart.ticketservice.database.model.SeatOrder;
import com.walmart.ticketservice.database.model.SeatHold;
import java.util.List;
import com.walmart.ticketservice.database.model.Venue;

@Repository
public interface SeatOrderRepository extends JpaRepository<SeatOrder, Long> {
	List<SeatOrder> findBySeatHold(SeatHold seathold);
	List<SeatOrder> findByVenue(Venue venue);
}
