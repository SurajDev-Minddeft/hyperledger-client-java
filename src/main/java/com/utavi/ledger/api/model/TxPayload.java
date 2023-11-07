package com.utavi.ledger.api.model;

import com.utavi.ledger.api.model.enums.TxStatus;
import java.util.Objects;
import net.minidev.json.JSONObject;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

public class TxPayload {

	private final String txId;
	private final String channelId;
	private final long timestamp;
	private final long blockNumber;
	private final boolean valid;

	public TxPayload(final TransactionEvent event) {
		this.txId = event.getTransactionID();
		this.channelId = event.getChannelId();
		this.timestamp = event.getTimestamp().toInstant().getEpochSecond();
		this.blockNumber = event.getBlockEvent().getBlockNumber();
		this.valid = event.isValid();
	}

	public String getTxId() {
		return this.txId;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public String asJson() {
		final JSONObject payload = new JSONObject();
		payload.put("txId", this.txId);
		payload.put("blockNumber", this.blockNumber);
		payload.put("timestamp", this.timestamp);
		payload.put("status", TxStatus.fromBoolean(this.valid));
		return payload.toJSONString();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TxPayload txPayload = (TxPayload) o;
		return this.blockNumber == txPayload.blockNumber &&
				this.valid == txPayload.valid &&
				Objects.equals(getTxId(), txPayload.getTxId()) &&
				Objects.equals(getChannelId(), txPayload.getChannelId()) &&
				Objects.equals(this.timestamp, txPayload.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getTxId(), getChannelId(), this.timestamp, this.blockNumber, this.valid);
	}
}
