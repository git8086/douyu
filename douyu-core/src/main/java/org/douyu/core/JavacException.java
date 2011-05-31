package org.douyu.core;

/**
 * 
 * @author ZHH
 *
 */
public class JavacException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JavacException() {
		super();
	}

	public JavacException(String message) {
		super(message);
	}

	public JavacException(Throwable cause) {
		super(cause);
	}

	public JavacException(String message, Throwable cause) {
		super(message, cause);
	}
}