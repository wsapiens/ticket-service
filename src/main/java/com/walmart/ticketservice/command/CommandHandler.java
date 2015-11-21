package com.walmart.ticketservice.command;

import static com.walmart.ticketservice.consts.PropertyType.ACTION;
import static com.walmart.ticketservice.consts.PropertyType.EMAIL;
import static com.walmart.ticketservice.consts.PropertyType.HOLD_ID;
import static com.walmart.ticketservice.consts.PropertyType.LEVEL;
import static com.walmart.ticketservice.consts.PropertyType.MAX_LEVEL;
import static com.walmart.ticketservice.consts.PropertyType.MIN_LEVEL;
import static com.walmart.ticketservice.consts.PropertyType.NUM_SEATS;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.walmart.ticketservice.consts.CommandType;
import com.walmart.ticketservice.database.model.SeatHold;
import com.walmart.ticketservice.service.ResetService;
import com.walmart.ticketservice.service.TicketService;

/**
 * Class for handling command line request
 * This is for helping test via command line
 * result will be printed out on command line
 * 
 * @author spark
 *
 */
@Component
public class CommandHandler {

	@Autowired
	private TicketService ticketService;

	@Autowired
	private ResetService resestService;

	public void execute(@SuppressWarnings("rawtypes") PropertySource ps) {
		if(null != ps) {
			try{
				if( ps.containsProperty(ACTION.toString()) ) {
					String action = (String) ps.getProperty(ACTION.toString());
					CommandType commandType = CommandType.valueOf(action.toUpperCase());
					switch(commandType) {
					case SEARCH: 	System.out.println( new StringBuilder("Number Of Available Seats: ").append( searchNumberOfAvailableSeats(ps) ).toString());
						break;
					case HOLD:		System.out.println( new StringBuilder("Seat HoldId: ").append( findAndHoldSeats(ps) ).toString());
						break;
					case RESERVE:	System.out.println( new StringBuilder("Reservation Confirmation Code: ").append( reserveSeats(ps) ).toString());
						break;
					case RESET:		resestService.reset();
									System.out.println("Datbase has been cleaned up with removing all hold, reservation and customer info");
						break;
					default:		System.out.println("unknown command type!");
						break;
					}
				} else {
					System.out.println("Please provide action type [search, hold, reserve]. ex) --action=hold");
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Search number of available seats for given level
	 * @param ps PropertySource
	 * @return number of available seats
	 */
	private int searchNumberOfAvailableSeats(@SuppressWarnings("rawtypes") PropertySource ps) {
		String levelString = (String) ps.getProperty(LEVEL.toString());
		Optional<Integer> venueLevel = StringUtils.isEmpty(levelString) ? Optional.empty() : Optional.of(Integer.valueOf( levelString ));
		return ticketService.numSeatsAvailable(venueLevel);
	}

	/**
	 * Find and Hold Seats
	 * @param ps PropertySource
	 * @return holdId
	 */
	private int findAndHoldSeats(@SuppressWarnings("rawtypes") PropertySource ps) {
		Integer numSeats = Integer.valueOf( (String) ps.getProperty(NUM_SEATS.toString()) );
		String minLevelString = (String) ps.getProperty(MIN_LEVEL.toString());
		String maxLevelString = (String) ps.getProperty(MAX_LEVEL.toString());
		String customerEmail = (String) ps.getProperty(EMAIL.toString());

		Optional<Integer> minLevel = StringUtils.isEmpty(minLevelString) ? Optional.empty() : Optional.of(Integer.valueOf(minLevelString));
		Optional<Integer> maxLevel = StringUtils.isEmpty(maxLevelString) ? Optional.empty() : Optional.of(Integer.valueOf(maxLevelString));
		SeatHold seatHold = ticketService.findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail);
		return seatHold.getId();
	}

	/**
	 * Reserve Seats
	 * @param ps PropertySource
	 * @return reservation confirmation code
	 */
	private String reserveSeats(@SuppressWarnings("rawtypes") PropertySource ps) {
		Integer seatHoldId = Integer.valueOf( (String) ps.getProperty(HOLD_ID.toString()) );
		String customerEmail = (String) ps.getProperty(EMAIL.toString());
		return ticketService.reserveSeats(seatHoldId, customerEmail);
	}
}
