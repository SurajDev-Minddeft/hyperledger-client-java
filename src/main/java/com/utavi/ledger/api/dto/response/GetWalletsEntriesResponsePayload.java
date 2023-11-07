package com.utavi.ledger.api.dto.response;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetWalletsEntriesResponsePayload {

	@NotBlank
	private String accountId;

	@NotEmpty
	private Map<String, List<WalletResponseDto>> balances;

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(final String accountId) {
		this.accountId = accountId;
	}

	public Map<String, List<WalletResponseDto>> getBalances() {
		return this.balances;
	}

	public void setBalances(final Map<String, List<WalletResponseDto>> balances) {
		this.balances = balances;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("accountId", this.accountId)
				.append("balances", this.balances)
				.toString();
	}
}
