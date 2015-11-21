package com.walmart.ticketservice.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MockServletContext.class, ControllerTestConfig.class})
@WebAppConfiguration
public class ServiceControllerTests {

	@Autowired
	private ServiceController serviceController;

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(serviceController).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAvailableSeats() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/ticket-service/v1/available-seats/venue?level=2"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
	}

	@Test
	public void testFindAndHoldSeats() throws Exception {
		// give min and max level both
		mvc.perform(MockMvcRequestBuilders.post("/ticket-service/v1/hold/num-seats/50/email/test@email.com/venue?minLevel=2&maxLevel=3"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// give min level and open for max
		mvc.perform(MockMvcRequestBuilders.post("/ticket-service/v1/hold/num-seats/50/email/test@email.com/venue?minLevel=2"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// give max level and open for min
		mvc.perform(MockMvcRequestBuilders.post("/ticket-service/v1/hold/num-seats/50/email/test@email.com/venue?maxLevel=3"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// give same level for min and max
		mvc.perform(MockMvcRequestBuilders.post("/ticket-service/v1/hold/num-seats/50/email/test@email.com/venue?minLevel=3&maxLevel=3"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
	}

	@Test
	public void testReserveSeats() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/ticket-service/v1/hold/150/email/test@email.com/reserve"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
	}

	/**
	 * Simulate the case where giving expired holdId
	 * @throws Exception
	 */
	@Test
	public void testReserveSeatsWithNoSeatHoldFoundException() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/ticket-service/v1/hold/40/email/test@email.com/reserve"))
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andReturn();
	}

	/**
	 * Simulate the case where giving wrong customer email address
	 * @throws Exception
	 */
	@Test
	public void testReserveSeatsWithCustomerValidationException() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/ticket-service/v1/hold/150/email/not.existing@email.com/reserve"))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andReturn();
	}

}
