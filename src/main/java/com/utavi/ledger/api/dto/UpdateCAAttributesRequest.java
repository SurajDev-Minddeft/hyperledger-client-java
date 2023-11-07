package com.utavi.ledger.api.dto;

import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdateCAAttributesRequest {

	private final String accountName;
	private final String key;
	private final String value;

	public UpdateCAAttributesRequest(final String accountName, final String key, final String value) {
		this.accountName = accountName;
		this.key = key;
		this.value = value;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UpdateCAAttributesRequest)) {
			return false;
		}
		final UpdateCAAttributesRequest that = (UpdateCAAttributesRequest) o;
		return getAccountName().equals(that.getAccountName()) &&
				getKey().equals(that.getKey()) &&
				getValue().equals(that.getValue());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAccountName(), getKey(), getValue());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("accountName", this.accountName)
				.append("key", this.key)
				.append("value", this.value)
				.toString();
	}
}
