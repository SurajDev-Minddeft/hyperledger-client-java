package com.utavi.ledger.api.dto.channel.config;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TimeoutRequest {

	public TimeoutRequest(@NotNull @Positive final Integer timeout) {
		this.timeout = timeout;
	}

	public TimeoutRequest() {
	}

	@NotNull
	@Positive
	private Integer timeout;

	public Integer getTimeout() {
		return this.timeout;
	}

	public void setTimeout(final Integer timeout) {
		this.timeout = timeout;
	}
}
