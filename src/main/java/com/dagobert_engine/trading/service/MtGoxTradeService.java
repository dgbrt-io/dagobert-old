package com.dagobert_engine.trading.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dagobert_engine.config.service.ConfigService;
import com.dagobert_engine.config.util.KeyName;
import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.core.model.CurrencyType;
import com.dagobert_engine.core.service.MtGoxApiAdapter;
import com.dagobert_engine.core.util.MtGoxException;
import com.dagobert_engine.core.util.MtGoxQueryUtil;
import com.dagobert_engine.core.util.QueryArgBuilder;
import com.dagobert_engine.trading.model.Order;
import com.dagobert_engine.trading.model.Order.StatusType;

/**
 * Trade service
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@ApplicationScoped
public class MtGoxTradeService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7420672734666974678L;


	public enum MtGoxStatus {
		RUNNING, NOT_RUNNING;
	}

	public enum ResultType {
		SUCCESS, FAILURE
	}
	

	public enum OrderAction {
		BUY, SELL;
	}

	// Paths
	private final String API_ADD_ORDER = "MONEY/ORDER/ADD";
	private final String API_CANCEL_ORDER = "MONEY/ORDER/CANCEL";
	private final String API_MONEY_ORDERS = "MONEY/ORDERS";

	@Inject
	private MtGoxApiAdapter adapter;
	
	@Inject
	private Strategy strategy;
	
	@Inject
	private ConfigService configService;

	@Inject
	private Logger logger;

	@PostConstruct
	private void postConstruct() throws Exception {
	}

	public static final String API_MONEY_ORDER_QUOTE = "MONEY/ORDER/QUOTE";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_AMOUNT_INT = "amount_int";
	public static final String PARAM_PRICE_INT = "price_int";
	
	private JSONParser parser = new JSONParser();
	
	public boolean enoughMoneyToBuy(double balance, double currentPrice) {
		return Double.parseDouble(configService.getProperty(KeyName.MIN_TRADE_AMOUNT)) <= currencyToBtc(balance, currentPrice);
	}
	
	/**
	 * Transforms a json object to a json object
	 * 
	 * @param obj
	 * @return
	 */
	private CurrencyData getCurrencyForJsonObj(JSONObject obj) {
		if (obj == null) {
			return null;
		}

		CurrencyData curr = new CurrencyData();
		curr.setType(CurrencyType.valueOf((String) obj.get("currency")));
		double value = 
				Double.parseDouble((String) obj.get("value_int")) / adapter.getDivisionFactors().get(curr.getType());
		curr.setValue(value);
		return curr;
	}
	
	/**
	 * Cancel a trade order
	 * 
	 * @param order
	 * @return
	 */
	public boolean cancelOrder(Order order) {
		
		String url = MtGoxQueryUtil.create(order.getCurrency(), API_CANCEL_ORDER);
		
		String resultJson = adapter.query(url, QueryArgBuilder.create().add("oid", order.getOrderId()).build());
		
		JSONObject root;
		try {
			root = (JSONObject) parser.parse(resultJson);
			String result = (String) root.get("result");
			
			if (!"success".equals(result)) {
				throw new MtGoxException(result);
			}
			
			return true;
		} catch (ParseException e) {
			throw new MtGoxException(e);
		}
	}
	
	// TODO MONEY/ORDER/RESULT
	// TODO MONEY/TRADES/FETCH (only get http method !!!!!)
	// TODO MONEY/TRADES/CANCELLED (is not implemented by mtgox
	
	
	
	/**
	 * Get an up-to-date quote for a bid or ask transaction
	 * 
	 * @param curr
	 * @param type
	 * @param amount
	 * @return
	 */
	public double getQuote(CurrencyType curr, Order.OrderType type, double amount) {
		
		String url = MtGoxQueryUtil.create(curr, API_MONEY_ORDER_QUOTE);
		
		
		String resultJSON = adapter.query(url, QueryArgBuilder.create().add("type", type.name().toLowerCase()).add("amount", "" + (int) amount * adapter.getDivisionFactors().get(curr)).build());
		
		try {
			JSONObject root = (JSONObject) parser.parse(resultJSON);
			
			String result = (String) root.get("result");
			
			if (!"success".equals(result)) {
				throw new MtGoxException(result);
			}
			
			JSONObject data = (JSONObject) root.get("data");
			String amountStr = (String) data.get("amount");
			
			return Double.parseDouble(amountStr) / adapter.getDivisionFactors().get(curr);			
		} catch (ParseException e) {
			throw new MtGoxException(e);
		}
	}
	
	public List<Order> getOpenOrders() {
		logger.log(Level.INFO, "Getting open orders...");
		
		ArrayList<Order> orders = new ArrayList<Order>();
		
		CurrencyType currency = CurrencyType.valueOf(configService.getProperty(KeyName.DEFAULT_CURRENCY));
		
		try {
			String resultJson = adapter.query(MtGoxQueryUtil.create(currency, API_MONEY_ORDERS));
			
			JSONObject root = (JSONObject) parser.parse(resultJson);
			String result = (String) root.get("result");
			
			if (!"success".equals(result)) {
				throw new MtGoxException(result);
			}
			
			JSONArray data = (JSONArray) root.get("data");
			
			for (int i = 0; i < data.size(); i++) {
				JSONObject orderJson = (JSONObject) data.get(i);
				
				Order order = new Order();
				order.setOrderId((String) orderJson.get("oid"));
				order.setItem(CurrencyType.valueOf((String) orderJson.get("item")));
				order.setType(Order.OrderType.valueOf(((String) orderJson.get("type")).toUpperCase()));
				order.setAmount(getCurrencyForJsonObj((JSONObject) orderJson.get("amount")));
				order.setEffectiveAmount(getCurrencyForJsonObj((JSONObject) orderJson.get("effective_amount")));
				order.setInvalidAmount(getCurrencyForJsonObj((JSONObject) orderJson.get("invalid_amount")));
				order.setPrice(getCurrencyForJsonObj((JSONObject) orderJson.get("price")));
				order.setStatus(StatusType.valueOf(((String) orderJson.get("status")).toUpperCase()));
				order.setDate(new Date((long) orderJson.get("date") * 1000));
				order.setPriority(Long.parseLong((String) orderJson.get("priority")));
				
				orders.add(order);
				
			}
		}
		catch (Exception exc) {
			throw new MtGoxException(exc);
		}
		
		return orders;
	}
	

	/**
	 * Am I able to sell?
	 * 
	 * @param balance
	 * @return
	 */
	public boolean enoughBTCtoSell(double balance) {
		return Double.parseDouble(configService.getProperty(KeyName.MIN_TRADE_AMOUNT)) <= balance;
	}

	public void trade() {
		
		logger.log(Level.INFO, "Using " + strategy.getClass().getName() + " implementation...");

		Order orderGiven = strategy.createOrder();
		
		if (orderGiven != null) {
			placeOrder(orderGiven);
		}
	}

	/**
	 * Calculates the max BTC amount we can buy
	 * 
	 * @param balanceUSD
	 * @param price
	 * @param feePerc
	 * @return
	 */
	private static double currencyToBtc(double currency, double price) {
		return currency / price;
	}

	
	public Order placeOrder(Order orderGiven) {
		if (orderGiven == null) {
			throw new IllegalArgumentException(
					"order must be given");
		}

		if (orderGiven.getCurrency() == null) {
			throw new IllegalArgumentException(
					"Currency must be set to buy the given asset");
		}
		
		if (orderGiven.getPrice() == null) {
			throw new IllegalArgumentException("Price must be set to place order.");
		}
		

		if (orderGiven.getCurrency() == null) {
			throw new IllegalArgumentException("Currency for order must be set to place order.");
		}

		if (orderGiven.getAmount().getValue() <  Double.parseDouble(configService.getProperty(KeyName.MIN_TRADE_AMOUNT)) ) {
			throw new IllegalArgumentException(
					"Amount must be greater or equal than MINIMUM_BTC_TRADE_SIZE ("
							+ Double.parseDouble(configService.getProperty(KeyName.MIN_TRADE_AMOUNT)) + ")");
		}

		if (orderGiven.getType() == null) {
			throw new IllegalArgumentException("Type of order must be set");
		}
		
		orderGiven.setDate(new Date());

		long amount_int = (long) (orderGiven
				.getAmount()
				.getValue()
				* (double) adapter
				.getDivisionFactors()
				.get(CurrencyType.BTC));
		long price_int = (long) (orderGiven
				.getPrice()
				.getValue()
				* (double) adapter
				.getDivisionFactors()
				.get(orderGiven
						.getCurrency()));

		String result = "";
		String orderId = "";
		String urlPath = MtGoxQueryUtil.create(orderGiven.getCurrency(), API_ADD_ORDER);
		HashMap<String, String> query_args = new HashMap<>();
		/*
		 * Params type : {ask (sell) | bid(buy) } amount_int : amount of BTC to
		 * buy or sell, as an integer price_int : The price per bitcoin in the
		 * auxiliary currency, as an integer, optional if you wish to trade at
		 * the market price
		 */
		query_args.put(PARAM_AMOUNT_INT, Long.toString(amount_int));
		query_args.put(PARAM_PRICE_INT, Long.toString(price_int));
		query_args.put(PARAM_TYPE, orderGiven.getType().name().toLowerCase());

		logger.log(Level.INFO, "Placing order: " + orderGiven.toString());

		String queryResult = adapter.query(urlPath, query_args);
		/*
		 * Sample result {"result":"success","data":"abc123-def45-.."}
		 */
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj2 = (JSONObject) (parser.parse(queryResult));
			result = (String) obj2.get("result");
			orderId = (String) obj2.get("data");
			
			if (orderId == null || orderId.equals("")) {
				logger.log(Level.INFO, "ERROR no order id was returned from server: " + orderId);
				return null;
			}

			// lastPriceArray[0] = (Double)obj2.get("last"); //USD

		} catch (ParseException ex) {
			Logger.getLogger(MtGoxTradeService.class.getName()).log(
					Level.SEVERE, null, ex);
			return null;
		}

		// Save to db
		if (!result.equals("success")) {

			throw new MtGoxException(result);
			
		} else {
			logger.log(Level.INFO, "Sucessfully placed order with id " + orderId);
			orderGiven.setOrderId(orderId);
			
			// TODO: re-read
		}

		return orderGiven;
	}

}