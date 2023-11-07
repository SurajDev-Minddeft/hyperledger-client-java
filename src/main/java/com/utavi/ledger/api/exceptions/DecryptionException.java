package com.utavi.ledger.api.exceptions;

public class DecryptionException extends BusinessException {

	public DecryptionException(final String message, final Throwable e) {
		super(message, e);
	}
}
