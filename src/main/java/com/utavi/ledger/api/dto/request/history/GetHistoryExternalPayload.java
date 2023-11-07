package com.utavi.ledger.api.dto.request.history;

import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetHistoryExternalPayload {

	@NotBlank
	private String key;

	public String getKey() {
		return this.key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("key", this.key)
				.toString();
	}
}
