package com.dagobert_engine.test.util;

import java.util.Arrays;
import java.util.List;

import com.dagobert_engine.test.core.CoreTest;

public enum Testclasses {
	INSTANCE;
	
	// Testklassen aus VERSCHIEDENEN Packages auflisten,
	// so dass alle darin enthaltenen Klassen ins Web-Archiv mitverpackt werden
	private final List<Class<? extends AbstractTest>> klassen = Arrays.asList(AbstractTest.class, CoreTest.class);
	
	public static Testclasses getInstance() {
		return INSTANCE;
	}
	
	public List<Class<? extends AbstractTest>> getTestklassen() {
		return klassen;
	}
}
