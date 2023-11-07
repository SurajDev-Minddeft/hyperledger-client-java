package com.utavi.ledger.api.dto.request.account;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateAccountExternalPayload {

	@NotBlank
	private String accountId;

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(final String accountId) {
		this.accountId = accountId;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CreateAccountExternalPayload that = (CreateAccountExternalPayload) o;
		return Objects.equals(this.accountId, that.accountId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.accountId);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("accountId", this.accountId)
				.toString();
	}
}
