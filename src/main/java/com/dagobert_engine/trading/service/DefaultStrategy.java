package com.dagobert_engine.trading.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.trading.model.Order;

/**
 * Default, useless strategy
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
		logger.log(Level.WARNING, "No Strategy set");
		return null;
	}

	@Override
	public boolean isBuying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSelling() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CurrencyData getLastBuyPrice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CurrencyData getLastSellPrice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CurrencyData getLastEarnings() {
		// TODO Auto-generated method stub
		return null;
	}

}
