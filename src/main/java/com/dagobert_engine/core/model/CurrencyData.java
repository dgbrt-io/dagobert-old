package com.dagobert_engine.core.model;


/**
 * Currency data.
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
public class CurrencyData {
	
	private double value;
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
