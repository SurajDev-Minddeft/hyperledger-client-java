package com.utavi.ledger.api.dto;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SetChannelPeersDto {

	@NotBlank
	private String channelName;

	@NotEmpty
	private Set<String> peers;

	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	public Set<String> getPeers() {
		return this.peers;
	}

	public void setPeers(final Set<String> peers) {
		this.peers = peers;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("channelName", this.channelName)
				.append("peers", this.peers)
				.toString();
	}
}
