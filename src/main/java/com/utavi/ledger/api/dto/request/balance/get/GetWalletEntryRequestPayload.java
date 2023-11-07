package com.utavi.ledger.api.dto.request.balance.get;

import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetWalletEntryRequestPayload {

	@NotBlank
	private String walletEntryId;

	@NotBlank
	private String creditId;

	@NotBlank
	private String accountId;

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(final String accountId) {
		this.accountId = accountId;
	}

	public String getCreditId() {
		return this.creditId;
	}

	public void setCreditId(final String creditId) {
		this.creditId = creditId;
	}

	public String getWalletEntryId() {
		return this.walletEntryId;
	}

	public void setWalletEntryId(final String walletEntryId) {
		this.walletEntryId = walletEntryId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("walletEntryId", this.walletEntryId)
				.append("creditId", this.creditId)
				.append("accountId", this.accountId)
				.toString();
	}
}
