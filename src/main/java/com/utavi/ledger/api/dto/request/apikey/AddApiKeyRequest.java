package com.utavi.ledger.api.dto.request.apikey;

import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AddApiKeyRequest {

	@NotBlank
	private String channelName;

	@NotBlank
	private String apiKey;

	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("channelName", this.channelName)
				.append("apiKey", this.apiKey)
				.toString();
	}
}
