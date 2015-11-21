package com.walmart.ticketservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.walmart.ticketservice.database.model.SeatHold;
import com.walmart.ticketservice.domain.AvailableSeatsResult;
import com.walmart.ticketservice.domain.ReservationResult;
import com.walmart.ticketservice.domain.SeatHoldDetail;
import com.walmart.ticketservice.domain.SeatHoldResult;
import com.walmart.ticketservice.error.CustomerValidationException;
import com.walmart.ticketservice.error.SeatHoldNotFoundException;
import com.walmart.ticketservice.service.TicketService;

/**
 * Controller for TicketService version 1
 * @author spark
 *
 */
@RestController
@RequestMapping(value = "/ticket-service/v1")
public class ServiceController {

	@Autowired
	private TicketService ticketService;

	@RequestMapping(value="/available-seats/venue", method=RequestMethod.GET)
	public DeferredResult<AvailableSeatsResult> getAvailableSeats(@RequestParam(value="level", required=false) Integer level) {
		DeferredResult<AvailableSeatsResult> result = new DeferredResult<>();
		Optional<Integer> venueLevel = (level != null) ? Optional.of(level) : Optional.empty();
		int numOfAvailSeats = ticketService.numSeatsAvailable(venueLevel);
		result.setResult( new AvailableSeatsResult(level, numOfAvailSeats) );
		return result;
	}

	@RequestMapping(value="/hold/num-seats/{numSeats}/email/{email}/venue", method=RequestMethod.POST)
	public DeferredResult<SeatHoldResult> findAndHoldSeats(@PathVariable Integer numSeats,
													@PathVariable String email,
													@RequestParam(value="minLevel", required=false) Integer min,
													@RequestParam(value="maxLevel", required=false) Integer max) {
		DeferredResult<SeatHoldResult> result = new DeferredResult<>();
		SeatHoldResult seatHoldResult = null;
		Optional<Integer> minLevel = (min != null) ? Optional.of(min) : Optional.empty();
		Optional<Integer> maxLevel = (max != null) ? Optional.of(max) : Optional.empty();
		SeatHold seatHold = ticketService.findAndHoldSeats(numSeats, minLevel, maxLevel, email);
		if(null != seatHold) {
			seatHoldResult = new SeatHoldResult(seatHold.getId(), seatHold.getCustomer().getEmail());
			List<SeatHoldDetail> details = seatHold.getSeatOrders()
													.stream()
													.map(order -> new SeatHoldDetail(order.getVenue().getLevelId(), order.getNumberOfSeats()) )
													.collect(Collectors.toList());
			if(!details.isEmpty()) {
				seatHoldResult.getDetails().addAll(details);
			}
		}
		result.setResult( seatHoldResult );
		return result;
	}

	
	@RequestMapping(value="/hold/{id}/email/{email}/reserve", method=RequestMethod.POST)
	public DeferredResult<ReservationResult> reserveSeats(@PathVariable Integer id, @PathVariable String email) {
		DeferredResult<ReservationResult> result = new DeferredResult<>();
		result.setResult( new ReservationResult(id, email, ticketService.reserveSeats(id, email)) );
		return result;
	}

	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="no such hold")
	@ExceptionHandler(SeatHoldNotFoundException.class)
	public SeatHoldNotFoundException handleSeatHoldNotFoundException(SeatHoldNotFoundException ex, HttpServletRequest request) {
		return ex;
	}

	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="email is not matching")
	@ExceptionHandler(CustomerValidationException.class)
	public CustomerValidationException handleCustomerValidationException(CustomerValidationException ex, HttpServletRequest request) {
		return ex;
	}
}
