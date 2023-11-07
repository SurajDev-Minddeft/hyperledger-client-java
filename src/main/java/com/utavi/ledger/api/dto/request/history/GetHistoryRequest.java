package com.utavi.ledger.api.dto.request.history;

import com.utavi.ledger.api.dto.request.ChaincodeRequest;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetHistoryRequest extends ChaincodeRequest {

	@NotNull
	private GetHistoryExternalPayload payload;

	public GetHistoryExternalPayload getPayload() {
		return this.payload;
	}

	public void setPayload(final GetHistoryExternalPayload payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("payload", this.payload)
				.append("siloName", getSiloName())
				.append("accountId", getAccountId())
				.toString();
	}
}
