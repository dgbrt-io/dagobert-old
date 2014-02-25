package com.dagobert_engine.core.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.ResourceBundle;

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

	private final String CONFIG_FILE = "META-INF/application.properties";

	private boolean tradingEnabled = false;
	private int defaultPeriodLength = 300000;
	private CurrencyType defaultCurrency = CurrencyType.USD;
	private long updateTime = 1;

	public Configuration() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void reload() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
		Properties props = new Properties();
		try {
			props.load(inputStream);

			tradingEnabled = Boolean.parseBoolean(props
					.getProperty("tradingEnabled"));
			defaultPeriodLength = Integer.parseInt(props
					.getProperty("defaultPeriodLength"));
			defaultCurrency = CurrencyType.valueOf(props
					.getProperty("defaultCurrency"));
			updateTime = Long.parseLong(props.getProperty("updateTime"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
