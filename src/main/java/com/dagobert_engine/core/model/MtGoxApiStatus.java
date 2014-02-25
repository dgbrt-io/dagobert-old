package com.dagobert_engine.core.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "status")
public class MtGoxApiStatus extends ApiStatus {
	
	@XmlElement
	private boolean keysSet;

	public boolean areKeysSet() {
		return keysSet;
	}

	public void setKeysSet(boolean keysSet) {
		this.keysSet = keysSet;
	}

}
