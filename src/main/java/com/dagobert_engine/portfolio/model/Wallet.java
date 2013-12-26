package com.dagobert_engine.portfolio.model;

import com.dagobert_engine.core.model.CurrencyData;

/**
 * Wallet data
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
public class Wallet {
	private CurrencyData balance;
	private CurrencyData dailyWithdrawLimit;
	private CurrencyData maxWithdraw;
	private CurrencyData monthlyWithdrawLimit;
	private CurrencyData openOrders;
	private int operations;
	public CurrencyData getBalance() {
		return balance;
	}
	public void setBalance(CurrencyData balance) {
		this.balance = balance;
	}
	public CurrencyData getDailyWithdrawLimit() {
		return dailyWithdrawLimit;
	}
	public void setDailyWithdrawLimit(CurrencyData dailyWithdrawLimit) {
		this.dailyWithdrawLimit = dailyWithdrawLimit;
	}
	public CurrencyData getMaxWithdraw() {
		return maxWithdraw;
	}
	public void setMaxWithdraw(CurrencyData getMaxWithdraw) {
		this.maxWithdraw = getMaxWithdraw;
	}
	public CurrencyData getMonthlyWithdrawLimit() {
		return monthlyWithdrawLimit;
	}
	public void setMonthlyWithdrawLimit(CurrencyData getMonthlyWithdrawLimit) {
		this.monthlyWithdrawLimit = getMonthlyWithdrawLimit;
	}
	public CurrencyData getOpenOrders() {
		return openOrders;
	}
	public void setOpenOrders(CurrencyData getOpenOrders) {
		this.openOrders = getOpenOrders;
	}
	public int getOperations() {
		return operations;
	}
	public void setOperations(int operations) {
		this.operations = operations;
	}
	public Wallet(CurrencyData balance, CurrencyData dailyWithdrawLimit,
			CurrencyData getMaxWithdraw, CurrencyData getMonthlyWithdrawLimit,
			CurrencyData getOpenOrders, int operations) {
		super();
		this.balance = balance;
		this.dailyWithdrawLimit = dailyWithdrawLimit;
		this.maxWithdraw = getMaxWithdraw;
		this.monthlyWithdrawLimit = getMonthlyWithdrawLimit;
		this.openOrders = getOpenOrders;
		this.operations = operations;
	}
	
	public Wallet() {}
}
