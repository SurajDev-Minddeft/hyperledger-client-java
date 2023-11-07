package com.utavi.ledger.api.dto.request.balance.move;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MoveBalancePayload {

	@NotBlank
	@JsonFormat(shape = Shape.STRING)
	private BigDecimal amount;

	@NotBlank
	private String fromWalletEntry;

	@NotBlank
	private String toWalletEntry;

	@NotBlank
	private String creditId;

	@NotBlank
	private String fromAccountId;

	@NotBlank
	private String toAccountId;

	public String getFromAccountId() {
		return this.fromAccountId;
	}

	public void setFromAccountId(final String fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public String getToAccountId() {
		return this.toAccountId;
	}

	public void setToAccountId(final String toAccountId) {
		this.toAccountId = toAccountId;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public String getFromWalletEntry() {
		return this.fromWalletEntry;
	}

	public void setFromWalletEntry(final String fromWalletEntry) {
		this.fromWalletEntry = fromWalletEntry;
	}

	public String getToWalletEntry() {
		return this.toWalletEntry;
	}

	public void setToWalletEntry(final String toWalletEntry) {
		this.toWalletEntry = toWalletEntry;
	}

	public String getCreditId() {
		return this.creditId;
	}

	public void setCreditId(final String creditId) {
		this.creditId = creditId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("amount", this.amount)
				.append("fromWalletEntry", this.fromWalletEntry)
				.append("toWalletEntry", this.toWalletEntry)
				.append("creditId", this.creditId)
				.append("fromAccountId", this.fromAccountId)
				.append("toAccountId", this.toAccountId)
				.toString();
	}
}
