package com.utavi.ledger.api.dto.request.balance.get;

import com.utavi.ledger.api.dto.request.ChaincodeRequest;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetWalletEntryRequest extends ChaincodeRequest {

	@NotNull
	private GetWalletEntryRequestPayload payload;

	public GetWalletEntryRequestPayload getPayload() {
		return this.payload;
	}

	public void setPayload(final GetWalletEntryRequestPayload payload) {
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
