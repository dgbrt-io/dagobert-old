package com.dagobert_engine.core.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.dagobert_engine.core.model.Configuration;
import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.core.model.CurrencyType;
import com.dagobert_engine.core.model.ApiStatus;
import com.dagobert_engine.core.model.MtGoxApiStatus;
import com.dagobert_engine.core.model.MtGoxConfiguration;
import com.dagobert_engine.core.util.ApiKeysNotSetException;
import com.dagobert_engine.core.util.MtGoxConnectionError;
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
public class MtGoxApiAdapter implements ApiAdapter, Serializable {
	
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
	
	@Inject
	private BootstrapService updateService;
	
	/**
	 * Logger
	 */
	@Inject
	private Logger logger;

	@Inject
	private Configuration config;
	
	@Inject
	private MtGoxConfiguration mtGoxConfig;
	

	/**
	 * Get ID key
	 */
	public String getIdKey() {
		CurrencyType curr = config.getDefaultCurrency();
		
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
	@Override
	public long getLag() {
		try {
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
			return Long.parseLong(lag);
		}
		catch (MtGoxConnectionError ex) {
			Logger.getLogger(MtGoxTradeService.class.getName()).log(
					Level.SEVERE, null, ex);
			return -1L;
		}
	}

	

	/**
	 * Builds a query string
	 * 
	 * @param args
	 * @return
	 */
	private String buildQueryString(HashMap<String, String> args) {
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
		if (obj == null)
			throw new IllegalArgumentException("JSONObject mustn't be null");

		CurrencyData curr = new CurrencyData();
		curr.setType(CurrencyType.valueOf((String) obj.get("currency")));
		double value = 
				Double.parseDouble((String) obj.get("value_int")) / mtGoxConfig.getDivisionFactors().get(curr.getType());
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
		
		final String publicKey = mtGoxConfig.getMtGoxPublicKey();
		final String privateKey = mtGoxConfig.getMtGoxPrivateKey();
		
		if (publicKey == null || privateKey == null || "".equals(publicKey) || "".equals(privateKey)) {
			throw new ApiKeysNotSetException("Either public or private key of MtGox are not set. Please set them up in src/main/resources/META-INF/seam-beans.xml");
		}

		// Create nonce
		final String nonce = String.valueOf(System.currentTimeMillis()) + "000";
		
		HttpsURLConnection connection = null;
		String answer = null;
		try {
			// add nonce and build arg list
			args.put(ARG_KEY_NONCE, nonce);
			String post_data = buildQueryString(args);
			
			String hash_data = path + "\0" + post_data; // Should be correct

			// args signature with apache cryptografic tools
			String signature = signRequest(mtGoxConfig.getMtGoxPrivateKey(), hash_data);

			// build URL
			URL queryUrl = new URL(Constants.API_BASE_URL + path);
			// create and setup a HTTP connection
			connection = (HttpsURLConnection) queryUrl.openConnection();

			connection.setRequestMethod("POST");

			connection.setRequestProperty(REQ_PROP_USER_AGENT,
					com.dagobert_engine.core.util.Constants.APP_NAME);
			connection.setRequestProperty(REQ_PROP_REST_KEY, mtGoxConfig.getMtGoxPublicKey());
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
			int code = connection.getResponseCode();
			if (code >= 400) {
				// get error stream
				br = new BufferedReader(new InputStreamReader(
						(connection.getErrorStream())));
				
				answer = toString(br);
				logger.severe("HTTP Error on queryin " + path + ": " + code + ", answer: " + answer);
				throw new MtGoxConnectionError(code, answer);
				
			} else {
				// get normal stream
				br = new BufferedReader(new InputStreamReader(
						(connection.getInputStream())));
				answer = toString(br);
				
			}
		}
		catch (UnknownHostException exc) {
			throw new MtGoxConnectionError("Could not connect to MtGox. Please check your internet connection. (" + exc.getClass().getName() + ")");
		}
		catch (IllegalStateException ex) {
			throw new MtGoxConnectionError(ex);
		} catch (IOException ex) {
			throw new MtGoxConnectionError(ex);
		} finally {
			
			if (connection != null)
				connection.disconnect();
			connection = null;
		}
		
		return answer;
	}
	
	private String toString(BufferedReader br) throws IOException {

		
		StringBuilder answer = new StringBuilder();
		String line = "";
		while ((line = br.readLine()) != null) {
			answer.append(line);
		}
		
		return answer.toString();
	}

	public ApiStatus getStatus() {
		MtGoxApiStatus status = new MtGoxApiStatus();
		status.setDefaultCurrency(config.getDefaultCurrency());
		status.setDefaultPeriodLength(config.getDefaultPeriodLength());
		status.setLag(getLag());
		status.setRunning(updateService.isRunning());
		status.setTime(new Date());
		status.setMinTradeAmount(mtGoxConfig.getMinTradeAmount());
		status.setKeysSet(StringUtils.isNotEmpty(mtGoxConfig.getMtGoxPublicKey()) && StringUtils.isNotEmpty(mtGoxConfig.getMtGoxPrivateKey()));
		status.setMinTradeAmount(mtGoxConfig.getMinTradeAmount());
		status.setOnline(status.getLag() != -1L);
		return status;
	}
}
