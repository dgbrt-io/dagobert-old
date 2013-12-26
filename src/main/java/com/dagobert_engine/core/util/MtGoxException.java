package com.dagobert_engine.core.util;

import javax.ejb.ApplicationException;

/**
 * MtGoxException
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@ApplicationException(rollback = true)
public class MtGoxException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1845262181666777455L;

	public MtGoxException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MtGoxException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MtGoxException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MtGoxException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
