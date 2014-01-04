package com.dagobert_engine.core.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmailConfiguration implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2219357761358836785L;
	private boolean enabled;
	private String host;
	private int port;
	private boolean authRequired;
	private String username;
	private String password;

	private ArrayList<String> receipients;
	
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isAuthRequired() {
		return authRequired;
	}
	public void setAuthRequired(boolean authRequired) {
		this.authRequired = authRequired;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<String> getReceipients() {
		return receipients;
	}
	public void setReceipients(ArrayList<String> receipients) {
		this.receipients = receipients;
	}
	
	

}
