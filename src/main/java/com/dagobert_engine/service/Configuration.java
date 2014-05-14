package com.dagobert_engine.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

import com.xeiam.xchange.currency.CurrencyPair;

@ApplicationScoped
public class Configuration implements Serializable {
	private static final long serialVersionUID = -6024254531148539599L;

	private final String CONFIG_FILE = "META-INF/application.properties";

	private Map<String, CurrencyPair> currencyPairs;
	private String krakenUsername;
	private String krakenApiKey;
	private String krakenSecret;
	private int periods = 4;
	private int defaultPeriodLength = 5;
	private List<CurrencyPair> tradedCurrencyPairs;
	private long updateTime = 1;
	private boolean mock = true;

	private Logger logger = Logger.getLogger(getClass());

	public Configuration() {
		super();
		// TODO Auto-generated constructor stub

		currencyPairs = new HashMap<String, CurrencyPair>(); 
		tradedCurrencyPairs = new ArrayList<>();
	}

	public void reload() {
		
		logger.info("Reloading configuration...");
		
		
		final Class<CurrencyPair> currencyPairClass = CurrencyPair.class;
		
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
		Properties props = new Properties();
		try {
			props.load(inputStream);

			defaultPeriodLength = Integer.parseInt(props
					.getProperty("defaultPeriodLength"));
			
			
			final String[] currencyKeys = props.getProperty("tradedCurrencyPairs").split(",");
			for (String key : currencyKeys) {
				tradedCurrencyPairs.add((CurrencyPair) currencyPairClass.getField(key).get(null));
			}
			
			updateTime = Long.parseLong(props.getProperty("updateTime"));
			
			periods = Integer.parseInt(props.getProperty("periods"));

			krakenApiKey = props.getProperty("krakenApiKey");
			krakenSecret = props.getProperty("krakenSecret");
			krakenUsername = props.getProperty("krakenUsername");
			mock = Boolean.valueOf("mock");
			
			// Read all currency pairs
			final Field[] fields = currencyPairClass.getFields();
			for (Field field : fields) {
				if (CurrencyPair.class.equals(field.getType())) {
					CurrencyPair pair = (CurrencyPair) field.get(null);
					currencyPairs.put(field.getName(), pair);
				}
			}
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getDefaultPeriodLength() {
		return defaultPeriodLength;
	}

	public void setDefaultPeriodLength(int defaultPeriodLength) {
		this.defaultPeriodLength = defaultPeriodLength;
	}

	

	public String getKrakenUsername() {
		return krakenUsername;
	}

	public void setKrakenUsername(String krakenUsername) {
		this.krakenUsername = krakenUsername;
	}

	public String getKrakenApiKey() {
		return krakenApiKey;
	}

	public void setKrakenApiKey(String krakenApiKey) {
		this.krakenApiKey = krakenApiKey;
	}

	public String getKrakenSecret() {
		return krakenSecret;
	}

	public void setKrakenSecret(String krakenSecret) {
		this.krakenSecret = krakenSecret;
	}

	public List<CurrencyPair> getTradedCurrencyPairs() {
		return tradedCurrencyPairs;
	}

	public void setTradedCurrencyPairs(List<CurrencyPair> tradedCurrencyPairs) {
		this.tradedCurrencyPairs = tradedCurrencyPairs;
	}

	public void setCurrencyPairs(Map<String, CurrencyPair> currencyPairs) {
		this.currencyPairs = currencyPairs;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}
	
	
	public boolean isMock() {
		return mock;
	}

	public void setMock(boolean mock) {
		this.mock = mock;
	}

	public Map<String, CurrencyPair> getCurrencyPairs() {
		return currencyPairs;
	}

	
}