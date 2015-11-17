package com.walmart.ticketservice.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfirmationCodeUtilsTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateCode() {
		String code = ConfirmationCodeUtils.generateCode(1, "customer@email.com");
		assertNotNull(code);
		assertFalse(code.isEmpty());
	}

}
