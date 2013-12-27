package com.dagobert_engine.core.util;

/**
 * Thrown if the api keys are not set
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
public class ApiKeysNotSetException extends MtGoxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6415643585427105721L;

	public ApiKeysNotSetException() {
		// TODO Auto-generated constructor stub
	}

	public ApiKeysNotSetException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ApiKeysNotSetException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ApiKeysNotSetException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
