package com.dagobert_engine.service;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

@ApplicationScoped
public class Dagobert {

	private Logger logger = Logger.getLogger(getClass());
	
	private Map<String, Service> services;
	
	public Dagobert() {
		services = new HashMap<>();
	}
	
	public void registerService(String name, Service service) {
		logger.info("Registering Service " + service.getClass().getSimpleName() + "...");
		services.put(name, service);
	}
	
	public void removeService(String name) {
		logger.info("Removing Service " + name + "...");
		services.remove(name);
	}
	
	public Map<String, Service> getServices() {
		return services;
	}

}
