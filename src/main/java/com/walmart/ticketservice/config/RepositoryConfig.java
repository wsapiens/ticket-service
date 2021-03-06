package com.walmart.ticketservice.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.walmart.ticketservice.database.model"})
@EnableJpaRepositories(
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager",
		basePackages = {"com.walmart.ticketservice.database.repository"})
public class RepositoryConfig {

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(entityManagerFactory);
		return tm;
	}
}
