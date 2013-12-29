package com.dagobert_engine.core.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.dagobert_engine.core.util.KeyName;
import com.dagobert_engine.core.util.MtGoxConnectionError;
import com.dagobert_engine.statistics.service.MtGoxStatisticsService;
import com.dagobert_engine.trading.service.MtGoxTradeService;

/**
 * This is the main loop
 * 
 * TODO: No EJB, only CDI. Transaction start via Seam Transactions
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@Singleton
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Startup
public class UpdateService extends Thread {
	
	@Inject
	private MtGoxStatisticsService ratesService;
	
	@Inject
	private MtGoxTradeService traderService;

	@Inject
	private Logger logger;
	
	@Inject
	private ConfigService config;
	
	private boolean running = true;

	public UpdateService() {
	}
	
	@PostConstruct
	public void postConstruct() {
		begin();
		logger.log(Level.INFO, "UpdateTimer initialized.");
	}
	
	public void cancel() {
		running = false;
	}
	
	public void begin() {
		start();
	}
	
	@Override
	public void run() {
		running = true;
		
		
		// Transaction is started by EJB
//		Context c = new InitialContext();
//		UserTransaction trans = (UserTransaction) c.lookup("java:jboss/UserTransaction");
//		
//		
//		if (trans.getStatus() != javax.transaction.Status.STATUS_ACTIVE) {
//			trans.begin();
//		}
		
		while (running) {
			try {
				long timeout = Long.parseLong(config.getProperty(KeyName.UPDATE_TIME));
				sleep(timeout * 1000L);
			} catch (NumberFormatException e) {
				running = false;
				logger.log(Level.SEVERE, e.getMessage());
				return;
			} catch (InterruptedException e) {
				running = false;
				logger.log(Level.SEVERE, e.getMessage());
				return;
			}
			
			try {
				
				// Refresh period data
				ratesService.refreshPeriods();
	
				// Do trading
				traderService.trade();
				
				
	//			trans.commit();
			}
			catch (MtGoxConnectionError exc) {
	//			trans.rollback();
				logger.severe("HTTP Error: " + exc.getCode() + ", answer: " + exc.getAnswer());
			}
			catch (Exception exc) {
	//			trans.rollback();
				exc.printStackTrace();
			}
		}
		running = false;
	}

	public boolean isRunning() {
		return running;
	}
}
