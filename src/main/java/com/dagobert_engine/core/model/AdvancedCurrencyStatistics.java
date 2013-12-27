package com.dagobert_engine.core.model;

/**
 * Advanced currency statistics generated
 * by non-fast ticker
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
public class AdvancedCurrencyStatistics extends CurrencyStatistics {

	
	/**
	 * High price
	 */
	private CurrencyData high;
	
	/**
	 * Low price
	 */
	private CurrencyData low;
	
	/**
	 * Total avg
	 */
	private CurrencyData avg;
	
	/**
	 * Volume weightened Avg
	 */
	private CurrencyData volumeWeightenedAvg;
	
	/**
	 * Total trading volume
	 */
	private CurrencyData volume;

	public CurrencyData getHigh() {
		return high;
	}

	public void setHigh(CurrencyData high) {
		this.high = high;
	}

	public CurrencyData getLow() {
		return low;
	}

	public void setLow(CurrencyData low) {
		this.low = low;
	}

	public CurrencyData getAvg() {
		return avg;
	}

	public void setAvg(CurrencyData avg) {
		this.avg = avg;
	}

	public CurrencyData getVolumeWeightenedAvg() {
		return volumeWeightenedAvg;
	}

	public void setVolumeWeightenedAvg(CurrencyData volumeWeightenedAvg) {
		this.volumeWeightenedAvg = volumeWeightenedAvg;
	}

	public CurrencyData getVolume() {
		return volume;
	}

	public void setVolume(CurrencyData volume) {
		this.volume = volume;
	}
	
	
}
