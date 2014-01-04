package com.dagobert_engine.core.model;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

/**
 * Used to read settings 
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 * 
 *          License http://www.apache.org/licenses/LICENSE-2.0
 * 
 */
@ApplicationScoped
public class Configuration implements Serializable {
	
	private static final long serialVersionUID = -6024254531148539599L;
	
	
	private boolean tradingEnabled;
	private int defaultPeriodLength;
	private CurrencyType defaultCurrency;
	private long updateTime;
	
	
	
	public Configuration() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getDefaultPeriodLength() {
		return defaultPeriodLength;
	}
	public void setDefaultPeriodLength(int defaultPeriodLength) {
		this.defaultPeriodLength = defaultPeriodLength;
	}
	public CurrencyType getDefaultCurrency() {
		return defaultCurrency;
	}
	public void setDefaultCurrency(CurrencyType defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public boolean isTradingEnabled() {
		return tradingEnabled;
	}
	public void setTradingEnabled(boolean tradingEnabled) {
		this.tradingEnabled = tradingEnabled;
	}
}
