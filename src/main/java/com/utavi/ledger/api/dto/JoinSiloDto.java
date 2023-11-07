package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.AccountType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JoinSiloDto {

	@NotBlank
	private String accountId;

	@NotBlank
	private String siloName;

	@NotNull
	private AccountType accountType;

	public AccountType getAccountType() {
		return this.accountType;
	}

	public void setAccountType(final AccountType accountType) {
		this.accountType = accountType;
	}

	public String getSiloName() {
		return this.siloName;
	}

	public void setSiloName(final String siloName) {
		this.siloName = siloName;
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
				.append("accountType", this.accountType)
				.append("siloName", this.siloName)
				.toString();
	}
}
