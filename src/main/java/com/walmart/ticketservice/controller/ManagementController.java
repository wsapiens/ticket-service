package com.walmart.ticketservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketservice.service.ResetService;

/**
 * Controller for management action
 * 
 * @author spark
 *
 */
@RestController
@RequestMapping(value = "/management")
public class ManagementController {

	@Autowired
	private ResetService resetService;

	/**
	 * this endpoint is opened just for helping test easy to clean up database
	 * @return result string
	 */
	@RequestMapping(value="/seat-holds", method=RequestMethod.DELETE)
	public String reset() {
		resetService.reset();
		return "All SeatHolds with Customer Info have been deleted!";
	}
}
