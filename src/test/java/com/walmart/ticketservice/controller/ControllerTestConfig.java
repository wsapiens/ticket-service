package com.walmart.ticketservice.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.walmart.ticketservice.database.model.Customer;
import com.walmart.ticketservice.database.model.SeatHold;
import com.walmart.ticketservice.database.model.SeatOrder;
import com.walmart.ticketservice.database.model.Venue;
import com.walmart.ticketservice.error.CustomerValidationException;
import com.walmart.ticketservice.error.SeatHoldNotFoundException;
import com.walmart.ticketservice.service.TicketService;

@Configuration
public class ControllerTestConfig {

	@SuppressWarnings("unchecked")
	@Bean
	public TicketService ticketService() {
		TicketService service = mock(TicketService.class);
		SeatHold seatHold = new SeatHold();
		seatHold.setId(150);
		seatHold.setCustomer(new Customer("test@email.com"));
		seatHold.getSeatOrders().add(new SeatOrder(seatHold, new Venue(2, "levelName", BigDecimal.valueOf(70.00d), 20, 25), 50));
		when(service.findAndHoldSeats(any(Integer.class), any(Optional.class), any(Optional.class), any(String.class))).thenReturn(seatHold);
		when(service.reserveSeats(150, "test@email.com")).thenReturn("confirm-code");
		when(service.reserveSeats(40, "test@email.com")).thenThrow(SeatHoldNotFoundException.class);			// simulate no seatHold found by id = 40
		when(service.reserveSeats(150, "not.existing@email.com")).thenThrow(CustomerValidationException.class); // simulate customer email not exist
		return service;
	}

	@Bean
	public ServiceController serviceController() {
		return new ServiceController();
	}
}
