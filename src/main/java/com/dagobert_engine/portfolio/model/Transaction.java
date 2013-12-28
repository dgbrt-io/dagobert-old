package com.dagobert_engine.portfolio.model;

import java.util.Date;

import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.core.model.CurrencyType;

/**
 * History record of the portfolio's history
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
public class Transaction implements Comparable<Transaction> {
	
	/**
	 * Category of transaction
	 * 
	 * @author Michael Kunzmann (mail@michaelkunzmann.com)
	 * @version 0.1-ALPHA
	 *
	 * License http://www.apache.org/licenses/LICENSE-2.0
	 *
	 */
	public enum TransactionCategory {
		
		MONEY_BITCOIN_BLOCK_TX_OUT("Money_Bitcoin_Block_Tx_Out"),
		MONEY_TRADE("Money_Trade");
		
		private String link;
		
		private TransactionCategory(String link) {
			this.link = link;
		}
		
		public String getLink() {
			return link;
		}
		
		public static TransactionCategory forLink(String link) {
			
			for (TransactionCategory cat : TransactionCategory.values()) {
				if (cat.getLink().equals(link))
					return cat;
			}
			return null;
		}
		
	}
	
	/**
	 * Type of record
	 * 
	 * 
	 * @author Michael Kunzmann (mail@michaelkunzmann.com)
	 * @version 0.1-ALPHA
	 *
	 * License http://www.apache.org/licenses/LICENSE-2.0
	 *
	 */
	public enum RecordType {
		
		// bid order
		SPENT,
		IN,
		
		// ask order
		EARNED,
		OUT,
		
		// both
		FEE,
		
		//other
		DEPOSIT,
		WITHDRAW
	}

	public Transaction() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Transaction type
	 */
	private Date time;
	

	/**
	 * The rate
	 */
	private CurrencyData rate;
	
	/**
	 * The amount transferred
	 */
	private CurrencyData value;
	
	/**
	 * the balance immediately following this transaction
	 */
	private CurrencyData balance;
	
	/**
	 * Info text
	 */
	private String info;
	
	/**
	 * The transaction UUID
	 */
	private String transactionUuid;
	
	/**
	 * Category of transaction
	 */
	private TransactionCategory transactionCategory;
	
	/**
	 * A specific identifier
	 */
	private String identifier;
	
	/**
	 * record type
	 */
	private RecordType type;
	
	/**
	 * Currency
	 */
	private CurrencyType currency;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public CurrencyData getValue() {
		return value;
	}

	public void setValue(CurrencyData value) {
		this.value = value;
	}

	public CurrencyData getBalance() {
		return balance;
	}

	public void setBalance(CurrencyData balance) {
		this.balance = balance;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTransactionUuid() {
		return transactionUuid;
	}

	public void setTransactionUuid(String transactionUuid) {
		this.transactionUuid = transactionUuid;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public RecordType getType() {
		return type;
	}

	public void setType(RecordType type) {
		this.type = type;
	}

	public TransactionCategory getTransactionCategory() {
		return transactionCategory;
	}

	public void setTransactionCategory(TransactionCategory transactionCategory) {
		this.transactionCategory = transactionCategory;
	}
	

	@Override
	public int compareTo(Transaction o) {
		if (this.getTime().equals(o.getTime())) return 0;
		
		if (this.time.before(o.time)) return -1;
		else
			return 1;
	}

	public CurrencyData getRate() {
		return rate;
	}

	public void setRate(CurrencyData rate) {
		this.rate = rate;
	}

	public CurrencyType getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyType currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Transaction [time=" + time + ", rate=" + rate + ", value="
				+ value + ", balance=" + balance + ", info=" + info
				+ ", transactionUuid=" + transactionUuid
				+ ", transactionCategory=" + transactionCategory
				+ ", identifier=" + identifier + ", type=" + type
				+ ", currency=" + currency + "]";
	}
	
	
	
}
