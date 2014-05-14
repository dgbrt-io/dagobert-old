package com.dagobert_engine.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xeiam.xchange.dto.marketdata.Ticker;

public class Period {
	private Date from;
	private Date to;
	private List<Ticker> tickers;
	
	public Period() {
		tickers = new ArrayList<>();
	}

	/**
	 * Unweightened Average 
	 * @return
	 */
	public BigDecimal getUnweightenedAvg() {
		BigDecimal sum = new BigDecimal(0.0);
		
		for (Ticker t : tickers) {
			sum.add(t.getLast());
		}
		
		return sum.divide(new BigDecimal(tickers.size()));
	}
	
	/**
	 * Minimum 
	 * @return
	 */
	public Ticker getMin() {
		if (tickers.size() == 0)
			return null;
		
		Ticker minTicker = tickers.get(0);
		
		for (Ticker t : tickers) {
			if (t.getLast().compareTo(minTicker.getLast()) < 0) {
				minTicker = t;
			}
		}
		
		
		return minTicker;
	}

	/**
	 * Maximum 
	 * @return
	 */
	public Ticker getMax() {
		if (tickers.size() == 0)
			return null;
		
		Ticker maxTicker = tickers.get(0);
		
		for (Ticker t : tickers) {
			if (t.getLast().compareTo(maxTicker.getLast()) > 0) {
				maxTicker = t;
			}
		}
		
		
		return maxTicker;
	}
	
	public BigDecimal getVariance() {
		BigDecimal sum = new BigDecimal(0.0);
		BigDecimal avg = getUnweightenedAvg();
		
		for (Ticker t : tickers) {
			sum.add(t.getLast().subtract(avg).pow(2));
		}
		
		return sum.divide(new BigDecimal(tickers.size() - 1));
	}
	
	public BigDecimal getStdDev() {
		return new BigDecimal(Math.sqrt(getVariance().doubleValue()));
	}
	
	
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public List<Ticker> getTickers() {
		return tickers;
	}
	public void setTickers(List<Ticker> tickers) {
		this.tickers = tickers;
	}

	public Ticker getLastTicker() {
		return getTickers().get(getTickers().size() - 1);
	}
}
