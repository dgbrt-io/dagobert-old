package com.dagobert_engine.statistics.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.dagobert_engine.config.service.ConfigService;
import com.dagobert_engine.config.util.KeyName;
import com.dagobert_engine.core.service.MtGoxApiAdapter;
import com.dagobert_engine.statistics.model.BTCRate;
import com.dagobert_engine.statistics.model.Period;
import com.dagobert_engine.statistics.model.Period.PropabilityType;
import com.dagobert_engine.trading.model.CurrencyType;

/**
 * StatisticsService
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@Singleton
@Startup
public class StatisticsService implements Serializable {

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

	@Inject
	private Logger logger;

	private Period currentPeriod = null;
	private Period lastPeriod = null;

	private final Object lock = new Object();

	/**
	 * 
	 * returns the string used to get the Ticker
	 * 
	 * @return the string you're searching for;)
	 */
	private static final String getTickerPath(CurrencyType cur, boolean fast) {
		return "BTC" + cur.toString() + "/MONEY/TICKER" + (fast ? "_FAST" : "");
	}

	private BigDecimal getLastPrice(CurrencyType cur) throws ParseException {
		if (cur == null) {
			throw new IllegalArgumentException("CurrencyType mustn't be null");
		}

		String urlPath = getTickerPath(cur, true);
		// long divideFactor =
		// adapter.getDivisionFactors().get(cur).longValue();
		HashMap<String, String> query_args = new HashMap<>();

		/*
		 * Params : No params required
		 */
		String queryResult = adapter.query(urlPath, query_args);

		/*
		 * Result sample :{ "result":"success", "data": { "high": **Currency
		 * Object - USD**, "low": **Currency Object - USD**, "avg": **Currency
		 * Object - USD**, "vwap": **Currency Object - USD**, "vol": **Currency
		 * Object - BTC**, "last_local": **Currency Object - USD**, "last_orig":
		 * **Currency Object - ???**, "last_all": **Currency Object - USD**,
		 * "last": **Currency Object - USD**, "buy": **Currency Object - USD**,
		 * "sell": **Currency Object - USD**, "now": "1364689759572564" }}
		 */

		try {
			JSONParser parser = new JSONParser();
			BigDecimal last = null;
			JSONObject httpAnswerJson = (JSONObject) (parser.parse(queryResult));
			JSONObject dataJson = (JSONObject) httpAnswerJson.get("data");
			JSONObject lastJson = (JSONObject) dataJson.get("last");
			String last_String = (String) lastJson.get("value");
			last = new BigDecimal(Double.parseDouble(last_String));
			return last;
		} catch (Exception exc) {
			logger.log(Level.WARNING, exc.getMessage());
			return null;
		}
	}

	public void refreshRates() throws ParseException {
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
			rate.setCurrency(CurrencyType.USD);
			rate.setValue(getLastPrice(rate.getCurrency()).doubleValue());

			// Add rate to current period
			rate.setPeriod(currentPeriod);
			currentPeriod.getRates().add(rate);

			// calculate values for period

			currentPeriod.setAvgRate(getAvg(currentPeriod.getRates()));
			currentPeriod.setLatestRate(rate);

			BTCRate maxRate = currentPeriod.getRates().get(
					getMaxIndex(currentPeriod.getRates()));
			BTCRate minRate = currentPeriod.getRates().get(
					getMinIndex(currentPeriod.getRates()));
			currentPeriod.setMaxRate(maxRate);
			currentPeriod.setMaxRate(minRate);

			currentPeriod.setStdDev(getStdDev(currentPeriod.getRates()));
			
			currentPeriod.setPropDown(getPropability(currentPeriod.getAvgRate(), currentPeriod.getStdDev(), PropabilityType.LESS_THAN, currentPeriod.getLatestRate().getValue()));
			currentPeriod.setPropUp(getPropability(currentPeriod.getAvgRate(), currentPeriod.getStdDev(), PropabilityType.GREATER_THAN, currentPeriod.getLatestRate().getValue()));
		}
	}

	private static double getPropability(double avg, double dev,
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

	private static double getAvg(List<BTCRate> rates) {
		double avg = 0.0;

		for (BTCRate rate : rates) {
			avg += rate.getValue();
		}

		if (rates.size() > 0)
			avg = avg / ((double) rates.size());

		return avg;
	}

	private static int getMinIndex(List<BTCRate> rates) {
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

	private static int getMaxIndex(List<BTCRate> rates) {
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

	private static double getStdDev(List<BTCRate> rates) {
		if (rates.size() == 0)
			return 0.0;

		try {

			double avg = getAvg(rates);

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
