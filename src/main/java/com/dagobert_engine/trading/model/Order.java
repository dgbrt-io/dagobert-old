package com.dagobert_engine.trading.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.dagobert_engine.portfolio.model.Currency;


/**
 * Order
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@XmlRootElement
public class Order {
	
	// Sample JSON
	//{
	//  "oid": "abc123-def456-..",
	//  "currency": "USD",
	//  "item": "BTC",
	//  "type": "bid",
	//  "amount":           **Currency Object**,
	//  "effective_amount": **Currency Object**,
	//  "invalid_amount":   **Currency Object**,
	//  "price":            **Currency Object**,
	//  "status": "pending",
	//  "date": 1365517594,
	//  "priority": "1365517594817935",
	//  "actions": []
	//}
		
	public enum StatusType {
		 PENDING, EXECUTING, POST_PENDING, OPEN, STOP, INVALID; 
	}
	
	public enum OrderType {
		BID,
		ASK
	}
	
	@XmlAttribute
	private String orderId;
	
	@XmlAttribute
	private Date date;
	private CurrencyType currency;
	private CurrencyType item;
	private OrderType type;
	private Currency amount;
	private Currency effectiveAmount;
	private Currency invalidAmount;
	private Currency price;
	private StatusType status;
	private Long priority;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public CurrencyType getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyType currency) {
		this.currency = currency;
	}
	public CurrencyType getItem() {
		return item;
	}
	public void setItem(CurrencyType item) {
		this.item = item;
	}
	public Currency getAmount() {
		return amount;
	}
	public void setAmount(Currency amount) {
		this.amount = amount;
	}
	public Currency getEffectiveAmount() {
		return effectiveAmount;
	}
	public void setEffectiveAmount(Currency effectiveAmount) {
		this.effectiveAmount = effectiveAmount;
	}
	public Currency getInvalidAmount() {
		return invalidAmount;
	}
	public void setInvalidAmount(Currency invalidAmount) {
		this.invalidAmount = invalidAmount;
	}
	public Currency getPrice() {
		return price;
	}
	public void setPrice(Currency price) {
		this.price = price;
	}
	public StatusType getStatus() {
		return status;
	}
	public void setStatus(StatusType status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getPriority() {
		return priority;
	}
	public void setPriority(Long priority) {
		this.priority = priority;
	}
	
	public OrderType getType() {
		return type;
	}
	public void setType(OrderType type) {
		this.type = type;
	}
	public Order() {
		
		
	}
	public Order(String orderId, Date date, CurrencyType currency,
			CurrencyType item, OrderType type, Currency amount,
			Currency effectiveAmount, Currency invalidAmount, Currency price,
			StatusType status, Long priority) {
		super();
		this.orderId = orderId;
		this.date = date;
		this.currency = currency;
		this.item = item;
		this.type = type;
		this.amount = amount;
		this.effectiveAmount = effectiveAmount;
		this.invalidAmount = invalidAmount;
		this.price = price;
		this.status = status;
		this.priority = priority;
	}
	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", date=" + date + ", currency="
				+ currency + ", item=" + item + ", type=" + type + ", amount="
				+ amount + ", effectiveAmount=" + effectiveAmount
				+ ", invalidAmount=" + invalidAmount + ", price=" + price
				+ ", status=" + status + ", priority=" + priority + "]";
	}

	
	
}
