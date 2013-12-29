package com.dagobert_engine.statistics.service;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.dagobert_engine.core.model.AdvancedCurrencyStatistics;
import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.core.model.CurrencyStatistics;
import com.dagobert_engine.core.model.CurrencyType;
import com.dagobert_engine.core.service.ConfigService;
import com.dagobert_engine.core.service.MtGoxApiAdapter;
import com.dagobert_engine.core.util.KeyName;
import com.dagobert_engine.core.util.MtGoxException;
import com.dagobert_engine.statistics.model.BTCRate;
import com.dagobert_engine.statistics.model.Period;
import com.dagobert_engine.statistics.model.Period.PropabilityType;

/**
 * StatisticsService
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@ApplicationScoped
public class MtGoxStatisticsService implements Serializable {

	public enum StatisticsServiceStatus {
		RUNNING, NOT_RUNNING;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3776561847459503540L;

	@Inject
	private ConfigService configService;

	@Inject
	private MtGoxApiAdapter adapter;
	

	private JSONParser parser = new JSONParser();

	/**
	 * Current period
	 */
	private Period currentPeriod = null;
	
	/**
	 * Last period
	 */
	private Period lastPeriod = null;

	private final Object lock = new Object();
	

	private static String getTickerPath(CurrencyType cur, boolean fast) {

		if (cur.equals(CurrencyType.BTC)) {
			throw new MtGoxException(cur + " is no reference currency. Please select any other currency");
		}
		
		return "BTC" + cur.name() + "/MONEY/TICKER" + (fast ? "_FAST" : "");
	}
	

	// TODO MONEY/DEPTH/FETCH
	// TODO MONEY/DEPTH/FULL
	
	/**
	 * Get advanced statistics with the normal ticker
	 * 
	 * @param type
	 * @return
	 */
	public AdvancedCurrencyStatistics getAdvancedStatistics(CurrencyType type) {
		if (type.equals(CurrencyType.BTC))
			throw new MtGoxException(type + " is no reference currency. Please select any other currency");
		
		String url = getTickerPath(type, false);
		String queryJson = adapter.query(url);
		
		try {
			JSONObject root = (JSONObject) parser.parse(queryJson);
			String result = (String) root.get("result");
			
			
			if (!"success".equals(result)) {
				throw new MtGoxException(result);
			}
			
			JSONObject data = (JSONObject) root.get("data");
			
			AdvancedCurrencyStatistics stats = new AdvancedCurrencyStatistics();
			stats.setHigh(adapter.getCurrencyForJsonObj((JSONObject) data.get("high")));
			stats.setLow(adapter.getCurrencyForJsonObj((JSONObject) data.get("low")));
			stats.setAvg(adapter.getCurrencyForJsonObj((JSONObject) data.get("avg")));
			stats.setVolumeWeightenedAvg(adapter.getCurrencyForJsonObj((JSONObject) data.get("vwap")));
			stats.setVolume(adapter.getCurrencyForJsonObj((JSONObject) data.get("vol")));
			
			stats.setLastTradeDefaultCurrency(adapter.getCurrencyForJsonObj((JSONObject) data.get("last_local")));
		    stats.setLastTradeAnyCurrency(adapter.getCurrencyForJsonObj((JSONObject) data.get("last_orig")));
		    stats.setLastTradeConvertedToDefault(adapter.getCurrencyForJsonObj((JSONObject) data.get("last_all")));
		    stats.setBuy(adapter.getCurrencyForJsonObj((JSONObject) data.get("buy")));
		    stats.setSell(adapter.getCurrencyForJsonObj((JSONObject) data.get("sell")));
		    stats.setTime(new Date(Long.valueOf(((String) data.get("now"))) / 1000));
		    stats.setTimestampMicroSecs(Long.valueOf((String) data.get("now")));
		    
		    return stats;
		} catch (org.json.simple.parser.ParseException e) {
			throw new MtGoxException(e);
		}
	}
	
	/**
	 * Get statistics for currency with the fast ticker
	 * 
	 * @param type
	 * @return
	 */
	public CurrencyStatistics getStatistics(CurrencyType type) {
		
		if (type.equals(CurrencyType.BTC)) {
			throw new MtGoxException(type + " is no reference currency. Please select any other currency");
		}
		
		String url = getTickerPath(type, true);
		String queryJson = adapter.query(url);
		
		try {
			JSONObject root = (JSONObject) parser.parse(queryJson);
			String result = (String) root.get("result");
			
			
			if (!"success".equals(result)) {
				throw new MtGoxException(result);
			}
			
			JSONObject data = (JSONObject) root.get("data");
			
			CurrencyStatistics stats = new CurrencyStatistics();
		    stats.setLastTradeDefaultCurrency(adapter.getCurrencyForJsonObj((JSONObject) data.get("last_local")));
		    stats.setLastTradeAnyCurrency(adapter.getCurrencyForJsonObj((JSONObject) data.get("last_orig")));
		    stats.setLastTradeConvertedToDefault(adapter.getCurrencyForJsonObj((JSONObject) data.get("last_all")));
		    stats.setBuy(adapter.getCurrencyForJsonObj((JSONObject) data.get("buy")));
		    stats.setSell(adapter.getCurrencyForJsonObj((JSONObject) data.get("sell")));
		    stats.setTime(new Date(Long.parseLong(((String) data.get("now"))) / 1000));
		    stats.setTimestampMicroSecs(Long.parseLong(((String) data.get("now"))));
		    
		    return stats;
		} catch (org.json.simple.parser.ParseException e) {
			throw new MtGoxException(e);
		}
	}
	
	/**
	 * Get last price
	 * 
	 * @param cur
	 * @return
	 * @throws ParseException
	 */
	public CurrencyData getLastPrice(CurrencyType cur) {
		if (cur == null) {
			throw new IllegalArgumentException("CurrencyType mustn't be null");
		}
		
		if (cur.equals(CurrencyType.BTC)) {
			throw new MtGoxException(cur + " is no reference currency. Please select any other currency.");
		}

		String urlPath = getTickerPath(cur, true);
		// long divideFactor =
		// adapter.getDivisionFactors().get(cur).longValue();
		HashMap<String, String> query_args = new HashMap<>();

		/*
		 * Params : No params required
		 */
		String queryResult = adapter.query(urlPath, query_args);

		try {
			double last;
			
			JSONObject httpAnswerJson = (JSONObject) (parser.parse(queryResult));
			String result = (String) httpAnswerJson.get("result");
			
			if (!"success".equals(result)) {
				throw new MtGoxException(result);
			}
			
			JSONObject dataJson = (JSONObject) httpAnswerJson.get("data");
			JSONObject lastJson = (JSONObject) dataJson.get("last");
			String last_String = (String) lastJson.get("value");
			last = Double.parseDouble(last_String);
			return new CurrencyData(last, cur);
		} catch (Exception exc) {
			throw new MtGoxException(exc);
		}
	}

	
	
	
	public void refreshPeriods() throws ParseException {
		synchronized (lock) {

			final Date NOW = new Date();
			final int periodLength = Integer.parseInt(configService.getProperty(KeyName.DEFAULT_PERIOD_LENGTH));

			if (currentPeriod == null || NOW.after(currentPeriod.getToTime())) {
				lastPeriod = currentPeriod;

				currentPeriod = new Period();
				currentPeriod.setFromTime(NOW);
				currentPeriod.setToTime(new Date(NOW.getTime() + periodLength));
			}

			BTCRate rate = new BTCRate();
			rate.setDateTime(NOW);
			rate.setCurrency(CurrencyType.valueOf(configService.getProperty(KeyName.DEFAULT_CURRENCY)));
			rate.setValue(getLastPrice(rate.getCurrency()).getValue());

			// Add rate to current period
			rate.setPeriod(currentPeriod);
			currentPeriod.getRates().add(rate);

			// calculate values for period

			currentPeriod.setAvgRate(calcAvgForRates(currentPeriod.getRates()));
			currentPeriod.setLatestRate(rate);

			BTCRate maxRate = currentPeriod.getRates().get(
					calcMaxIndex(currentPeriod.getRates()));
			BTCRate minRate = currentPeriod.getRates().get(
					calcMinIndex(currentPeriod.getRates()));
			currentPeriod.setMaxRate(maxRate);
			currentPeriod.setMaxRate(minRate);

			currentPeriod.setStdDev(calcStdDev(currentPeriod.getRates()));
			
			currentPeriod.setPropDown(calcPropability(currentPeriod.getAvgRate(), currentPeriod.getStdDev(), PropabilityType.LESS_THAN, currentPeriod.getLatestRate().getValue()));
			currentPeriod.setPropUp(calcPropability(currentPeriod.getAvgRate(), currentPeriod.getStdDev(), PropabilityType.GREATER_THAN, currentPeriod.getLatestRate().getValue()));
		}
	}

	private static double calcPropability(double avg, double dev,
			PropabilityType type, double value) {
		NormalDistributionImpl normDistr = new NormalDistributionImpl();

		if (dev == 0.0)
			return 0.0;

		double stdValue = (value - avg) / dev;

		try {
			switch (type) {
			case GREATER_THAN:
				return 1 - normDistr.cumulativeProbability(stdValue);
			case LESS_THAN:
				return normDistr.cumulativeProbability(stdValue);
			}
			return 0.0;
		} catch (MathException exc) {
			throw new RuntimeException(exc);

		}
	}

	private static double calcAvgForRates(List<BTCRate> rates) {
		double avg = 0.0;

		for (BTCRate rate : rates) {
			avg += rate.getValue();
		}

		if (rates.size() > 0)
			avg = avg / ((double) rates.size());

		return avg;
	}

	private static int calcMinIndex(List<BTCRate> rates) {
		int minIndex = -1;
		for (int i = 0; i < rates.size(); i++) {
			if (minIndex == -1)
				minIndex = 0;
			if (rates.get(i).getValue() < rates.get(minIndex).getValue())
				minIndex = i;
			i++;
		}

		return minIndex;
	}

	private static int calcMaxIndex(List<BTCRate> rates) {
		int maxIndex = -1;
		int i = 0;
		for (BTCRate rate : rates) {
			if (maxIndex == -1)
				maxIndex = 0;
			if (rate.getValue() > rates.get(i).getValue())
				maxIndex = i;
			i++;
		}

		return maxIndex;
	}

	private static double calcStdDev(List<BTCRate> rates) {
		if (rates.size() == 0)
			return 0.0;

		try {

			double avg = calcAvgForRates(rates);

			double var = 0.0;
			for (BTCRate rate : rates) {
				var = var + (Math.pow(rate.getValue() - avg, 2));
			}

			var = var * 1.0 / (((double) rates.size()) - 1.0);
			return Math.pow(var, 0.5);
		} catch (NumberFormatException exc) {
			return 0.0;
		}
	}

	public Period getLastPeriod() {
		synchronized (lock) {
			return lastPeriod;
		}
	}

	public Period getCurrentPeriod() {
		synchronized (lock) {
			return currentPeriod;
		}
	}
}
