package com.utavi.ledger.api.dto.response;

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WalletResponseDto {

	@NotNull
	private BigDecimal amount;

	@NotBlank
	private String walletEntryId;

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

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("amount", this.amount)
				.append("walletEntryId", this.walletEntryId)
				.toString();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final WalletResponseDto that = (WalletResponseDto) o;
		return Objects.equals(getAmount(), that.getAmount()) &&
				Objects.equals(getWalletEntryId(), that.getWalletEntryId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAmount(), getWalletEntryId());
	}
}
