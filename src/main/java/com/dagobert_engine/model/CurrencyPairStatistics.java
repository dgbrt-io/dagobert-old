package com.dagobert_engine.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.xeiam.xchange.dto.marketdata.Ticker;

public class CurrencyPairStatistics {
	
	@JsonIgnore
	private Object semaphore = new Object();
	
	private Period[] periods;
	private int numberOfPeriods;
	private int periodLengthMinutes;
	
	public CurrencyPairStatistics(int numberOfPeriods, int periodLengthMinutes) {
		this.periods = new Period[numberOfPeriods];
		this.numberOfPeriods = numberOfPeriods;
		this.periodLengthMinutes = periodLengthMinutes;
	}
	
	public void clear() {
		synchronized (semaphore) {
			this.periods = new Period[this.numberOfPeriods];
		}
	}
	
	public Period[] getPeriods() {
		synchronized(semaphore) {
			return periods.clone();
		}
	}	
	
	public List<Period> getAvailablePeriods() {
		final List<Period> ret = new ArrayList<Period>();

		synchronized(semaphore) {
			for (int i = 0; i < periods.length; i++) {
				if (periods[i] != null) {
					ret.add(periods[i]);
				}
			}
		}
		return ret;
	}
	
	public void addTicker(Ticker ticker) {
		synchronized (semaphore) {
			final long ONE_MINUTE_IN_MILLIS = 60000L;
			final Date now = new Date();
			final long nowMillis = now.getTime();
			
			// Initial period
			if (periods[0] == null) {
				periods[0] = new Period();
				periods[0].setFrom(now);
				periods[0].setTo(new Date(nowMillis + (long) (periodLengthMinutes * ONE_MINUTE_IN_MILLIS)));
				
			}
			
			// Move periods if period is over and create new period
			if (now.after(periods[0].getTo())) {
				
				for (int i = (periods.length - 2); i >= 0; i--) {
					periods[i + 1] = periods[i];
				}
				
				periods[0] = new Period();
				periods[0].setFrom(periods[1].getTo());
				periods[0].setTo(new Date(periods[0].getFrom().getTime() 
								+ (long) (periodLengthMinutes * ONE_MINUTE_IN_MILLIS)));
			}
	
			// Add to latest period
			periods[0].getTickers().add(ticker);
		}
		
		
	}
	
	public int getNumberOfPeriods() {
		return numberOfPeriods;
	}

	public void setNumberOfPeriods(int numberOfPeriods) {
		this.numberOfPeriods = numberOfPeriods;
	}

	public int getPeriodLengthMinutes() {
		return periodLengthMinutes;
	}

	public void setPeriodLengthMinutes(int periodLengthMinutes) {
		this.periodLengthMinutes = periodLengthMinutes;
	}

	@Override
	public String toString() {
		synchronized (semaphore) {
			String output = "==== Datastore ====\n";
			
			for (int p = 0; p < periods.length; p++) {
				output += " > Period " + p + ":\n";
				
				if (periods[p] != null) {
					for (int t = 0; t < periods[p].getTickers().size(); t++) {
						Ticker ticker = periods[p].getTickers().get(t);
						output += "    * Ticker " + t + ": " + ticker.toString() + "\n";
					}
				}
				else {
					output += "    --- Empty ---\n";
				}
			}
			
			output += "================\n";
			return output;
		}
	}
}
