package com.dagobert_engine.core.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Currency data.
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@XmlRootElement
public class CurrencyData {
	
	@XmlElement
	private double value;
	
	@XmlElement
	private CurrencyType type;
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public CurrencyType getType() {
		return type;
	}
	public void setType(CurrencyType type) {
		this.type = type;
	}
	public CurrencyData(double value, CurrencyType type) {
		super();
		this.value = value;
		this.type = type;
	}

	public CurrencyData() {}
	@Override
	public String toString() {
		return "CurrencyData [value=" + value + ", type=" + type + "]";
	}
	
	
	
}
