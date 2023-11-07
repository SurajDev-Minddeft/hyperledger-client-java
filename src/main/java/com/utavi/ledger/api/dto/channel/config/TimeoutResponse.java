package com.utavi.ledger.api.dto.channel.config;

public class TimeoutResponse {

	public TimeoutResponse(final String timeout) {
		this.timeout = timeout;
	}

	public TimeoutResponse() {
	}

	private String timeout;

	public String getTimeout() {
		return this.timeout;
	}

	public void setTimeout(final String timeout) {
		this.timeout = timeout;
	}
}
