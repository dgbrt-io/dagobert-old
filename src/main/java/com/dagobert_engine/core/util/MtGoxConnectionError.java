package com.dagobert_engine.core.util;

public class MtGoxConnectionError extends MtGoxException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5446834917239558775L;
	private int code;
	private String answer;
	
	public MtGoxConnectionError(int code, String answer) {
		
	}

	public MtGoxConnectionError() {
		// TODO Auto-generated constructor stub
	}

	public MtGoxConnectionError(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MtGoxConnectionError(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MtGoxConnectionError(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	
}
