package com.utavi.ledger.api.exceptions;

public class BusinessException extends RuntimeException {

	public BusinessException(final String msg) {
		super(msg);
	}

	public BusinessException(final Throwable cause) {
		super(cause);
	}

	public BusinessException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
