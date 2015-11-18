package com.walmart.ticketservice.database.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

@ContextConfiguration(classes = {RepositoryTestConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@DirtiesContext
public class CustomerRepositoryTests {

	@Autowired
	private CustomerRepository customerRepository;

	@Before
	public void setUp() throws Exception {
		Customer homer = new Customer("homer@simpson.com");
		customerRepository.save(homer);

		Customer peter = new Customer("peter@griffin.com");
		customerRepository.save(peter);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindByEmail() {
		Customer customer = customerRepository.findByEmail("homer@simpson.com");
		assertNotNull(customer);
		assertTrue(customer.getEmail().equalsIgnoreCase("homer@simpson.com"));
	}

}
