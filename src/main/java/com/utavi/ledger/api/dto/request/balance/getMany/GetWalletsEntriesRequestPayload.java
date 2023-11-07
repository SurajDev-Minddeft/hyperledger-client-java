package com.utavi.ledger.api.dto.request.balance.getMany;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetWalletsEntriesRequestPayload {

	@NotBlank
	private String accountId;

	@NotEmpty
	private Map<String, List<String>> wallets;

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(final String accountId) {
		this.accountId = accountId;
	}

	public Map<String, List<String>> getWallets() {
		return this.wallets;
	}

	public void setWallets(final Map<String, List<String>> wallets) {
		this.wallets = wallets;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("accountId", this.accountId)
				.append("wallets", this.wallets)
				.toString();
	}
}
