package com.dagobert_engine.portfolio.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.NotImplementedException;
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
import com.dagobert_engine.portfolio.model.MtGoxPermission;
import com.dagobert_engine.portfolio.model.Transaction;
import com.dagobert_engine.portfolio.model.Wallet;
import com.dagobert_engine.portfolio.model.Transaction.RecordType;

/**
 * Retrieve portfolio data
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 * 
 *          License http://www.apache.org/licenses/LICENSE-2.0
 * 
 */
@ApplicationScoped
public class MtGoxPortfolioService implements Serializable {

	private static final long serialVersionUID = 1669780614487420871L;

	private final String API_GET_INFO = "MONEY/INFO";
	private final String API_MONEY_WALLET_HISTORY = "MONEY/WALLET/HISTORY";

	/**
	 * Logger
	 */
	@Inject
	private Logger logger;

	/**
	 * Adapter
	 */
	@Inject
	private MtGoxApiAdapter adapter;

	@Inject
	private ConfigService config;

	/**
	 * Parser
	 */
	private JSONParser parser = new JSONParser();

	private JSONObject getData() {

		CurrencyType currency = CurrencyType.valueOf(config
				.getProperty(KeyName.DEFAULT_CURRENCY));

		String jsonResult = adapter.query(MtGoxQueryUtil.create(currency,
				API_GET_INFO));
		JSONObject root;

		try {
			root = (JSONObject) parser.parse(jsonResult);
		} catch (ParseException e) {
			throw new MtGoxException(e);
		}

		String result = (String) root.get("result");

		if (!"success".equals(result)) {
			throw new MtGoxException("Error invoking MtGox Api: " + result);
		}

		JSONObject data = (JSONObject) root.get("data");
		return data;
	}

	/**
	 * Get last 50 results of the transaction history
	 * 
	 */
	public List<Transaction> getTransactions(CurrencyType curr) {
		return getTransactions(curr, 1);
	}

	/**
	 * Get the transaction history with a given page. Result is always 50 as
	 * maximum
	 * 
	 */
	public List<Transaction> getTransactions(CurrencyType curr, int page) {

		// create url and params
		final String url = API_MONEY_WALLET_HISTORY;
		final HashMap<String, String> params = QueryArgBuilder.create()
				.add("currency", curr.name()).add("page", "" + page).build();

		// Get json string
		final String jsonString = adapter.query(url, params);
		try {
			JSONObject root = (JSONObject) parser.parse(jsonString);
			String result = (String) root.get("result");

			if (!"success".equals(result)) {
				throw new MtGoxException(result);
			} else {
				JSONObject data = (JSONObject) root.get("data");
				JSONArray transactions = (JSONArray) data.get("result");

				final List<Transaction> resultList = new ArrayList<>();

				for (int i = 0; i < transactions.size(); i++) {
					JSONObject currentObj = (JSONObject) transactions.get(i);

					Transaction transaction = new Transaction();

					// int index = Integer.parseInt((String)
					// currentObj.get("Index"));
					Date time = new Date(((long) currentObj.get("Date")) * 1000);
					Transaction.RecordType type = Transaction.RecordType
							.valueOf(((String) currentObj.get("Type"))
									.toUpperCase());
					CurrencyData value = adapter
							.getCurrencyForJsonObj((JSONObject) currentObj
									.get("Value"));
					CurrencyData balance = adapter
							.getCurrencyForJsonObj((JSONObject) currentObj
									.get("Balance"));
					String info = (String) currentObj.get("Info");

					JSONArray link = (JSONArray) currentObj.get("Link");

					transaction.setCurrency(curr);

					if (info == null)
						return null;

					String rateText = info.split("at ")[1];
					Pattern pattern = Pattern.compile("[0-9]*\\.[0-9]{5}");
					Matcher matcher = pattern.matcher(rateText);

					if (matcher.find()) {

						transaction.setRate(new CurrencyData(Double
								.parseDouble(matcher.group(0)), config.getDefaultCurrency()));
					}
					
					transaction.setTime(time);
					transaction.setType(type);
					transaction.setValue(value);
					transaction.setBalance(balance);
					transaction.setInfo(info);

					if (link.size() > 0) {
						transaction.setTransactionUuid((String) link.get(0));
						transaction
								.setTransactionCategory(Transaction.TransactionCategory
										.forLink((String) link.get(1)));
						transaction.setIdentifier((String) link.get(2));
					}

					resultList.add(transaction);
				}
				return resultList;
			}

		} catch (ParseException e) {
			throw new MtGoxException(e);
		}

	}

