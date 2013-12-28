package com.dagobert_engine.core.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Information about a currency
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@XmlRootElement
public class CurrencyInfo {
	public enum CurrencySymbolPosition {
		BEFORE,
		AFTER;
	}
	
	// Example JSON
//	{
//        "currency":"USD",
//        "name":"Dollar",
//        "symbol":"$",
//        "decimals":"5",
//        "display_decimals":"2",
//        "symbol_position":"before",
//        "virtual":"N",
//        "ticker_channel":"abc123-def456",
//        "depth_channel":"abc123-def456"
//    }

	@XmlElement
	private CurrencyType currency;
	
	@XmlElement
	private String name;
	
	@XmlElement
	private String symbol;
	
	@XmlElement
	private int decimals;
	
	@XmlElement
	private int decimalsDisplayed;
	
	@XmlElement
	private CurrencySymbolPosition symbolPosition;
	
	@XmlElement
	private String virtual;
	
	@XmlElement
	private String tickerChannelId;
	
	@XmlElement
	private String depthChannelId;
	
	public CurrencyInfo() {}
	
	public CurrencyInfo(CurrencyType currency, String name, String symbol,
			int decimals, int decimalsDisplayed,
			CurrencySymbolPosition symbolPosition, String virtual,
			String tickerChannelId, String depthChannelId) {
		super();
		this.currency = currency;
		this.name = name;
		this.symbol = symbol;
		this.decimals = decimals;
		this.decimalsDisplayed = decimalsDisplayed;
		this.symbolPosition = symbolPosition;
		this.virtual = virtual;
		this.tickerChannelId = tickerChannelId;
		this.depthChannelId = depthChannelId;
	}



	public CurrencyType getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyType currency) {
		this.currency = currency;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getDecimals() {
		return decimals;
	}
	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}
	public int getDecimalsDisplayed() {
		return decimalsDisplayed;
	}
	public void setDecimalsDisplayed(int decimalsDisplayed) {
		this.decimalsDisplayed = decimalsDisplayed;
	}
	public CurrencySymbolPosition getSymbolPosition() {
		return symbolPosition;
	}
	public void setSymbolPosition(CurrencySymbolPosition symbolPosition) {
		this.symbolPosition = symbolPosition;
	}
	public String getVirtual() {
		return virtual;
	}
	public void setVirtual(String virtual) {
		this.virtual = virtual;
	}
	public String getTickerChannelId() {
		return tickerChannelId;
	}
	public void setTickerChannelId(String tickerChannelId) {
		this.tickerChannelId = tickerChannelId;
	}
	public String getDepthChannelId() {
		return depthChannelId;
	}
	public void setDepthChannelId(String depthChannelId) {
		this.depthChannelId = depthChannelId;
	}
	
	
}
