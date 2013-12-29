package com.dagobert_engine.trading.service;

import javax.enterprise.context.ApplicationScoped;

import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.trading.model.Order;

/**
 * Implement this interface to define your own strategy
 * Don't forget setting it up in bean.xml
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@ApplicationScoped
public interface Strategy {
	
	public Order createOrder();
	public boolean isBuying();
	public boolean isSelling();
	
	public CurrencyData getLastBuyPrice();
	public CurrencyData getLastSellPrice();
	public CurrencyData getLastEarnings();

}
