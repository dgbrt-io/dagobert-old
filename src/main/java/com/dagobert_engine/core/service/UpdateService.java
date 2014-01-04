package com.dagobert_engine.core.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.dagobert_engine.core.model.Configuration;
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
	private Configuration config;
	
	private boolean running = true;

	public UpdateService() {
	}
	
	@PostConstruct
	public void postConstruct() {
		
		
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
		
		if (!config.isTradingEnabled()) {
			logger.warning("    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    ");
			logger.warning("    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    ");
		    logger.warning("    !!!!!                                 !!!!!    ");
			logger.warning("    !!!!!          W A R N I N G          !!!!!    ");
		    logger.warning("    !!!!!                                 !!!!!    ");
		    logger.warning("    !!!!!   Trading is disabled. If you   !!!!!    ");
		    logger.warning("    !!!!!    want to enable it, go to     !!!!!    ");
		    logger.warning("    !!!!!        src/main/resources       !!!!!    ");
		    logger.warning("    !!!!!     /META-INF/seam-beans.xml    !!!!!    ");
		    logger.warning("    !!!!!                                 !!!!!    ");
			logger.warning("    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    ");
			logger.warning("    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    ");
		}
		
		begin();
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
				long timeout = config.getUpdateTime();
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
				if (config.isTradingEnabled()) {
					traderService.trade();
				}
				
				
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
