package com.dagobert_engine.core.util;

import com.dagobert_engine.core.model.CurrencyType;

/**
 * QueryUtil
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
public class MtGoxQueryUtil {
	
	
	
	private MtGoxQueryUtil() {}
	public static String create(CurrencyType type, String query) {
		return "BTC" + type.name() + "/" + query;
	}
}
