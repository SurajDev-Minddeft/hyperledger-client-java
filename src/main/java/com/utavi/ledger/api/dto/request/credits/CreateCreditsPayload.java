package com.utavi.ledger.api.dto.request.credits;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateCreditsPayload {

	@NotNull
	@JsonFormat(shape = Shape.STRING)
	private BigDecimal amount;

	@NotBlank
	private String walletEntryId;

	@NotBlank
	private String creditId;

	@NotBlank
	private String accountId;

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public String getWalletEntryId() {
		return this.walletEntryId;
	}

	public void setWalletEntryId(final String walletEntryId) {
		this.walletEntryId = walletEntryId;
	}

	public String getCreditId() {
		return this.creditId;
	}

	public void setCreditId(final String creditId) {
		this.creditId = creditId;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(final String accountId) {
		this.accountId = accountId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("accountId", this.accountId)
				.append("amount", this.amount)
				.append("walletEntryId", this.walletEntryId)
				.append("creditId", this.creditId)
				.toString();
	}
}
