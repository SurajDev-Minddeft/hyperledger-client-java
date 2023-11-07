package com.utavi.ledger.api.dto;

public class PeerInfoDto {

	private String peerName;
	private String channelName;
	private String location;

	public String getPeerName() {
		return this.peerName;
	}

	public String getChannelName() {
		return this.channelName;
	}

	public String getLocation() {
		return this.location;
	}

	public void setPeerName(final String peerName) {
		this.peerName = peerName;
	}

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	public void setLocation(final String location) {
		this.location = location;
	}
}
