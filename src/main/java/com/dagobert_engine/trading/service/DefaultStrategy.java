package com.dagobert_engine.trading.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.apache.commons.lang.NotImplementedException;

import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.trading.model.Order;

/**
 * Default, useless strategy.
 * You have to implement your own.
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@Default
public class DefaultStrategy implements Strategy {
	@Inject
	private Logger logger;

	@Override
	public Order createOrder() {
		logger.log(Level.WARNING, "No Strategy set. Please implement " + Strategy.class.getName() + ", annotate it with @Alternative and add your implementation to beans.xml as alternative.");
		return null;
	}

	@Override
	public boolean isBuying() {
		throw new NotImplementedException();
	}

	@Override
	public boolean isSelling() {
		throw new NotImplementedException();
	}

	@Override
	public CurrencyData getLastBuyPrice() {
		throw new NotImplementedException();
	}

	@Override
	public CurrencyData getLastSellPrice() {
		throw new NotImplementedException();
	}


}
