package com.dagobert_engine.portfolio.model;

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
	private Currency balance;
	private Currency dailyWithdrawLimit;
	private Currency maxWithdraw;
	private Currency monthlyWithdrawLimit;
	private Currency openOrders;
	private int operations;
	public Currency getBalance() {
		return balance;
	}
	public void setBalance(Currency balance) {
		this.balance = balance;
	}
	public Currency getDailyWithdrawLimit() {
		return dailyWithdrawLimit;
	}
	public void setDailyWithdrawLimit(Currency dailyWithdrawLimit) {
		this.dailyWithdrawLimit = dailyWithdrawLimit;
	}
	public Currency getMaxWithdraw() {
		return maxWithdraw;
	}
	public void setMaxWithdraw(Currency getMaxWithdraw) {
		this.maxWithdraw = getMaxWithdraw;
	}
	public Currency getMonthlyWithdrawLimit() {
		return monthlyWithdrawLimit;
	}
	public void setMonthlyWithdrawLimit(Currency getMonthlyWithdrawLimit) {
		this.monthlyWithdrawLimit = getMonthlyWithdrawLimit;
	}
	public Currency getOpenOrders() {
		return openOrders;
	}
	public void setOpenOrders(Currency getOpenOrders) {
		this.openOrders = getOpenOrders;
	}
	public int getOperations() {
		return operations;
	}
	public void setOperations(int operations) {
		this.operations = operations;
	}
	public Wallet(Currency balance, Currency dailyWithdrawLimit,
			Currency getMaxWithdraw, Currency getMonthlyWithdrawLimit,
			Currency getOpenOrders, int operations) {
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
