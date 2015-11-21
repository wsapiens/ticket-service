package com.walmart.ticketservice.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ticketservice.config.ServiceProperties;
import com.walmart.ticketservice.consts.VenueLevel;
import com.walmart.ticketservice.database.model.Customer;
import com.walmart.ticketservice.database.model.SeatHold;
import com.walmart.ticketservice.database.model.SeatOrder;
import com.walmart.ticketservice.database.model.Venue;
import com.walmart.ticketservice.database.repository.CustomerRepository;
import com.walmart.ticketservice.database.repository.SeatHoldRepository;
import com.walmart.ticketservice.database.repository.SeatOrderRepository;
import com.walmart.ticketservice.database.repository.VenueRepository;
import com.walmart.ticketservice.error.CustomerValidationException;
import com.walmart.ticketservice.error.SeatHoldNotFoundException;
import com.walmart.ticketservice.util.ConfirmationCodeUtils;

/**
 * class for TicketService implementation
 * take Serializable isolation level for ultimate concurrency safety
 * but performance will be downgraded
 * 
 * @author spark
 *
 */
@Service("ticketService")
@Transactional(value="transactionManager", isolation= Isolation.SERIALIZABLE)
public class TicketServiceImpl implements TicketService {

	private static Logger log = Logger.getLogger(TicketServiceImpl.class);

	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private SeatHoldRepository seatHoldRepository;

	@Autowired
	private SeatOrderRepository seatOrderRepository;

	@Autowired
	private ServiceProperties serviceProperties;

	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		// clean up expired hold
		removeExpiredSeatHold();

		if(venueLevel.isPresent()) {
			Venue venue = venueRepository.findOne(venueLevel.get());
			if(null != venue) {
				return getAvailableSeatsInVenueLevel(venue);
			}
		} else {
			// If no venueLevel is given, search total available seat through whole levels
			List<Venue> venues = venueRepository.findAll();
			return venues.stream().mapToInt(venue -> getAvailableSeatsInVenueLevel(venue)).sum();
		}
		return 0;
	}

	/**
	 * Get available seat for given venue level
	 * @param venue VenueLevel entity
	 * @return number of available seats
	 */
	private int getAvailableSeatsInVenueLevel(Venue venue) {
		List<SeatOrder> seatOrders = seatOrderRepository.findByVenue(venue);
		int numberOfSeatTaken = seatOrders.stream().mapToInt(SeatOrder::getNumberOfSeats).sum();
		int totalNumberOfSeat = venue.getNumberOfRow() * venue.getSeatsInRow();
		return totalNumberOfSeat - numberOfSeatTaken;
	}

	/**
	 * Clean up expired SeatHold
	 */
	private void removeExpiredSeatHold() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expired = now.minusSeconds(serviceProperties.getSeatHoldExpireTime());
		Instant expiredInstant = expired.atZone(ZoneId.systemDefault()).toInstant();

		// Clean up expired holds
		List<SeatHold> expiredHolds = seatHoldRepository.findByConfirmationCodeIsNullAndHoldTimeBefore(Date.from(expiredInstant));
		if(!expiredHolds.isEmpty()) {
			seatHoldRepository.delete(expiredHolds);
		}
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {
		if(StringUtils.isEmpty(customerEmail)) {
			log.warn("Get empty email, so ignore hold request");
			throw new CustomerValidationException();
		}

		// Retreive venue and customer info
		List<VenueLevel> venueLevels = VenueLevel.getLevels(minLevel, maxLevel);
		Customer customer = customerRepository.findByEmail(customerEmail);
		if(null == customer) {
			log.info(String.join(": ", "create new customer by email", customerEmail));
			customer = customerRepository.save(new Customer(customerEmail));
		} else {
			log.info(String.join(": ", "found existing customer by email", customerEmail));
		}
		SeatHold seatHold = new SeatHold();
		seatHold.setCustomer(customer);

		// Find seats through venue levels
		int numSeatsToHold = numSeats;
		for(VenueLevel venueLevel: venueLevels) {
			if(0 < numSeatsToHold) {
				Venue venue = venueRepository.findOne(venueLevel.getId());
				int numSeatsAvail = numSeatsAvailable(Optional.of(venueLevel.getId()));
				if( 0 < numSeatsAvail ) {
					if(numSeatsAvail >= numSeatsToHold) {	// more seat available
						seatHold.getSeatOrders().add( new SeatOrder(seatHold, venue, numSeatsToHold) );
						numSeatsToHold = 0;
						break;
					} else {							// not enough seat available in this level
						seatHold.getSeatOrders().add( new SeatOrder(seatHold, venue, numSeatsAvail) );
						numSeatsToHold = numSeatsToHold - numSeatsAvail;
					}
				}
			}
		}

		// persist the hold
		if( seatHold.getSeatOrders().isEmpty() ) {
			log.warn("fail to hold any seat in levels for customer email: ".concat(customerEmail));
		} else {
			seatHold.setHoldTime(new Date());
			seatHold = seatHoldRepository.save(seatHold);
			String message = new StringBuilder("hold ")
					.append(numSeats - numSeatsToHold)
					.append(" seats for email: ")
					.append(customerEmail)
					.toString();
			log.info(message);
		}
		return seatHold;
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expired = now.minusSeconds(serviceProperties.getSeatHoldExpireTime());
		Instant expiredInstant = expired.atZone(ZoneId.systemDefault()).toInstant();

		// find SeatHold
		SeatHold seatHold = seatHoldRepository.findOne(seatHoldId);
		if(null == seatHold) {
			String errorMessage = String.join(": ", "fail on reservation, no SeatHold found. it probably already expired, seatHoldId", String.valueOf(seatHoldId));
			log.error(errorMessage);
			throw new SeatHoldNotFoundException(errorMessage);
		}

		// verify customer info first
		Customer customer = seatHold.getCustomer();
		if(null == customer || !StringUtils.equalsIgnoreCase(customer.getEmail(), customerEmail)) {
			StringBuilder errorMessage = new StringBuilder("Customer Eamil Validation on SeatHold fail, seatHoldId: ")
					.append(seatHoldId)
					.append(", customerEmail: ")
					.append(customerEmail);
			log.error(errorMessage.toString());
			throw new CustomerValidationException(errorMessage.toString());
		}

		// verify seatHold and reservation status
		if( StringUtils.isEmpty(seatHold.getConfirmationCode()) ) {
			if(seatHold.getHoldTime().before(Date.from(expiredInstant))) {
				String errorMessage = String.join(": ", "fail on reservation, the SeatHold is expired, seatHoldId", String.valueOf(seatHoldId));
				log.error(errorMessage);
				throw new SeatHoldNotFoundException(errorMessage);
			}
		} else {
			String message = new StringBuilder("The seatHold is already reservated, seatHoldId: ")
					.append(seatHoldId)
					.append(", customerEmail: ")
					.append(customerEmail)
					.toString();
			log.warn(message);
			return seatHold.getConfirmationCode();
		}

		// generate confirmation code and check in the hold as reservation
		String code = ConfirmationCodeUtils.generateCode(seatHoldId, customerEmail);
		seatHold.setConfirmationCode(code);
		seatHold.setReservationTime(new Date());
		seatHoldRepository.save(seatHold);
		String message = new StringBuilder("Reserved Seat for email: ")
				.append(customerEmail)
				.append(", seatHoldId: ")
				.append(seatHoldId)
				.append(", confirmationCode: ")
				.append(code)
				.toString();
		log.info(message);
		return code;
	}

}
