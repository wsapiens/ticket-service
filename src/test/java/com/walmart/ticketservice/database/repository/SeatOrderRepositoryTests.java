package com.walmart.ticketservice.database.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ticketservice.consts.VenueLevel;
import com.walmart.ticketservice.database.model.Customer;
import com.walmart.ticketservice.database.model.SeatHold;
import com.walmart.ticketservice.database.model.SeatOrder;
import com.walmart.ticketservice.database.model.Venue;

@ContextConfiguration(classes = {RepositoryTestConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@DirtiesContext
public class SeatOrderRepositoryTests {

	@Autowired
	private SeatHoldRepository seatHoldRepository;

	@Autowired
	private SeatOrderRepository seatOrderRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private VenueRepository venueRepository;

	private Integer holdId;

	@Before
	public void setUp() throws Exception {
		Customer customer1 = new Customer("homer@simpson.com");
		customer1 = customerRepository.save(customer1);
		Customer customer2 = new Customer("peter@griffin");
		customer2 = customerRepository.save(customer2);

		LocalDateTime now = LocalDateTime.now();
		Instant nowInstant = now.atZone(ZoneId.systemDefault()).toInstant();

		Venue orchestra = venueRepository.findOne(VenueLevel.ORCHESTRA.getId());
		Venue main = venueRepository.findOne(VenueLevel.MAIN.getId());
		Venue balcony1 = venueRepository.findOne(VenueLevel.BALCONY1.getId());
		
		SeatHold hold1 = new SeatHold();
		hold1.setHoldTime(Date.from(nowInstant));
		hold1.setCustomer(customer1);

		SeatOrder order1 = new SeatOrder(hold1, main, 10);
		SeatOrder order2 = new SeatOrder(hold1, balcony1, 5);
		hold1.getSeatOrders().add(order1);
		hold1.getSeatOrders().add(order2);
		hold1 = seatHoldRepository.save(hold1);
		holdId = hold1.getId();

		SeatHold hold2 = new SeatHold();
		hold2.setHoldTime(Date.from(nowInstant));
		hold2.setCustomer(customer2);

		SeatOrder order3 = new SeatOrder(hold2, orchestra, 20);
		SeatOrder order4 = new SeatOrder(hold2, main, 50);
		hold2.getSeatOrders().add(order3);
		hold2.getSeatOrders().add(order4);
		hold2 = seatHoldRepository.save(hold2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindBySeatHold() {
		SeatHold seatHold = seatHoldRepository.findOne(holdId);
		assertNotNull(seatHold);

		List<SeatOrder> seatOrders = seatOrderRepository.findBySeatHold(seatHold);
		assertNotNull(seatOrders);
		assertTrue(seatOrders.size() == 2);
	}

	@Test
	public void testFindByVenue() {
		Venue main = venueRepository.findOne(VenueLevel.MAIN.getId());

		List<SeatOrder> seatOrders = seatOrderRepository.findByVenue(main);
		assertNotNull(seatOrders);
		assertTrue(seatOrders.size() == 2);

		int numberOfSeatHolded = seatOrders.stream().mapToInt(SeatOrder::getNumberOfSeats).sum();
		assertTrue(numberOfSeatHolded == 60);
		Integer availableSeat = main.getNumberOfRow() * main.getSeatsInRow() - numberOfSeatHolded;
		assertTrue(availableSeat > 0);
	}

}
