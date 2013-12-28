package com.dagobert_engine.portfolio.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Permission given by api key of MtGox
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@XmlRootElement
public enum MtGoxPermission {
	DEPOSIT("deposit"), GET_INFO("get_info"), MERCHANT("merchant"), TRADE("trade"), WITHDRAW("withdraw");
	
	@XmlElement
	private String strValue;
	
	private MtGoxPermission(String str) {
		strValue = str;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	
	public static MtGoxPermission fromString(String str) {
		
		for (MtGoxPermission perm : MtGoxPermission.values()) {
			if (perm.strValue.equals(str)) 
				return perm;
		}
		
		return null;
	}
	
	
}
