package com.utavi.ledger.api.dto.request.credits;

import com.utavi.ledger.api.dto.request.ChaincodeRequest;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateCreditsRequest extends ChaincodeRequest {

	@NotNull
	private CreateCreditsPayload payload;

	public void setPayload(final CreateCreditsPayload payload) {
		this.payload = payload;
	}

	@Override
	public CreateCreditsPayload getPayload() {
		return this.payload;
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
