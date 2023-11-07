package com.utavi.ledger.api.dto.request.silo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LeaveSiloDto {

	@NotBlank
	private String siloName;

	@NotNull
	private String accountId;

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
				.append("siloName", this.siloName)
				.append("accountId", this.accountId)
				.toString();
	}
}
