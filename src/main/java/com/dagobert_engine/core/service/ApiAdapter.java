package com.dagobert_engine.core.service;

import java.util.HashMap;

import org.json.simple.JSONObject;

import com.dagobert_engine.core.model.ApiStatus;
import com.dagobert_engine.core.model.CurrencyData;

public interface ApiAdapter {

	public long getLag();
	public String query(String url, HashMap<String, String> args);
	public CurrencyData getCurrencyForJsonObj(JSONObject obj);
	public ApiStatus getStatus();
}
