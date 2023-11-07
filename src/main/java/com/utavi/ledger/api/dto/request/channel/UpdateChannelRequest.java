package com.utavi.ledger.api.dto.request.channel;

public class UpdateChannelRequest {

	public enum UpdateGroup {
		APPLICATION, ORDERER
	}

	private String channelName;
	private Payload payload;
	private UpdateGroup updateGroup;

	public Payload getPayload() {
		return this.payload;
	}

	public void setPayload(final Payload payload) {
		this.payload = payload;
	}

	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	public UpdateGroup getUpdateGroup() {
		return this.updateGroup;
	}

	public void setUpdateGroup(final UpdateGroup updateGroup) {
		this.updateGroup = updateGroup;
	}

	public static class Payload {

		private final String targetValue;
		private final String key;
		private final String value;

		public Payload(final String targetValue, final String key, final String value) {
			this.targetValue = targetValue;
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}

		public String getTargetValue() {
			return this.targetValue;
		}
	}

}