package com.utavi.ledger.api.dto.response;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransactionResponse {

	private String transactionId;
	private long blockNumber;
	private boolean isValid;
	private Date timestamp;

	public TransactionResponse(final String transactionId, final long blockNumber, final Date timestamp, final boolean isValid) {
		this.transactionId = transactionId;
		this.blockNumber = blockNumber;
		this.timestamp = timestamp;
		this.isValid = isValid;
	}

	private TransactionResponse() {}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(final String transactionId) {
		this.transactionId = transactionId;
	}

	public long getBlockNumber() {
		return this.blockNumber;
	}

	public void setBlockNumber(final long blockNumber) {
		this.blockNumber = blockNumber;
	}

	public boolean isValid() {
		return this.isValid;
	}

	public void setValid(final boolean valid) {
		this.isValid = valid;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("transactionId", this.transactionId)
				.append("blockNumber", this.blockNumber)
				.append("isValid", this.isValid)
				.append("timestamp", this.timestamp)
				.toString();
	}
}
