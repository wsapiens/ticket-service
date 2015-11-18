package com.walmart.ticketservice.database.repository;

import static com.walmart.ticketservice.consts.ServiceConsts.SEAT_HOLD_EXPIRATION_TIME_IN_SECONDS;
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

import com.walmart.ticketservice.database.model.Customer;
import com.walmart.ticketservice.database.model.SeatHold;

@ContextConfiguration(classes = {RepositoryTestConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@DirtiesContext
public class SeatHoldRepositoryTests {

	@Autowired
	private SeatHoldRepository seatHoldRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Before
	public void setUp() throws Exception {
		Customer customer1 = new Customer("homer@simpson.com");
		customer1 = customerRepository.save(customer1);
		Customer customer2 = new Customer("peter@griffin");
		customer2 = customerRepository.save(customer2);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oldDate = now.minusSeconds(SEAT_HOLD_EXPIRATION_TIME_IN_SECONDS + 60L);
		Instant oldInstant = oldDate.atZone(ZoneId.systemDefault()).toInstant();
		Instant nowInstant = now.atZone(ZoneId.systemDefault()).toInstant();

		// expired
		SeatHold hold1 = new SeatHold();
		hold1.setHoldTime(Date.from(oldInstant));
		hold1.setCustomer(customer1);
		hold1 = seatHoldRepository.save(hold1);

		// not yet expired
		SeatHold hold2 = new SeatHold();
		hold2.setHoldTime(Date.from(nowInstant));
		hold2.setCustomer(customer2);
		hold2 = seatHoldRepository.save(hold2);

		// promoted to reservation, since it has confirmationCode
		SeatHold hold3 = new SeatHold();
		hold3.setHoldTime(Date.from(oldInstant));
		hold3.setConfirmationCode("confirmationCode");
		hold3.setCustomer(customer1);
		hold3 = seatHoldRepository.save(hold3);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * find expired seatHold which has null for confirmationCode and holdTime is older than expirationTime
	 */
	@Test
	public void testFindByConfirmationCodeIsNullAndHoldTimeBefore() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiredDateTime = now.minusSeconds(SEAT_HOLD_EXPIRATION_TIME_IN_SECONDS);
		Instant instant = expiredDateTime.atZone(ZoneId.systemDefault()).toInstant();

		List<SeatHold> seatHolds = seatHoldRepository.findByConfirmationCodeIsNullAndHoldTimeBefore(Date.from(instant));
		assertNotNull(seatHolds);
		assertTrue(seatHolds.size() == 1);
		assertTrue(seatHolds.get(0).getCustomer().getEmail().equalsIgnoreCase("homer@simpson.com"));
	}

	@Test
	public void testFind() {
		Customer customer = customerRepository.findByEmail("homer@simpson.com");
		List<SeatHold> seatHolds = seatHoldRepository.findByCustomer(customer);
		assertNotNull(seatHolds);
		assertTrue(seatHolds.size() == 2);
		assertTrue(seatHolds.get(0).getCustomer().getEmail().equalsIgnoreCase("homer@simpson.com"));
	}

}
