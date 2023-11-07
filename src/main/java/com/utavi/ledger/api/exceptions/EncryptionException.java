package com.utavi.ledger.api.exceptions;

public class EncryptionException extends BusinessException {

	public EncryptionException(final String message, final Throwable e) {
		super(message, e);
	}
}
