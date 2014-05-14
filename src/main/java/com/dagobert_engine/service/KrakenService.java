package com.dagobert_engine.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jboss.logging.Logger;

import com.dagobert_engine.model.CurrencyPairStatistics;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Ticker.TickerBuilder;
import com.xeiam.xchange.kraken.KrakenExchange;

@ApplicationScoped
public class KrakenService implements Service {
	
	public static String SERVICE_NAME = "kraken";

	private Logger logger = Logger.getLogger(getClass());

	private Map<String, BigDecimal> mockWallet;

	/**
	 * Configuration
	 */
	@Inject
	private Configuration config;

	/**
	 * Exchange
	 */
	private Exchange exchange;

	/**
	 * Data stores by curreny pair
	 */
	@JsonIgnore
	private Map<CurrencyPair, CurrencyPairStatistics> statistics;

	public KrakenService() {
		statistics = new HashMap<>();
		mockWallet = new HashMap<>();

	}

	@PostConstruct
	public void postContruct() {
		// Init mock wallet

		for (CurrencyPair pair : config.getTradedCurrencyPairs()) {
			mockWallet.put(pair.baseSymbol, new BigDecimal(0.0));
			mockWallet.put(pair.counterSymbol, new BigDecimal(100.0));
		}

		// Initialize data stores
		final int numberOfPeriods = config.getPeriods();
		final int periodLengthMinutes = config.getDefaultPeriodLength();

		List<CurrencyPair> pairs = config.getTradedCurrencyPairs();

		logger.info("Trading with the following currency pairs: "
				+ Arrays.toString(pairs.toArray()));
		for (CurrencyPair pair : pairs) {
			statistics.put(pair, new CurrencyPairStatistics(numberOfPeriods,
					periodLengthMinutes));
		}

		// Initialize exchange
		this.exchange = ExchangeFactory.INSTANCE
				.createExchange(KrakenExchange.class.getName());
		this.exchange.getExchangeSpecification().setApiKey(
				config.getKrakenApiKey());
		this.exchange.getExchangeSpecification().setSecretKey(
				config.getKrakenSecret());
		this.exchange.getExchangeSpecification().setUserName(
				config.getKrakenUsername());
		this.exchange.applySpecification(this.exchange
				.getExchangeSpecification());
	}

	@Override
	public Exchange getExchange() {
		return exchange;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}

	@Override
	public Map<CurrencyPair, CurrencyPairStatistics> getCurrencyPairStatistics() {
		return statistics;
	}

	public void setCurrencyPairStatistics(
			Map<CurrencyPair, CurrencyPairStatistics> statistics) {
		this.statistics = statistics;
	}

	@Override
	public void updateData() {

		for (CurrencyPair pair : this.statistics.keySet()) {

			try {
				Ticker newTicker = this.exchange.getPollingMarketDataService()
						.getTicker(pair);
				final TickerBuilder tickerBuilder = Ticker.TickerBuilder
						.newInstance();
				newTicker = tickerBuilder.withAsk(newTicker.getAsk())
						.withBid(newTicker.getBid())
						.withCurrencyPair(newTicker.getCurrencyPair())
						.withHigh(newTicker.getHigh())
						.withLast(newTicker.getLast())
						.withLow(newTicker.getLow())
						.withVolume(newTicker.getVolume())
						.withTimestamp(new Date()).build();

				this.statistics.get(pair).addTicker(newTicker);
			} catch (IOException exc) {
				logger.error(exc.toString());
			} catch (ExchangeException exc) {
				logger.warn(exc.getMessage());
			}
		}
	}

	@Override
	public String getName() {
		return SERVICE_NAME;
	}

	@Override
	public Map<String, Object> getInfo() {
		final Map<String, Object> info = new HashMap<>();
		info.put("name", SERVICE_NAME);
		// TODO: more info
		return info;
	}

	@Override
	public void buy(CurrencyPair pair, BigDecimal amount) {

		if (config.isMock()) {

			final BigDecimal counterBalance = mockWallet
					.get(pair.counterSymbol);
			final Ticker lastTicker = this.getCurrencyPairStatistics()
					.get(pair).getPeriods()[0].getLastTicker();
			final BigDecimal lastRate = lastTicker.getLast();
			final BigDecimal spentMoney = amount.multiply(lastRate);

			if (counterBalance.subtract(spentMoney).doubleValue() >= 0.0) {

				mockWallet.put(pair.baseSymbol, mockWallet.get(pair.baseSymbol)
						.add(amount));
				mockWallet.put(pair.counterSymbol,
						counterBalance.subtract(spentMoney));
			} else {
				throw new RuntimeException("Can't buy " + amount + " "
						+ pair.baseSymbol + ", you only have " + counterBalance
						+ " " + pair.counterSymbol + ", but you need " + spentMoney
						+ " " + pair.counterSymbol);
			}
		} else {
			// TODO:
		}
	}

	@Override
	public void sell(CurrencyPair pair, BigDecimal amount) {
		if (config.isMock()) {

			final BigDecimal baseBalance = mockWallet.get(pair.baseSymbol);

			if (baseBalance.subtract(amount).doubleValue() >= 0.0) {
				final Ticker lastTicker = this.getCurrencyPairStatistics()
						.get(pair).getPeriods()[0].getLastTicker();
				final BigDecimal lastRate = lastTicker.getLast();

				mockWallet.put(pair.baseSymbol, baseBalance.subtract(amount));
				mockWallet.put(pair.counterSymbol, amount.multiply(lastRate));
			} else {
				throw new RuntimeException("Can't sell " + amount + " "
						+ pair.baseSymbol + ", you only have " + baseBalance
						+ " " + pair.baseSymbol);
			}
		} else {
			// TODO:
		}
	}

}