	public Transaction getLastBuyTransaction(CurrencyType curr) {
		final List<Transaction> transactions = getTransactions(curr, 1);

		for (int i = 0; i < transactions.size(); i++) {

			Transaction trans = transactions.get(i);
			if (trans.getType().equals(RecordType.IN)) {
				return trans;
			}
		}
		return null;
	}

	public Transaction getLastSellTransaction(CurrencyType curr) {
		final List<Transaction> transactions = getTransactions(curr, 1);

		for (int i = 0; i < transactions.size(); i++) {
			Transaction trans = transactions.get(i);
			if (trans.getType().equals(RecordType.OUT)) {
				return trans;
			}
		}
		return null;
	}

	// TODO: MONEY/BITCOIN/GET_ADDRESS
	// TODO: SECURITY/HOTP/GEN
	// TODO: STREAM/LIST_

	/**
	 * Get monthly volume
	 * 
	 * @param forceRefresh
	 * @return
	 */
	public CurrencyData getMonthlyVolume() {

		JSONObject monthlyVolume = (JSONObject) getData().get("Monthly_Volume");
		return adapter.getCurrencyForJsonObj(monthlyVolume);

	}

	/**
	 * Get current trade fee
	 * 
	 * @param forceRefresh
	 * @return
	 */
	public double getTradeFee() {
		return (Double) getData().get("Trade_Fee");
	}

	/**
	 * Get permissions for api key
	 * 
	 * @param forceRefresh
	 * @return
	 */
	public MtGoxPermission[] getApiPermissions() {

		ArrayList<MtGoxPermission> perms = new ArrayList<>();
		JSONArray array = (JSONArray) getData().get("Rights");

		for (int i = 0; i < array.size(); i++) {
			perms.add(MtGoxPermission.fromString((String) array.get(i)));
		}

		return perms.toArray(new MtGoxPermission[perms.size()]);
	}

	/**
	 * Get Link TODO: 47:19,766 SEVERE
	 * [com.dagobert_engine.core.service.MtGoxApiAdapter] HTTP Error: 403,
	 * answer: {"result":"error","error":
	 * "Identification required to access private API"
	 * ,"token":"login_error_invalid_nonce"}
	 */
	public String getLink() {
		return (String) getData().get("Link");

	}

	/**
	 * Get last login
	 * 
	 * @param forceRefresh
	 * @return
	 */
	public Date getLastLogin() {

		DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		try {
			return df.parse((String) getData().get("Last_Login"));
		} catch (java.text.ParseException e) {
			throw new MtGoxException(e);
		}
	}

	/**
	 * Get join date
	 * 
	 * @param forceRefresh
	 * @return
	 */
	public Date getJoinDate() {

		DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		try {
			return df.parse((String) getData().get("Created"));
		} catch (java.text.ParseException e) {
			throw new MtGoxException(e);
		}
	}

	/**
	 * Get id
	 * 
	 * @param forceRefresh
	 * @return
	 */
	public String getId() {
		return (String) getData().get("Id");
	}

	/**
	 * Get locale
	 * 
	 * @param forceRefresh
	 * @return
	 */
	public Locale getLocale() {
		return new Locale((String) getData().get("Language"));
	}

	/**
	 * 
	 * @return
	 */
	public Map<CurrencyType, Wallet> getWallets() {

		HashMap<CurrencyType, Wallet> wallets = new HashMap<>();
		JSONObject walletsJson = (JSONObject) getData().get("wallets");

		for (CurrencyType type : CurrencyType.values()) {

			try {
				JSONObject walletJson = (JSONObject) walletsJson.get(type
						.name());

				Wallet wallet = new Wallet();

				wallet.setBalance(adapter
						.getCurrencyForJsonObj((JSONObject) walletJson
								.get("Balance")));
				wallet.setDailyWithdrawLimit(adapter
						.getCurrencyForJsonObj((JSONObject) walletJson
								.get("Daily_Withdraw_Limit")));
				wallet.setMaxWithdraw(adapter
						.getCurrencyForJsonObj((JSONObject) walletJson
								.get("Max_Withdraw")));
				wallet.setMonthlyWithdrawLimit(adapter
						.getCurrencyForJsonObj((JSONObject) walletJson
								.get("Monthly_Withdraw_Limit")));
				wallet.setOpenOrders(adapter
						.getCurrencyForJsonObj((JSONObject) walletJson
								.get("Open_Orders")));
				wallet.setOperations(Integer.parseInt((String) walletJson
						.get("Operations")));

				wallets.put(type, wallet);
			} catch (Exception exc) {
				logger.log(Level.WARNING, "No wallet for currency" + type
						+ ". Original msg: " + exc.getMessage());
			}

		}
		return wallets;

	}

