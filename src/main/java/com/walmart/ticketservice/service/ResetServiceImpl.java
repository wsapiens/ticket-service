package com.walmart.ticketservice.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ticketservice.database.repository.CustomerRepository;
import com.walmart.ticketservice.database.repository.SeatHoldRepository;

/**
 * This service is for resetting database to give convenience on testing
 * In actual service, we may not have this kind of service
 * 
 * @author spark
 *
 */
@Service("resetService")
@Transactional(value="transactionManager", isolation= Isolation.SERIALIZABLE)
public class ResetServiceImpl implements ResetService {

	private static Logger log = Logger.getLogger(ResetServiceImpl.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private SeatHoldRepository seatHoldRepository;

	@Override
	public void reset() {
		log.info("received reset request, so clean up seatHold and customer tables on database");
		seatHoldRepository.deleteAll();
		customerRepository.deleteAll();
	}

}
