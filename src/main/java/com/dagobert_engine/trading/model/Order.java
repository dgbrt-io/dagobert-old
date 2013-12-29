package com.dagobert_engine.trading.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.core.model.CurrencyType;


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
	
	@XmlElement
	private CurrencyType currency;
	
	@XmlElement
	private CurrencyType item;
	
	@XmlElement
	private OrderType type;
	
	@XmlElement
	private CurrencyData amount;
	
	@XmlElement
	private CurrencyData effectiveAmount;
	
	@XmlElement
	private CurrencyData invalidAmount;
	
	@XmlElement
	private CurrencyData price;
	
	@XmlElement
	private StatusType status;

	@XmlElement
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
	public CurrencyData getAmount() {
		return amount;
	}
	public void setAmount(CurrencyData amount) {
		this.amount = amount;
	}
	public CurrencyData getEffectiveAmount() {
		return effectiveAmount;
	}
	public void setEffectiveAmount(CurrencyData effectiveAmount) {
		this.effectiveAmount = effectiveAmount;
	}
	public CurrencyData getInvalidAmount() {
		return invalidAmount;
	}
	public void setInvalidAmount(CurrencyData invalidAmount) {
		this.invalidAmount = invalidAmount;
	}
	public CurrencyData getPrice() {
		return price;
	}
	public void setPrice(CurrencyData price) {
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
			CurrencyType item, OrderType type, CurrencyData amount,
			CurrencyData effectiveAmount, CurrencyData invalidAmount, CurrencyData price,
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
