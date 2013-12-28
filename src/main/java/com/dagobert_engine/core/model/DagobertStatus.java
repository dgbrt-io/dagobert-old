package com.dagobert_engine.core.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@XmlRootElement(name = "status")
public class DagobertStatus {
	
	@XmlAttribute
	private Date time;
	
	@XmlElement
	private boolean isRunning;
	
	@XmlElement
	private int defaultPeriodLength;
	
	@XmlElement
	private CurrencyType defaultCurrency;
	
	@XmlElement
	private String lag;

	public DagobertStatus() {
		// TODO Auto-generated constructor stub
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public int getDefaultPeriodLength() {
		return defaultPeriodLength;
	}

	public void setDefaultPeriodLength(int defaultPeriodLength) {
		this.defaultPeriodLength = defaultPeriodLength;
	}

	public CurrencyType getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(CurrencyType defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public String getLag() {
		return lag;
	}

	public void setLag(String lag) {
		this.lag = lag;
	}

	
}
