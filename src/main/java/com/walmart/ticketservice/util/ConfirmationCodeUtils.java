package com.walmart.ticketservice.util;

import static com.walmart.ticketservice.consts.ServiceConsts.CODE_GENERATOR_SEED_PREFIX;
import static com.walmart.ticketservice.consts.ServiceConsts.CODE_DELIMITER;

import java.util.UUID;

public class ConfirmationCodeUtils {

	private ConfirmationCodeUtils() {}

	public static String generateCode(int seatHoldId, String customerEmail) {
		String name = String.join(CODE_DELIMITER, CODE_GENERATOR_SEED_PREFIX, customerEmail, String.valueOf(seatHoldId));
		UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
		return uuid.toString();
	}
}
