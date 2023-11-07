package com.utavi.ledger.api.dto.response;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetWalletEntryResponsePayload {

	@NotNull
	private BigDecimal amount;

	@NotBlank
	private String accountId;

	@NotBlank
	private String walletEntryId;

	@NotBlank
	private String creditId;

	public BigDecimal getAmount() {
		return this.amount;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(final String accountId) {
		this.accountId = accountId;
	}

	public String getWalletEntryId() {
		return this.walletEntryId;
	}

	public String getCreditId() {
		return this.creditId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("amount", this.amount)
				.append("accountId", this.accountId)
				.append("walletEntryId", this.walletEntryId)
				.append("creditId", this.creditId)
				.toString();
	}
}
