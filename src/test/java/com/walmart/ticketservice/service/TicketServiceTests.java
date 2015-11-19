package com.walmart.ticketservice.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.walmart.ticketservice.database.model.SeatHold;
import com.walmart.ticketservice.error.CustomerValidationException;
import com.walmart.ticketservice.error.SeatHoldNotFoundException;

@ContextConfiguration(classes = ServiceTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TicketServiceTests {

	@Autowired
	private TicketService ticketService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNumSeatsAvailable() {
		int numSeatsAvail = ticketService.numSeatsAvailable(Optional.of(1));
		assertTrue(0 <= numSeatsAvail);
	}

	/**
	 * Test with mocked repositories
	 * mocked customer is ready for the email homer@simpson.com
	 */
	@Test
	public void testFindAndHoldSeats() {
		SeatHold seatHold = ticketService.findAndHoldSeats(900, Optional.of(1), Optional.of(3), "homer@simpson.com");
		assertNotNull(seatHold);
		assertTrue(StringUtils.equalsIgnoreCase(seatHold.getCustomer().getEmail(), "homer@simpson.com"));
		assertFalse(seatHold.getSeatOrders().isEmpty());
	}

	/**
	 * Test with giving empty level for min and max both and 
	 * passing new email which is not in customer list
	 */
	@Test
	public void testFindAndHoldSeatsWithNolevels() {
		SeatHold seatHold = ticketService.findAndHoldSeats(1000, Optional.empty(), Optional.empty(), "bart@simpson.com");
		assertNotNull(seatHold);
		assertFalse(seatHold.getSeatOrders().isEmpty());
	}

	/**
	 * Test for checking reserveSeats
	 * Mocked repositories has customer for email homer@simpson.com and SeatHoldId = 11
	 */
	@Test
	public void testReserveSeats() {
		String confirmCode = ticketService.reserveSeats(11, "homer@simpson.com");
		assertFalse(confirmCode.isEmpty());
	}

	/**
	 * Test with passing SeatHoldId which is not existing on database
	 * This is test for simulating the case the SeatHold is expired and deleted already
	 */
	@Test(expected=SeatHoldNotFoundException.class)
	public void testReserveSeatsWithExpiredSeatHoldId() {
		ticketService.reserveSeats(5, "homer@simpson.com");
	}

	/**
	 * This test is for the case where seatHoldId is gone from database
	 * most likely seatHold is already expired and deleted to free hold seats
	 */
	@Test(expected=SeatHoldNotFoundException.class)
	public void testReserveSeatsWithNonExistingSeatHoldId() {
		ticketService.reserveSeats(9, "homer@simpson.com");
	}

	/**
	 * This test is for the case where given wrong email address for the seatHold
	 * It should throw exception for email verification failure
	 */
	@Test(expected=CustomerValidationException.class)
	public void testReserveSeatsWithInvalidEmail() {
		ticketService.reserveSeats(11, "marge@simpson.com");
	}

}
