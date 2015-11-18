package com.walmart.ticketservice.consts;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VenueLevelTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLevels() {
		List<VenueLevel> levels = VenueLevel.getLevels(Optional.of(2), Optional.of(3));
		assertTrue(levels.size() == 2);
		assertTrue(levels.get(0).getId() == 2 );
		assertTrue(levels.get(1).getId() == 3 );

		levels = VenueLevel.getLevels(Optional.of(4), Optional.of(4));
		assertTrue(levels.size() == 1);
		assertTrue(levels.get(0).getId() == 4 );

		levels = VenueLevel.getLevels(Optional.of(1), Optional.of(4));
		assertTrue(levels.size() == 4);
		assertTrue(levels.get(0).getId() == 1 );
		assertTrue(levels.get(1).getId() == 2 );
		assertTrue(levels.get(2).getId() == 3 );
		assertTrue(levels.get(3).getId() == 4 );

		levels = VenueLevel.getLevels(Optional.empty(), Optional.of(3));
		assertTrue(levels.size() == 3);
		assertTrue(levels.get(0).getId() == 1 );
		assertTrue(levels.get(1).getId() == 2 );
		assertTrue(levels.get(2).getId() == 3 );

		levels = VenueLevel.getLevels(Optional.of(2), Optional.empty());
		assertTrue(levels.size() == 3);
		assertTrue(levels.get(0).getId() == 2 );
		assertTrue(levels.get(1).getId() == 3 );
		assertTrue(levels.get(2).getId() == 4 );

		levels = VenueLevel.getLevels(Optional.empty(), Optional.empty());
		assertTrue(levels.size() == 4);
		assertTrue(levels.get(0).getId() == 1 );
		assertTrue(levels.get(1).getId() == 2 );
		assertTrue(levels.get(2).getId() == 3 );
		assertTrue(levels.get(3).getId() == 4 );

		// if mix given bigger than max, change min same as max
		levels = VenueLevel.getLevels(Optional.of(3), Optional.of(2));
		assertTrue(levels.size() == 1);
		assertTrue(levels.get(0).getId() == 2 );
	}

}
