package com.utavi.ledger.api.dto.request.account;

import com.utavi.ledger.api.dto.request.ChaincodeRequest;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateAccountRequest extends ChaincodeRequest {

	@NotNull
	private CreateAccountExternalPayload payload;

	public CreateAccountExternalPayload getPayload() {
		return this.payload;
	}

	public void setPayload(final CreateAccountExternalPayload payload) {
		this.payload = payload;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CreateAccountRequest that = (CreateAccountRequest) o;
		return Objects.equals(this.payload, that.payload);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.payload);
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
