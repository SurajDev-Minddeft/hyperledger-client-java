package com.utavi.ledger.api.dto.channel.config;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AbsoluteMaxBytesRequest {

	public AbsoluteMaxBytesRequest(@NotNull @Positive final Integer absoluteMaxBytes) {
		this.absoluteMaxBytes = absoluteMaxBytes;
	}

	public AbsoluteMaxBytesRequest() {
	}

	@NotNull
	@Positive
	private Integer absoluteMaxBytes;

	public Integer getAbsoluteMaxBytes() {
		return this.absoluteMaxBytes;
	}

	public void setAbsoluteMaxBytes(final Integer absoluteMaxBytes) {
		this.absoluteMaxBytes = absoluteMaxBytes;
	}
}
