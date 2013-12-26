package com.dagobert_engine.core.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dagobert_engine.config.service.ConfigService;
import com.dagobert_engine.config.util.KeyName;
import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.core.model.CurrencyType;
import com.dagobert_engine.core.util.ApiKeys;
import com.dagobert_engine.core.util.MtGoxException;
import com.dagobert_engine.core.util.MtGoxQueryUtil;
import com.dagobert_engine.trading.service.MtGoxTradeService;
import com.dagobert_engine.trading.service.util.Constants;

/**
 * Api adapter
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@ApplicationScoped
public class MtGoxApiAdapter implements Serializable {
	
	// request arg keys
	public static final String ARG_KEY_NONCE = "nonce";
	
	// HTTP property keys
	public static final String REQ_PROP_USER_AGENT = "User-Agent";
	public static final String REQ_PROP_REST_KEY = "Rest-Key";
	public static final String REQ_PROP_REST_SIGN = "Rest-Sign";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6725526226426777506L;

	// Path variables
	private final String API_LAG = "MONEY/ORDER/LAG";
	private final String API_ID_KEY = "MONEY/IDKEY";
	
	/**
	 * Logger
	 */
	@Inject
	private Logger logger;
	/**
	 * Division factors
	 */
	private HashMap<CurrencyType, Integer> divisionFactors;

	/**
	 * MtGox api keys
	 */
	private ApiKeys keys = null;

	/**
	 * Inject ConfigService
	 */
	@Inject
	private ConfigService configService;
	
	/**
	 * Setup
	 */
	@PostConstruct
	public void setup() {
		// Read api keys

		String apiKey = configService.getProperty(KeyName.MTGOX_PUBLIC_KEY);
		String privateKey = configService.getProperty(KeyName.MTGOX_PRIVATE_KEY);
		
		if (StringUtils.isNotEmpty(apiKey) && StringUtils.isNotEmpty(privateKey)) {
			keys = new ApiKeys();
			keys.setApiKey(apiKey);
			keys.setPrivateKey(privateKey);
		}
		
		divisionFactors = new HashMap<CurrencyType, Integer>();
		divisionFactors.put(CurrencyType.BTC, 100000000);
		divisionFactors.put(CurrencyType.USD, 100000);
		divisionFactors.put(CurrencyType.GBP, 100000);
		divisionFactors.put(CurrencyType.EUR, 100000);
		divisionFactors.put(CurrencyType.JPY, 1000);
		divisionFactors.put(CurrencyType.AUD, 100000);
		divisionFactors.put(CurrencyType.CAD, 100000);
		divisionFactors.put(CurrencyType.CHF, 100000);
		divisionFactors.put(CurrencyType.CNY, 100000);
		divisionFactors.put(CurrencyType.DKK, 100000);
		divisionFactors.put(CurrencyType.HKD, 100000);
		divisionFactors.put(CurrencyType.PLN, 100000);
		divisionFactors.put(CurrencyType.RUB, 100000);
		divisionFactors.put(CurrencyType.SEK, 1000);
		divisionFactors.put(CurrencyType.SGD, 100000);
		divisionFactors.put(CurrencyType.THB, 100000);
	}

	/**
	 * Get ID key
	 */
	public String getIdKey() {
		CurrencyType curr = CurrencyType.valueOf(configService.getProperty(KeyName.DEFAULT_CURRENCY));
		
		String resultJson = query(MtGoxQueryUtil.create(curr, API_ID_KEY));
		
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject root = (JSONObject) (parser.parse(resultJson));
			String result = (String) root.get("result");
			
			if (!"success".equals(result)) {
				throw new MtGoxException(result);
			}
			
			String data = (String) root.get("data");
			return data;
		} catch (ParseException ex) {
			logger.log(Level.SEVERE, ex.toString());
			return null;
		}
		
		
	}
	
	
	/**
	 * Get lag of connection
	 * @return
	 */
	public String getLag() {
		String urlPath = API_LAG;
		HashMap<String, String> query_args = new HashMap<>();
		/*
		 * Params
		 */
		String queryResult = query(urlPath, query_args);
		/*
		 * Sample result the lag in milliseconds
		 */
		JSONParser parser = new JSONParser();
		String lag = "";
		try {
			JSONObject httpAnswerJson = (JSONObject) (parser.parse(queryResult));
			JSONObject dataJson = (JSONObject) httpAnswerJson.get("data");
			lag = (String) dataJson.get("lag_text");
		} catch (ParseException ex) {
			Logger.getLogger(MtGoxTradeService.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		return lag;
	}


	/**
	 * Builds a query string
	 * 
	 * @param args
	 * @return
	 */
	public String buildQueryString(HashMap<String, String> args) {
		String result = new String();
		for (String hashkey : args.keySet()) {
			if (result.length() > 0)
				result += '&';
			try {
				result += URLEncoder.encode(hashkey, Constants.ENCODING) + "="
						+ URLEncoder.encode(args.get(hashkey), Constants.ENCODING);
			} catch (Exception ex) {
				Logger.getLogger(MtGoxTradeService.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		return result;
	}

	/**
	 * Signs a request with a secret
	 * 
	 * @param secret
	 * @param hash_data
	 * @return
	 */
	private String signRequest(String secret, String hash_data) {
		String signature = "";
		try {
			Mac mac = Mac.getInstance(Constants.SIGN_HASH_FUNCTION);
			SecretKeySpec secret_spec = new SecretKeySpec(
					Base64.decodeBase64(secret), Constants.SIGN_HASH_FUNCTION);
			mac.init(secret_spec);
			signature = Base64.encodeBase64String(mac.doFinal(hash_data
					.getBytes()));
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			Logger.getLogger(MtGoxTradeService.class.getName()).log(
					Level.SEVERE, null, e);
		}
		return signature;
	}
	
	/**
	 * Queries the mtgox api without params
	 * 
	 * @param url
	 * @return
	 */
	public String query(String url) {
		return query(url, new HashMap<String, String>());
	}
	

	
	/**
	 * Transforms a json object to a json object
	 * 
	 * @param obj
	 * @return
	 */
	public CurrencyData getCurrencyForJsonObj(JSONObject obj) {

		CurrencyData curr = new CurrencyData();
		curr.setType(CurrencyType.valueOf((String) obj.get("currency")));
		double value = 
				Double.parseDouble((String) obj.get("value_int")) / getDivisionFactors().get(curr.getType());
		curr.setValue(value);
		return curr;
	}
	
	/**
	 * Queries the mtgox api with params
	 * 
	 * @param url
	 * @return
	 */
	public String query(String path, HashMap<String, String> args) {
		if (keys == null) {
			throw new MtGoxException("Api keys are not set. Please set them up in <classpath>/bitcoin/core/settings.properties");
		}

		// Create nonce
		String nonce = String.valueOf(System.currentTimeMillis()) + "000";
		
		// No http error yet
		boolean httpError = false;
		HttpsURLConnection connection = null;
		String answer = "";
		try {
			// add nonce and build arg list
			args.put(ARG_KEY_NONCE, nonce);
			String post_data = buildQueryString(args);
			
			String hash_data = path + "\0" + post_data; // Should be correct

			// args signature with apache cryptografic tools
			String signature = signRequest(keys.getPrivateKey(), hash_data);

			// build URL
			URL queryUrl = new URL(Constants.API_BASE_URL + path);
			// create and setup a HTTP connection
			connection = (HttpsURLConnection) queryUrl.openConnection();

			connection.setRequestMethod("POST");

			connection.setRequestProperty(REQ_PROP_USER_AGENT,
					com.dagobert_engine.core.util.Constants.APP_NAME);
			connection.setRequestProperty(REQ_PROP_REST_KEY, keys.getApiKey());
			connection.setRequestProperty(REQ_PROP_REST_SIGN,
					signature.replaceAll("\n", ""));

			connection.setDoOutput(true);
			connection.setDoInput(true);

			// Read the response

			DataOutputStream os = new DataOutputStream(
					connection.getOutputStream());
			os.writeBytes(post_data);
			os.close();

			BufferedReader br = null;
			
			// Any error?
			if (connection.getResponseCode() >= 400) {
				httpError = true; 
				
				// get error stream
				br = new BufferedReader(new InputStreamReader(
						(connection.getErrorStream())));
				
			} else
				// get normal stream
				br = new BufferedReader(new InputStreamReader(
						(connection.getInputStream())));

			String output;

			if (httpError)
				logger.log(Level.SEVERE, "HTTP ERROR: " + connection.getResponseCode() + ", Post Data: "
						+ post_data);
			
			while ((output = br.readLine()) != null) {
				answer += output;
			}
		}
		catch (IllegalStateException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		} finally {
			
			if (connection != null)
				connection.disconnect();
			connection = null;
		}
		
		return answer;
	}

	public void setDivisionFactors(
			HashMap<CurrencyType, Integer> divisionFactors) {
		this.divisionFactors = divisionFactors;
	}

	public HashMap<CurrencyType, Integer> getDivisionFactors() {
		return divisionFactors;
	}
}
