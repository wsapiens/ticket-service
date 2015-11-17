package com.walmart.ticketservice.service;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ticketservice.database.model.Customer;
import com.walmart.ticketservice.database.model.SeatHold;
import com.walmart.ticketservice.database.model.SeatOrder;
import com.walmart.ticketservice.database.model.Venue;
import com.walmart.ticketservice.database.repository.SeatHoldRepository;
import com.walmart.ticketservice.database.repository.SeatOrderRepository;
import com.walmart.ticketservice.database.repository.VenueRepository;
import com.walmart.ticketservice.error.TicketServiceException;
import com.walmart.ticketservice.util.ConfirmationCodeUtils;

@Transactional(value="transactionManager", isolation= Isolation.REPEATABLE_READ)
public class TicketServiceImpl implements TicketService {

	private static Logger log = Logger.getLogger(TicketServiceImpl.class);

	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private SeatHoldRepository seatHoldRepository;

	@Autowired
	private SeatOrderRepository seatOrderRepository;

	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		if(venueLevel.isPresent()) {
			Venue venue = venueRepository.findOne(venueLevel.get());
			if(null != venue) {
				// find any reserved or holded seat for given level
				List<SeatOrder> seatOrders = seatOrderRepository.findByVenue(venue);
				int numberOfSeatTaken = seatOrders.stream().mapToInt(SeatOrder::getNumberOfSeats).sum();
				int totalNumberOfSeat = venue.getNumberOfRow() * venue.getSeatsInRow();
				return totalNumberOfSeat - numberOfSeatTaken;
			}
		}
		return 0;
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		SeatHold seatHold = seatHoldRepository.findOne(seatHoldId);
		if(null == seatHold) {
			log.info(String.join(": ", "No SeatHold found. it must be expired, seatHoldId", String.valueOf(seatHoldId)) );
			return null;
		}
		Customer customer = seatHold.getCustomer();
		if(null == customer || !customer.getEmail().equalsIgnoreCase(customerEmail)) {
			StringBuilder errorMessage = new StringBuilder("Customer Validation on SeatHold fail, seatHoldId: ")
												.append(seatHoldId)
												.append(", customerEmail: ")
												.append(customerEmail);
			throw new TicketServiceException(errorMessage.toString());
		}

		String code = ConfirmationCodeUtils.generateCode(seatHoldId, customerEmail);
		seatHold.setConfirmationCode(code);
		seatHoldRepository.save(seatHold);
		return code;
	}

}
