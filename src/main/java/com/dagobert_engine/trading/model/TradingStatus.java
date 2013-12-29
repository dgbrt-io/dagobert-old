package com.dagobert_engine.trading.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.dagobert_engine.core.model.CurrencyData;

@XmlRootElement
public class TradingStatus {
	@XmlAttribute
	private Date time;
	
	private boolean isBuying;
	private boolean isSelling;
	
	private CurrencyData lastBuyPrice;
	private CurrencyData lastSellPrice;
	private CurrencyData minimumSellPrice;
	private CurrencyData lastEarnings;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public boolean isBuying() {
		return isBuying;
	}
	public void setBuying(boolean isBuying) {
		this.isBuying = isBuying;
	}
	public boolean isSelling() {
		return isSelling;
	}
	public void setSelling(boolean isSelling) {
		this.isSelling = isSelling;
	}
	public CurrencyData getLastBuyPrice() {
		return lastBuyPrice;
	}
	public void setLastBuyPrice(CurrencyData lastBuyPrice) {
		this.lastBuyPrice = lastBuyPrice;
	}
	public CurrencyData getLastSellPrice() {
		return lastSellPrice;
	}
	public void setLastSellPrice(CurrencyData lastSellPrice) {
		this.lastSellPrice = lastSellPrice;
	}
	public CurrencyData getMinimumSellPrice() {
		return minimumSellPrice;
	}
	public void setMinimumSellPrice(CurrencyData minimumSellPrice) {
		this.minimumSellPrice = minimumSellPrice;
	}
	public CurrencyData getLastEarnings() {
		return lastEarnings;
	}
	public void setLastEarnings(CurrencyData lastEarnings) {
		this.lastEarnings = lastEarnings;
	}
	
	
}
