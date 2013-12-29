package com.dagobert_engine.core.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.dagobert_engine.core.model.CurrencyType;
import com.dagobert_engine.core.util.KeyName;

/**
 * Used to read settings file
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 * 
 *          License http://www.apache.org/licenses/LICENSE-2.0
 * 
 */
@ApplicationScoped
public class ConfigService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3484221239454953716L;

	/**
	 * Properties file
	 */
	public static final String PROPERTIES_FILE = "/com/dagobert_engine/settings.properties";

	/**
	 * Logger
	 */
	@Inject
	private Logger logger;

	/**
	 * Properties
	 */
	private Map<String, String> properties = null;

	/**
	 * Initial setup
	 */
	private void setup() {

		logger.log(Level.INFO, "Loading properties...");

		properties = new HashMap<>();
		try {
			Properties p = new Properties();
			InputStream in = getClass().getResourceAsStream(PROPERTIES_FILE);
			p.load(in);

			Enumeration<Object> keys = p.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				logger.info("Setting property " + key);
				properties.put(key, p.getProperty(key));
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,
					e.getClass().getName() + ": " + e.getMessage());
		}

		logger.log(Level.INFO, "Done loading properties.");
	}

	/**
	 * Get property for KeyName
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(KeyName key) {
		if (properties == null)
			setup();

		
		String str = properties.get(key.name());
		
		if (str == null)
			logger.warning("Key " + key.name() + " is not set");
		
		return str;
	}

	/**
	 * Get default currency
	 * 
	 * @return
	 */
	public CurrencyType getDefaultCurrency() {

		return CurrencyType.valueOf(getProperty(KeyName.DEFAULT_CURRENCY));
	}

	/**
	 * Set property
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(KeyName key, String value) {
		if (properties == null)
			setup();
		properties.put(key.name(), value);
	}
}
