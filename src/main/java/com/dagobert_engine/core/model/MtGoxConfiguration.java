package com.dagobert_engine.core.model;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

/**
 * MtGox config
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@ApplicationScoped
public class MtGoxConfiguration implements Serializable {

	private static final long serialVersionUID = 8704544385151759493L;
	private String mtGoxPublicKey;
	private String mtGoxPrivateKey;
	private double minTradeAmount;
	private Map<CurrencyType, Integer> divisionFactors;

	public MtGoxConfiguration() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMtGoxPublicKey() {
		return mtGoxPublicKey;
	}

	public void setMtGoxPublicKey(String mtGoxPublicKey) {
		this.mtGoxPublicKey = mtGoxPublicKey;
	}

	public String getMtGoxPrivateKey() {
		return mtGoxPrivateKey;
	}

	public void setMtGoxPrivateKey(String mtGoxPrivateKey) {
		this.mtGoxPrivateKey = mtGoxPrivateKey;
	}

	public double getMinTradeAmount() {
		return minTradeAmount;
	}

	public void setMinTradeAmount(double minTradeAmount) {
		this.minTradeAmount = minTradeAmount;
	}

	public Map<CurrencyType, Integer> getDivisionFactors() {
		return divisionFactors;
	}

	public void setDivisionFactors(Map<CurrencyType, Integer> divisionFactors) {
		this.divisionFactors = divisionFactors;
	}

}
