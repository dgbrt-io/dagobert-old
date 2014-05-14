package com.dagobert_engine.service;

import java.math.BigDecimal;
import java.util.Map;

import com.dagobert_engine.model.CurrencyPairStatistics;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;

public interface Service {
	
	public Exchange getExchange();
	public Map<CurrencyPair, CurrencyPairStatistics> getCurrencyPairStatistics();
	public void updateData();
	public String getName();
	public Map<String, Object> getInfo();
	public void buy(CurrencyPair pair, BigDecimal amount);
	public void sell(CurrencyPair pair, BigDecimal amount);
}
