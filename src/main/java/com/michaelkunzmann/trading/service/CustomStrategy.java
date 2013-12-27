package com.michaelkunzmann.trading.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import com.dagobert_engine.statistics.model.Period;
import com.dagobert_engine.trading.model.Order;
import com.dagobert_engine.trading.service.Strategy;
import com.dagobert_engine.trading.service.MtGoxTradeService.TrendType;

@Alternative
public class CustomStrategy implements Strategy {
	
	@Inject
	private Logger logger;
	

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

	public Order createOrder() {
		
		logger.log(Level.INFO, "CustomStrategy: createOrder()");
		
		return null;

//		
//		// Get BTC balance
//		final BigDecimal balanceBTC= portfolioService.getBalance(CurrencyType.BTC);
//		if (balanceBTC == null) {
//			
//			logger.log(Level.SEVERE, "There seems to be no BTC wallet");
//			return;
//		}
//
//		// fee
//		final double fee = currentPrice * portfolioService.getTradeFee(false);
//
//		logger.log(Level.INFO, "Current Price: " + currentPrice + ", avg="
//				+ currentPeriod.getAvgRate() + ", std dev="
//				+ currentPeriod.getStdDev() + ", fee=" + fee);
//
//
//		// Get trend
//		final TrendType trend = analyzeTrend(lastPeriod, currentPeriod);
//
//		logger.log(Level.INFO, "Trend: " + trend);
//
//		
//		boolean soldYet = lastSellOrder != null;
//		
//		if (soldYet){
//			logger.log(Level.INFO, "We have already sold yet. We have to buy.");
//			
//			
//			if (trend == TrendType.RISING) {
//				logger.log(Level.INFO, " ==> *************** I buy ***************");
//					
//					
//					try {
//						placeOrder(lastTrade, OrderAction.BUY, CurrencyType.USD, currencyToBtc(balanceUSD.doubleValue(), currentPrice), currentPrice);
//					} catch (AssetNotSupportedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//					logger.log(Level.INFO, "------------------------------------------------");
//					return;
//			}
//			else {
//				logger.log(Level.INFO, "Trend is not rising");
//				logger.log(Level.INFO, " ==> I don't don't buy");
//			}
//			
//		}
//		else {
//			logger.log(Level.INFO, "We have not sold yet. We have to sell.");
//			
//
//			if (trend == TrendType.FALLING) {
//				
//				
//				
//				double lastPricePlusFees = lastTrade.getBuyOrder().getPrice() * (1 + FEE_PERC);
//				if (currentPrice <= lastPricePlusFees) {
//					logger.log(Level.INFO, "currentPrice=" + currentPrice + " <=  " + lastPricePlusFees);
//					logger.log(Level.INFO, " ==> I don't sell");
//				}
//				else {
//					logger.log(Level.INFO, " ==> *************** I sell ***************");
//					
//					try {
//						placeOrder(lastTrade, OrderAction.SELL, CurrencyType.USD, balanceBTC.doubleValue(), currentPrice);
//					} catch (AssetNotSupportedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					logger.log(Level.INFO, "------------------------------------------------");
//					return;
//				}
//			}
//			else {
//				logger.log(Level.INFO, "Not selling, because trend is not falling");
//			}
//		}
//		logger.log(Level.INFO, "I do nothing");
	}
}
