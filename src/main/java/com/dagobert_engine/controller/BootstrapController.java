package com.dagobert_engine.controller;


import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.dagobert_engine.service.Configuration;
import com.dagobert_engine.service.KrakenService;
import com.dagobert_engine.service.Service;
import com.dagobert_engine.service.Dagobert;
import com.dagobert_engine.service.TradeExecutor;

@Singleton
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Startup
public class BootstrapController {
	
	@Inject
	private Configuration config;
	
	@Inject
	private Dagobert serviceBook;
	
	@Inject
	private KrakenService krakenService;
	
	@Inject
	private TradeExecutor executor;

	private Logger logger = Logger.getLogger(getClass());

	@PostConstruct
	public void postConstruct() throws InterruptedException {
		logger.info("###################################################");
		logger.info("###################################################");
		logger.info("###########                            ############");
		logger.info("###########        -----------         ############");
		logger.info("###########        \\         /         ############");
		logger.info("###########         )       (          ############");
		logger.info("###########       =============        ############");
		logger.info("###########      /  /  \\ /  \\ \\        ############");
		logger.info("###########      |  |()| |()|  |       ############");
		logger.info("###########      |  \\__/ \\__/  |       ############");
		logger.info("###########      \\     ..      /       ############");
		logger.info("###########  ####(=============)####   ############");
		logger.info("###########   ####            ####     ############");
		logger.info("###########                            ############");
		logger.info("###########                            ############");
		logger.info("###########   D  A  G  O  B  E  R  T   ############");
		logger.info("###########       Trading Engine       ############");
		logger.info("###########                            ############");
		logger.info("###########                            ############");
		logger.info("###########         COPYRIGHT          ############");
		logger.info("###########  ----------------------    ############");
		logger.info("###########   (c) Michael Kunzmann     ############");
		logger.info("###########  mail@michaelkunzmann.com  ############");
		logger.info("###########                            ############");
		logger.info("###########     License: APACHE 2.0    ############");
		logger.info("###########                            ############");
		logger.info("###################################################");
		logger.info("###################################################");
		
		// Loading config
		config.reload();
		
		// Register services
		serviceBook.registerService(KrakenService.SERVICE_NAME, krakenService);
		
		
		final Thread thread = new Thread() {

			@Override
			public void run() {
				while (true) {
					for (Service service : serviceBook.getServices().values()) {
						executor.trade(service);
					}

					try {
						sleep(config.getUpdateTime() * 1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		thread.start();
		
	}
}
