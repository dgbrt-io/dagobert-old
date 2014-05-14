package com.dagobert_engine.rest.util;

import java.util.HashMap;

public class MapBuilder<K, V> {
	
	private HashMap<K, V> map = null;
	
	
	public MapBuilder<K, V> create() {
		map = new HashMap<>();
		return this;
	}
	
	public MapBuilder<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}
	
	public HashMap<K, V> build () {
		HashMap<K, V> map = this.map;
		this.map = null;
		return map;
	}
	
	
}