	public String sendBtc(BigDecimal amount, String dest_address) { // TODO

		throw new NotImplementedException();
		/*
		 * String urlPath = API_WITHDRAW; HashMap<String, String> query_args =
		 * new HashMap<>(); /* Params address : Target bitcoin address
		 * amount_int : Amount of bitcoins to withdraw fee_int : Fee amount to
		 * be added to transaction (optional), maximum 0.01 BTC no_instant :
		 * Setting this parameter to 1 will prevent transaction from being
		 * processed internally, and force usage of the bitcoin blockchain even
		 * if receipient is also on the system green : Setting this parameter to
		 * 1 will cause the TX to use MtGox’s green address / query_args.put(
		 * "amount_int", Long.toString(Math.round(amount.multiply(
		 * adapter.getDivisionFactors().get(CurrencyType.BTC)).doubleValue())));
		 * query_args.put("address", dest_address); String queryResult =
		 * adapter.query(urlPath, query_args);
		 * 
		 * /* Sample result On success, this method will return the transaction
		 * id (in offser trx ) which will contain either the bitcoin transaction
		 * id as hexadecimal or a UUID value in case of internal transfer. /
		 * 
		 * JSONParser parser = new JSONParser(); try { JSONObject obj2 =
		 * (JSONObject) (parser.parse(queryResult)); // JSONObject data =
		 * (JSONObject)obj2.get("data"); //TODO
		 * 
		 * } catch (ParseException ex) { logger.log(Level.SEVERE,
		 * ex.getMessage()); }
		 */
		// return ""; // TODO Edit
	}

	public CurrencyData getBalance(CurrencyType currency) {
		HashMap<String, String> query_args = new HashMap<>();

		String queryResult = adapter.query(MtGoxQueryUtil.create(
				config.getDefaultCurrency(), API_GET_INFO), query_args);
		/*
		 * Sample result { "data": { "Created": "yyyy-mm-dd hh:mm:ss", "Id":
		 * "abc123", "Index": "123", "Language": "en_US", "Last_Login":
		 * "yyyy-mm-dd hh:mm:ss", "Login": "username", "Monthly_Volume":
		 * **Currency Object**, "Trade_Fee": 0.6, "Rights": ['deposit',
		 * 'get_info', 'merchant', 'trade', 'withdraw'], "Wallets": { "BTC": {
		 * "Balance": **Currency Object**, "Daily_Withdraw_Limit": **Currency
		 * Object**, "Max_Withdraw": **Currency Object**,
		 * "Monthly_Withdraw_Limit": null, "Open_Orders": **Currency Object**,
		 * "Operations": 1, }, "USD": { "Balance": **Currency Object**,
		 * "Daily_Withdraw_Limit": **Currency Object**, "Max_Withdraw":
		 * **Currency Object**, "Monthly_Withdraw_Limit": **Currency Object**,
		 * "Open_Orders": **Currency Object**, "Operations": 0, }, "JPY":{...},
		 * "EUR":{...}, // etc, depends what wallets you have }, }, "result":
		 * "success" }
		 */

		JSONParser parser = new JSONParser();
		try {
			JSONObject httpAnswerJson = (JSONObject) (parser.parse(queryResult));
			JSONObject dataJson = (JSONObject) httpAnswerJson.get("data");
			JSONObject walletsJson = (JSONObject) dataJson.get("Wallets");

			JSONObject walletJson = (JSONObject) ((JSONObject) walletsJson
					.get(currency.name())).get("Balance");

			String balance = (String) walletJson.get("value");
			return new CurrencyData(Double.parseDouble(balance), currency);

		} catch (Exception ex) {
			logger.log(Level.WARNING, ex.getMessage());
			return null;
		}
	}
}
