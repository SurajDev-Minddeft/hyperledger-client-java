package com.utavi.ledger.api.dto.channel.config;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MaxMessageCountRequest {

	public MaxMessageCountRequest(@NotNull @Positive final Integer maxMessageCount) {
		this.maxMessageCount = maxMessageCount;
	}

	public MaxMessageCountRequest() {
	}

	@NotNull
	@Positive
	private Integer maxMessageCount;

	public Integer getMaxMessageCount() {
		return this.maxMessageCount;
	}

	public void setMaxMessageCount(final Integer maxMessageCount) {
		this.maxMessageCount = maxMessageCount;
	}
}
