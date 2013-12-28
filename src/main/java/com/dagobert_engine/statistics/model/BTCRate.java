package com.dagobert_engine.statistics.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.dagobert_engine.core.model.CurrencyType;

/**
 * BTC Rate
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@XmlRootElement
public class BTCRate implements Serializable {
	private static final long serialVersionUID = 7183650153668161034L;

	public static final String RATE = "rate";
	public static final String RATES = "rates";
	
	/**
	 * Date time
	 */
	@XmlAttribute
	private Date time;
	
	/**
	 * Currency
	 */
	@XmlElement
	private CurrencyType currency;
	
	
	/**
	 * Period
	 */
	@XmlTransient
	@JsonIgnore
	private Period period;
	
	/**
	 * Value
	 */
	@XmlElement
	private double value;
	
	public Date getDateTime() {
		return time;
	}

	public void setDateTime(Date time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double dollars) {
		this.value = dollars;
	}
	
	public CurrencyType getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyType currency) {
		this.currency = currency;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}


	@Override
	public String toString() {
		return "AssetRate [dateTime=" + time + ", currency="
				+ currency + ", value=" + value + "]";
	}

	
	
}
