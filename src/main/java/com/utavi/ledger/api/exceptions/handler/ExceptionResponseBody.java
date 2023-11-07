package com.utavi.ledger.api.exceptions.handler;

class ExceptionResponseBody {

	private final String exceptionMessage;

	ExceptionResponseBody(final String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionMessage() {
		return this.exceptionMessage;
	}

}
