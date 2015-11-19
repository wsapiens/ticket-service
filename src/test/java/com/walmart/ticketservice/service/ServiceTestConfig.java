package com.walmart.ticketservice.service;

import static com.walmart.ticketservice.consts.ServiceConsts.SEAT_HOLD_EXPIRATION_TIME_IN_SECONDS;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.walmart.ticketservice.config.ServiceProperties;
import com.walmart.ticketservice.database.model.Customer;
import com.walmart.ticketservice.database.model.SeatHold;
import com.walmart.ticketservice.database.model.SeatOrder;
import com.walmart.ticketservice.database.model.Venue;
import com.walmart.ticketservice.database.repository.CustomerRepository;
import com.walmart.ticketservice.database.repository.SeatHoldRepository;
import com.walmart.ticketservice.database.repository.SeatOrderRepository;
import com.walmart.ticketservice.database.repository.VenueRepository;

@Configuration
public class ServiceTestConfig {

	@Bean
	public ServiceProperties serviceProperties() {
		return new ServiceProperties(Long.valueOf("120"));
	}

	@Bean
	public VenueRepository venueRepository() {
		VenueRepository repository = mock(VenueRepository.class);
		when(repository.findOne(1)).thenReturn(new Venue(1, "Orchestra", BigDecimal.valueOf(100.00d), 25, 50));
		when(repository.findOne(2)).thenReturn(new Venue(2, "Main", BigDecimal.valueOf(75.00d), 20, 100));
		when(repository.findOne(3)).thenReturn(new Venue(3, "Balcony 1", BigDecimal.valueOf(50.00d), 15, 100));
		when(repository.findOne(4)).thenReturn(new Venue(4, "Balcony 2", BigDecimal.valueOf(40.00d), 15, 100));
		return repository;
	}

	@Bean
	public CustomerRepository customerRepository() {
		CustomerRepository repository = mock(CustomerRepository.class);
		when(repository.findByEmail("homer@simpson.com")).thenReturn(new Customer("homer@simpson.com"));
		when(repository.save(any(Customer.class))).thenReturn(new Customer("bart@simpson.com"));
		return repository;
	}

	@Bean
	public SeatHoldRepository seatHoldRepository() {
		LocalDateTime now = LocalDateTime.now();
		Instant nowInstant = now.atZone(ZoneId.systemDefault()).toInstant();
		LocalDateTime expired = now.minusSeconds(SEAT_HOLD_EXPIRATION_TIME_IN_SECONDS + 100L);
		Instant expiredInstant = expired.atZone(ZoneId.systemDefault()).toInstant();

		SeatHoldRepository repository = mock(SeatHoldRepository.class);
		SeatHold seatHold = new SeatHold();
		seatHold.setId(11);
		seatHold.setCustomer(new Customer("homer@simpson.com"));
		seatHold.getSeatOrders().add(new SeatOrder());
		seatHold.setHoldTime(Date.from(nowInstant));

		SeatHold expiredSeatHold = new SeatHold();
		expiredSeatHold.setId(5);
		expiredSeatHold.setCustomer(new Customer("homer@simpson.com"));
		expiredSeatHold.setHoldTime(Date.from(expiredInstant));

		when(repository.save(any(SeatHold.class))).thenReturn(seatHold);
		when(repository.findOne(11)).thenReturn(seatHold);
		when(repository.findOne(5)).thenReturn(expiredSeatHold);

		List<SeatHold> expiredSeatHolds = new ArrayList<>();
		expiredSeatHolds.add(expiredSeatHold);
		when(repository.findByConfirmationCodeIsNullAndHoldTimeBefore(any(Date.class))).thenReturn(expiredSeatHolds);
		return repository;
	}

	@Bean
	public SeatOrderRepository seatOrderRepository() {
		SeatOrderRepository repository = mock(SeatOrderRepository.class);
		List<SeatOrder> seatOrders = new ArrayList<>();
		SeatOrder seatOrder1 = new SeatOrder(new SeatHold(), new Venue(), 1000);
		SeatOrder seatOrder2 = new SeatOrder(new SeatHold(), new Venue(), 200);
		seatOrders.add(seatOrder1);
		seatOrders.add(seatOrder2);
		when(repository.findByVenue(any(Venue.class))).thenReturn(seatOrders);
		return repository;
	}

	@Bean
	public TicketService ticketService() {
		return new TicketServiceImpl();
	}
}
