package com.dagobert_engine.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.logging.Logger;

import com.dagobert_engine.model.Period;
import com.xeiam.xchange.currency.CurrencyPair;

@ApplicationScoped
public class TradeExecutor {

	private Logger logger = Logger.getLogger(getClass());

	@Inject
	private Configuration config;


	public TradeExecutor() {
	}

	public void trade(Service service) {
		service.updateData();

		// Every CurrencyPair must have data of at least 2 periods
		for (CurrencyPair pair : config.getTradedCurrencyPairs()) {

			final List<Period> availablePeriods = service
					.getCurrencyPairStatistics().get(pair)
					.getAvailablePeriods();

			if (availablePeriods.size() < 2) {
				logger.info("We need minimum 2 periods to trade. Waiting for the first period to end...");
				return;
			}
		}
		
		

	}
}
