package com.dagobert_engine.core.util;

import java.util.HashMap;

/**
 * Builds query arguments
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
public class QueryArgBuilder {

	HashMap<String, String> map = new HashMap<>();
	
	private QueryArgBuilder() {}
	public static QueryArgBuilder create() {
		return new QueryArgBuilder();
	}
	
	
	public QueryArgBuilder add(String key, String value) {
		map.put(key, value);
		return this;
	}
	
	public HashMap<String, String> build() {
		return map;
	}

	
	
}
