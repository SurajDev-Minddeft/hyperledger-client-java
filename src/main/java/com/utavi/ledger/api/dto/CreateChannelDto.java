package com.utavi.ledger.api.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class CreateChannelDto {

	@NotBlank
	private String channelName;

	public CreateChannelDto(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelName() {
		return channelName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CreateChannelDto that = (CreateChannelDto) o;
		return Objects.equals(channelName, that.channelName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(channelName);
	}

	@Override
	public String toString() {
		return "CreateChannelDto{" +
				   "channelName='" + channelName + '\'' +
				   '}';
	}
}
