package com.walmart.ticketservice.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Aop Service Tracker to write work log.
 * 
 * @author spark
 *
 */
@Aspect
@Component
public class ServiceTracker {

	static Logger log = Logger.getLogger(ServiceTracker.class.getName());

	@Before(value="execution(* @org.springframework.stereotype.Service com.walmart.ticketservice..*(..)) ")
	public void beforeProcess(JoinPoint joinPoint) {
		if(log.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder("[ReqIn ] ");
			sb.append(joinPoint.getTarget().getClass().getSimpleName())
			.append(" ( ")
			.append(joinPoint.getSignature())
			.append(" )");
			log.info(sb.toString());
		}
	}

	@AfterReturning(value="execution(* @org.springframework.stereotype.Service com.walmart.ticketservice..*(..)) ", returning="result")
	public void afterProcess(JoinPoint joinPoint, Object result) {
		if(log.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder("[ReqOut] ");
			sb.append(joinPoint.getTarget().getClass().getSimpleName())
			.append(" ( ")
			.append(joinPoint.getSignature())
			.append(" )");
			log.info(sb.toString());
		}
	}

	@AfterThrowing(value="execution(* @org.springframework.stereotype.Service com.walmart.ticketservice..*(..)) ", throwing="e")
	public void failProcess(JoinPoint joinPoint, Exception e) {
		if(log.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder("[ReqErr] ");
			sb.append(joinPoint.getTarget().getClass().getSimpleName())
			.append(" ( ")
			.append(joinPoint.getSignature())
			.append(" ) - ")
			.append(e.getMessage());
			log.info(sb.toString());
		}
	}
}
