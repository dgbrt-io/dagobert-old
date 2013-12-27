package com.dagobert_engine.trading.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.dagobert_engine.trading.service.MtGoxTradeService.MtGoxStatus;

/**
 * MtGox controller
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@Named("mtgox")
@ApplicationScoped
public class MtGoxController {
	
	public String startTrading() {
		return null;
	}
	public MtGoxStatus getStatus() {
		return null;
	}
	public String stopTrading() {
		return null;
	}

}
