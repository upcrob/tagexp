package com.upcrob.tagexp;

/**
 * Describes an Exception thrown if a parse error occurs.
 */
public class ParseException extends RuntimeException {
	public ParseException(String msg) {
		super(msg);
	}
	
	public ParseException(String msg, Exception e) {
		super(msg, e);
	}
}
