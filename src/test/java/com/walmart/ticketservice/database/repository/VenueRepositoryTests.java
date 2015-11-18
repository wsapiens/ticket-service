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

import com.walmart.ticketservice.consts.VenueLevel;
import com.walmart.ticketservice.database.model.Venue;

@ContextConfiguration(classes = {RepositoryTestConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@DirtiesContext
public class VenueRepositoryTests {

	@Autowired
	private VenueRepository venueRepository;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test for finding venue by venue level id
	 * test venue data should be setup by schema.sql and import.sql under src/main/resources
	 */
	@Test
	public void testFindOneVenueByLevelId() {
		Venue venue = venueRepository.findOne(VenueLevel.MAIN.getId());
		assertNotNull(venue);
		assertTrue(venue.getLevelId() == VenueLevel.MAIN.getId());

		venue = venueRepository.findOne(VenueLevel.ORCHESTRA.getId());
		assertNotNull(venue);
		assertTrue(venue.getLevelId() == VenueLevel.ORCHESTRA.getId());

		venue = venueRepository.findOne(VenueLevel.BALCONY1.getId());
		assertNotNull(venue);
		assertTrue(venue.getLevelId() == VenueLevel.BALCONY1.getId());

		venue = venueRepository.findOne(VenueLevel.BALCONY2.getId());
		assertNotNull(venue);
		assertTrue(venue.getLevelId() == VenueLevel.BALCONY2.getId());
	}

}
