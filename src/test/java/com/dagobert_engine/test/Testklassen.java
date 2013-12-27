package com.dagobert_engine.test;

import java.util.Arrays;
import java.util.List;

public enum Testklassen {
	INSTANCE;
	
	// Testklassen aus VERSCHIEDENEN Packages auflisten,
	// so dass alle darin enthaltenen Klassen ins Web-Archiv mitverpackt werden
	private final List<Class<? extends AbstractTest>> klassen = Arrays.asList(AbstractTest.class, CoreTest.class);
	
	public static Testklassen getInstance() {
		return INSTANCE;
	}
	
	public List<Class<? extends AbstractTest>> getTestklassen() {
		return klassen;
	}
}
