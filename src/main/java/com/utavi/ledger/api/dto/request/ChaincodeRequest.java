package com.utavi.ledger.api.dto.request;

import javax.validation.constraints.NotBlank;

public abstract class ChaincodeRequest {

	@NotBlank
	private String accountId;

	@NotBlank
	private String siloName;

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

	public abstract Object getPayload();
}
