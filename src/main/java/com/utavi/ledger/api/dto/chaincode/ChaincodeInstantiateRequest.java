package com.utavi.ledger.api.dto.chaincode;

import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChaincodeInstantiateRequest extends ChaincodeManagementRequest {

	@NotBlank
	private String channelName;

	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("channelName", this.channelName)
				.append("chaincodeName", getChaincodeName())
				.append("chaincodeVersion", getChaincodeVersion())
				.toString();
	}
}
