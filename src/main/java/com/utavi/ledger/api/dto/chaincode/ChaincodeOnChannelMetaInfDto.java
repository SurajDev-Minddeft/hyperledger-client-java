package com.utavi.ledger.api.dto.chaincode;

import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChaincodeOnChannelMetaInfDto extends ChaincodeMetaInfDto {

	@NotBlank
	private String channelName;

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	public String getChannelName() {
		return this.channelName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", getName())
				.append("path", getPath())
				.append("lang", getLang())
				.append("version", getVersion())
				.append("channelName", getChannelName())
				.toString();
	}
}
