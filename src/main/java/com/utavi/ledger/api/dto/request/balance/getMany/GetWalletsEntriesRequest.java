package com.utavi.ledger.api.dto.request.balance.getMany;

import com.utavi.ledger.api.dto.request.ChaincodeRequest;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetWalletsEntriesRequest extends ChaincodeRequest {

	@NotNull
	private GetWalletsEntriesRequestPayload payload;

	@Override
	public GetWalletsEntriesRequestPayload getPayload() {
		return this.payload;
	}

	public void setPayload(final GetWalletsEntriesRequestPayload payload) {
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
