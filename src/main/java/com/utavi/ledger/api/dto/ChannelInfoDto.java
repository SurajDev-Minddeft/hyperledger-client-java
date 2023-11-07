package com.utavi.ledger.api.dto;

import javax.validation.constraints.NotBlank;

public class ChannelInfoDto {

	@NotBlank
	private String channelName;

	private String siloName;

	public String getSiloName() {
		return this.siloName;
	}

	public void setSiloName(final String siloName) {
		this.siloName = siloName;
	}

	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}
}
