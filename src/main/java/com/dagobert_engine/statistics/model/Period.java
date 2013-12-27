package com.dagobert_engine.statistics.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Period
 * 
 * @author Michael
 */
@XmlRootElement
public class Period {
	public enum PropabilityType {
		GREATER_THAN, LESS_THAN
	}
	

	public static final String PERIOD = "period";
	public static final String PERIODS = "periods";
	
	/**
	 * Rates of the period
	 */
	@XmlElementWrapper(name = "rates")
	@XmlElement(name = "rate")
	private List<BTCRate> rates = new ArrayList<>();
	
	/**
	 * Start time
	 */
	@XmlAttribute
	private Date fromTime;
	
	/**
	 * End time
	 */
	@XmlAttribute
	private Date toTime;

	/**
	 * Average rate of the period
	 */
	@XmlElement
	private double avgRate;

	/**
	 * Standard deviation of the period
	 */
	@XmlElement
	private double stdDev;
	

	/**
	 * Propability that the rate goes up in the next period
	 */
	@XmlElement
	private double propUp;
	
	/**
	 * Propability that the rate goes down in the next period
	 */
	@XmlElement
	private double propDown;
	
	/**
	 * Latest rate
	 */
	@XmlElement
	private BTCRate latestRate;


	@XmlElement
	private BTCRate minRate;

	@XmlElement
	private BTCRate maxRate;
	

	public double getPropUp() {
		return propUp;
	}

	public void setPropUp(double propUp) {
		this.propUp = propUp;
	}

	public double getPropDown() {
		return propDown;
	}

	public void setPropDown(double propDown) {
		this.propDown = propDown;
	}
	public List<BTCRate> getRates() {
		return rates;
	}

	public void setRates(List<BTCRate> rates) {
		this.rates = rates;
	}

	public double getAvgRate() {
		return avgRate;
	}

	public void setAvgRate(double avg) {
		this.avgRate = avg;
	}

	public double getStdDev() {
		return stdDev;
	}

	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}

	public BTCRate getLatestRate() {
		return latestRate;
	}

	public void setLatestRate(BTCRate latest) {
		this.latestRate = latest;
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date from) {
		this.fromTime = from;
	}

	public Date getToTime() {
		return toTime;
	}

	public void setToTime(Date to) {
		this.toTime = to;
	}

	public BTCRate getMinRate() {
		return minRate;
	}

	public void setMinRate(BTCRate minRate) {
		this.minRate = minRate;
	}

	public BTCRate getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(BTCRate maxRate) {
		this.maxRate = maxRate;
	}
}
