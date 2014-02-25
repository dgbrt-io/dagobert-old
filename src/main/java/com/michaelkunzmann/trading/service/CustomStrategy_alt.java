package com.michaelkunzmann.trading.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import com.dagobert_engine.core.model.AdvancedCurrencyStatistics;
import com.dagobert_engine.core.model.Configuration;
import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.core.model.CurrencyType;
import com.dagobert_engine.portfolio.model.Transaction;
import com.dagobert_engine.portfolio.service.MtGoxPortfolioService;
//import com.dagobert_engine.portfolio.model.Transaction;
//import com.dagobert_engine.portfolio.service.MtGoxPortfolioService;
import com.dagobert_engine.statistics.model.Period;
import com.dagobert_engine.statistics.service.MtGoxStatisticsService;
import com.dagobert_engine.trading.model.Order;
import com.dagobert_engine.trading.model.Order.OrderType;
import com.dagobert_engine.trading.service.MtGoxTradeService;
import com.dagobert_engine.trading.service.Strategy;

/**
 * 
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 * 
 *          License http://www.apache.org/licenses/LICENSE-2.0
 * 
 */
@Alternative
public class CustomStrategy_alt implements Strategy {

	private static final double MIN_GROWTH_RATE = 0.005;
	private static final double MIN_ROI_PER_TRADE = 0.01;
	
	
	/**
	 * We NEVER sell at a price
	 */
	private final double GLUECKSKREUZER = 0.00001;

	@Inject
	private MtGoxPortfolioService portfolio;

	@Inject
	private MtGoxStatisticsService statistics;

	@Inject
	private MtGoxTradeService trading;

	@Inject
	private Configuration config;

	public enum TrendType {
		RISING, NORMAL, FALLING;
	}

	@Inject
	private Logger logger;

	private boolean enoughBtcToSell;

	private boolean enoughMoneyToBuy;

	/**
	 * Determines, if we have a positive or negative trend.
	 * 
	 * @param lastPeriod
	 * @param currentPeriod
	 * @return
	 */
	public TrendType analyzeTrend(Period lastPeriod, Period currentPeriod) {

		// Parameters
		final double lastPeriodAvg = lastPeriod.getAvgRate();
		final double currentPeriodAvg = currentPeriod.getAvgRate();

		// Calculate avg delta between both periods
		double avgDiff = currentPeriodAvg - lastPeriodAvg;

		double lastPeriodStdDev = lastPeriod.getStdDev();

		// Negative delta?
		if (avgDiff < 0.0) {

			// Make delta positive
			double positiveAvgDiff = avgDiff * -1.0;

			// Is delta greater than std dev?
			if (positiveAvgDiff >= lastPeriodStdDev) {
				// Negative trend
				return TrendType.FALLING;
			} else {
				// Normal trend
				return TrendType.NORMAL;
			}

		}

		// Positive delta?
		else if (avgDiff >= 0) {

			// Is delta greater than std dev?
			if (avgDiff >= lastPeriodStdDev) {
				// Positive trend
				return TrendType.RISING;
			} else {
				// Normal trend
				return TrendType.NORMAL;
			}
		} else {
			throw new RuntimeException(
					"Error calculating trend. Delta between last and current avg is invalid: "
							+ avgDiff);
		}

	}

	@Override
	public Order createOrder() {

		// Given
		final CurrencyType defaultCurrency = config.getDefaultCurrency();

		final AdvancedCurrencyStatistics stats = statistics
				.getAdvancedStatistics(defaultCurrency);
		final CurrencyData lastTrade = stats.getLastTradeDefaultCurrency();
		final double feePerc = portfolio.getTradeFee() / 100.0;

		final Transaction buyTransaction = portfolio
				.getLastBuyTransaction(CurrencyType.BTC);
		final Transaction sellTransaction = portfolio
				.getLastSellTransaction(CurrencyType.BTC);

		// Periods
		final Period lastPeriod = statistics.getLastPeriod();
		final Period currentPeriod = statistics.getCurrentPeriod();

		if (lastPeriod == null) {
			logger.info("Current period not finished yet. Waiting for current period to finish.");
			return null;
		}

		final TrendType trend = analyzeTrend(lastPeriod, currentPeriod);

		// Balances
		final CurrencyData balanceBtc = portfolio.getBalance(CurrencyType.BTC);
		final CurrencyData balanceCurr = portfolio.getBalance(defaultCurrency);

		List<Order> openOrders = trading.getOpenOrders();

		if (openOrders.size() > 0) {
			logger.log(Level.INFO, "There are still " + openOrders.size()
					+ " open orders by MtGox. Waiting until they're all done. ");
			return null;
		}

		enoughBtcToSell = trading.enoughBTCtoSell(balanceBtc.getValue());
		enoughMoneyToBuy = trading.enoughMoneyToBuy(balanceCurr.getValue(),
				lastTrade.getValue());
		
		
		// We only buy/sell if trend is normal
		if (trend != TrendType.NORMAL)
			return null;
		

		
		// Can we sell?
		if (enoughBtcToSell) {
				
				double sellPrice = lastTrade.getValue() - GLUECKSKREUZER;

				// Current rate rater higher than buy price + buy fee + sell fee?
				if (sellPrice > buyTransaction.getRate().getValue()
						* (1.0 + feePerc + MIN_ROI_PER_TRADE)) {
					Order order = new Order();
					order.setAmount(new CurrencyData(balanceBtc.getValue(),
							CurrencyType.BTC));
					order.setPrice(new CurrencyData(sellPrice,
							defaultCurrency));
					order.setType(OrderType.ASK);
					order.setCurrency(defaultCurrency);

					return order;
				}
		}
		
		// Can we buy?
		if (enoughMoneyToBuy) {
				double buyPrice = lastTrade.getValue() + GLUECKSKREUZER;

				boolean beyondLastSellPriceAndGrowingRate = buyPrice < sellTransaction.getRate().getValue() * (1.0 - feePerc - MIN_GROWTH_RATE);
				
				if (beyondLastSellPriceAndGrowingRate) {
					

					Order order = new Order();
					order.setAmount(new CurrencyData(balanceCurr.getValue()
							/ buyPrice, CurrencyType.BTC));
					order.setPrice(new CurrencyData(buyPrice,
							defaultCurrency));
					order.setType(OrderType.BID);
					order.setCurrency(defaultCurrency);
					return order;
				}
		}

		return null;
	}

	@Override
	public boolean isBuying() {
		return enoughMoneyToBuy;
	}

	@Override
	public boolean isSelling() {
		return enoughBtcToSell;
	}

	@Override
	public CurrencyData getLastBuyPrice() {
		return portfolio.getLastBuyTransaction(CurrencyType.BTC).getRate();
	}

	@Override
	public CurrencyData getLastSellPrice() {
		return portfolio.getLastSellTransaction(CurrencyType.BTC).getRate();
	}
}
