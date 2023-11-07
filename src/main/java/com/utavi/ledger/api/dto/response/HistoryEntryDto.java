package com.utavi.ledger.api.dto.response;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class HistoryEntryDto {

	private String transactionId;
	private long timestamp;
	private Object value;

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(final String transactionId) {
		this.transactionId = transactionId;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(final Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("transactionId", this.transactionId)
				.append("timestamp", this.timestamp)
				.append("value", this.value)
				.toString();
	}
}
