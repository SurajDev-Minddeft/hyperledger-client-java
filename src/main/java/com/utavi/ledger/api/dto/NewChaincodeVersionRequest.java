package com.utavi.ledger.api.dto;

import javax.validation.constraints.NotBlank;

public class NewChaincodeVersionRequest {

	@NotBlank
	private String channelName;

	@NotBlank
	private String chaincodeName;

	@NotBlank
	private String version;

	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	public String getChaincodeName() {
		return this.chaincodeName;
	}

	public void setChaincodeName(final String chaincodeName) {
		this.chaincodeName = chaincodeName;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(final String version) {
		this.version = version;
	}
}
