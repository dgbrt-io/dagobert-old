package com.dagobert_engine.trading.service;

import java.io.Serializable;
import java.math.BigDecimal;
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
import com.dagobert_engine.portfolio.service.MtGoxPortfolioService;
import com.dagobert_engine.statistics.model.Period;
import com.dagobert_engine.statistics.service.StatisticsService;
import com.dagobert_engine.trading.model.Order;
import com.dagobert_engine.trading.model.Order.OrderType;
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

	public enum TrendType {
		RISING, NORMAL, FALLING;
	}
	

	public enum OrderAction {
		BUY, SELL;
	}

	// Paths
	private final String API_ADD_ORDER = "MONEY/ORDER/ADD";
	private final String API_CANCEL_ORDER = "MONEY/ORDER/CANCEL";
	private final String API_MONEY_ORDERS = "MONEY/ORDERS";

	@Inject
	private StatisticsService statsService;

	@Inject
	private MtGoxApiAdapter adapter;
	
	@Inject
	private MtGoxPortfolioService portfolioService;
	
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
	
	private List<Order> openOrders = new ArrayList<>();
	private Order lastOrder = null;

	
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
			
			logger.log(Level.INFO, "There are " + data.size() + " orders in total.");
			
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
		
		// Get periods
		final Period lastPeriod = statsService.getLastPeriod();
		final Period currentPeriod = statsService.getCurrentPeriod();

		if (lastPeriod == null) {
			logger.log(Level.INFO, "There is no last period... waiting for this period to complete...");
			logger.log(Level.INFO, "current time: " + new Date() + ", waiting for " + currentPeriod.getToTime());
			return;
		}

		// Get current price
		final double currentPrice = currentPeriod.getLatestRate().getValue();

		// Get USD balance
		final BigDecimal balanceUSD = portfolioService.getBalance(CurrencyType.USD);
		
		if (balanceUSD == null) {
			
			logger.log(Level.SEVERE, "There seems to be no USD wallet");
			return;
		}
		
		// Assign last order
		
		openOrders = getOpenOrders();
		
		if (openOrders.size() > 0) {
			logger.log(Level.INFO, "There are still open orders by MtGox. Waiting until they're done. ");
			logger.log(Level.INFO, "=== Open orders ===");
			
			for (Order order : openOrders) {
				logger.log(Level.INFO, "  * " + order.toString());
			}

			logger.log(Level.INFO, "===================");
			return;
		}
		
		if (lastOrder == null && enoughMoneyToBuy(balanceUSD.doubleValue(), currentPrice)) {
			logger.log(Level.INFO, "Creating fake Buy order for existing BTC balance at current rate");
			lastOrder = createInitialBTCBuy(balanceUSD.doubleValue());
		}
		
		if (lastOrder == null && enoughBTCtoSell(balanceUSD.doubleValue())){
			logger.log(Level.INFO, "Creating fake Sell order for existing money balance at current rate");
			lastOrder = createInitialBTCSell(balanceUSD.doubleValue());
		}

		if (lastOrder == null) {
			throw new MtGoxException("There are not enough funds to trade with. Please add BTC or other money to your wallet.");
		}
		
		Order orderGiven = strategy.createOrder();
		
		if (orderGiven != null) {
			placeOrder(orderGiven);
		}
	}

	/**
	 * Creates an initial buy order
	 * 
	 * @param balanceBTC
	 * @return
	 */
	private Order createInitialBTCBuy(double balanceBTC) {
		Order buyOrder = new Order();
		buyOrder.setOrderId("DAGOBERT-INITIAL-BUY-" + new Date().getTime());
		buyOrder.setAmount(new CurrencyData(balanceBTC, CurrencyType.USD));
		buyOrder.setPrice(new CurrencyData(statsService.getLastPeriod().getLatestRate()
				.getValue(), CurrencyType.USD));
		buyOrder.setCurrency(CurrencyType.USD);
		buyOrder.setDate(new Date());
		buyOrder.setStatus(StatusType.STOP);
		buyOrder.setType(OrderType.BID);
		return buyOrder;
	}

	/**
	 * Creates an initial sell order
	 * 
	 * @param balanceBTC
	 * @return
	 */
	private Order createInitialBTCSell(double balanceBTC) {

		Order sellOrder = new Order();
		sellOrder.setOrderId("DAGOBERT-INITIAL-SELL-" + new Date().getTime());
		sellOrder.setAmount(new CurrencyData(balanceBTC, CurrencyType.USD));
		sellOrder.setPrice(new CurrencyData(statsService.getLastPeriod().getLatestRate()
				.getValue(), CurrencyType.USD));
		sellOrder.setCurrency(CurrencyType.USD);
		sellOrder.setDate(new Date());
		sellOrder.setStatus(StatusType.STOP);
		sellOrder.setType(OrderType.ASK);
		return sellOrder;
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

		if (orderGiven.getAmount().getValue() <  Double.parseDouble(configService.getProperty(KeyName.MIN_TRADE_AMOUNT)) ) {
			throw new IllegalArgumentException(
					"Amount must be greater or equal than MINIMUM_BTC_TRADE_SIZE ("
							+ Double.parseDouble(configService.getProperty(KeyName.MIN_TRADE_AMOUNT)) + ")");
		}

		if (orderGiven.getType() == null) {
			throw new IllegalArgumentException("Type of order must be set");
		}
		
		orderGiven.setDate(new Date());
		orderGiven.setStatus(StatusType.EXECUTING);

		long amount_int = (int) (orderGiven.getAmount().getValue() * ((double) adapter.getDivisionFactors().get(CurrencyType.BTC)));
		long price_int = (int) (orderGiven.getPrice().getValue() * ((double) adapter.getDivisionFactors().get(orderGiven.getCurrency().name())));

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