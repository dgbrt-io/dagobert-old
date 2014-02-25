package com.dagobert_engine.core.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

/**
 * MtGox config
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 * 
 *          License http://www.apache.org/licenses/LICENSE-2.0
 * 
 */
@ApplicationScoped
public class MtGoxConfiguration implements Serializable {
	private final String CONFIG_FILE = "META-INF/mtgox.properties";

	private static final long serialVersionUID = 8704544385151759493L;
	private String mtGoxPublicKey;
	private String mtGoxPrivateKey;
	private double minTradeAmount = 0.1;
	private Map<CurrencyType, Integer> divisionFactors;

	public MtGoxConfiguration() {
		super();
	}

	public void reload() {

		Properties props = new Properties();
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(CONFIG_FILE);

		try {
			props.load(is);
			mtGoxPublicKey = props.getProperty("mtGoxPublicKey");
			mtGoxPrivateKey = props.getProperty("mtGoxPrivateKey");
			minTradeAmount = Double.parseDouble(props.getProperty("minTradeAmount"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		divisionFactors = new HashMap<CurrencyType, Integer>();
		divisionFactors.put(CurrencyType.BTC, 100000000);
		divisionFactors.put(CurrencyType.USD, 100000);
		divisionFactors.put(CurrencyType.GBP, 100000);
		divisionFactors.put(CurrencyType.EUR, 100000);
		divisionFactors.put(CurrencyType.JPY, 1000);
		divisionFactors.put(CurrencyType.AUD, 100000);
		divisionFactors.put(CurrencyType.CAD, 100000);
		divisionFactors.put(CurrencyType.CHF, 100000);
		divisionFactors.put(CurrencyType.CNY, 100000);
		divisionFactors.put(CurrencyType.DKK, 100000);
		divisionFactors.put(CurrencyType.HKD, 100000);
		divisionFactors.put(CurrencyType.PLN, 100000);
		divisionFactors.put(CurrencyType.RUB, 100000);
		divisionFactors.put(CurrencyType.SEK, 1000);
		divisionFactors.put(CurrencyType.SGD, 100000);
		divisionFactors.put(CurrencyType.THB, 100000);

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
