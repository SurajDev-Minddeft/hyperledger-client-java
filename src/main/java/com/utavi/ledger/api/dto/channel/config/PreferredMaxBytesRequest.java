package com.utavi.ledger.api.dto.channel.config;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PreferredMaxBytesRequest {

	public PreferredMaxBytesRequest(@NotNull @Positive final Integer preferredMaxBytes) {
		this.preferredMaxBytes = preferredMaxBytes;
	}

	public PreferredMaxBytesRequest() {
	}

	@NotNull
	@Positive
	private Integer preferredMaxBytes;

	public Integer getPreferredMaxBytes() {
		return this.preferredMaxBytes;
	}

	public void setPreferredMaxBytes(final Integer preferredMaxBytes) {
		this.preferredMaxBytes = preferredMaxBytes;
	}
}
