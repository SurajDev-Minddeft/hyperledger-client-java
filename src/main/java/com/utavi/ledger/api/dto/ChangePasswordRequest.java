package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.OrgType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChangePasswordRequest {

	@NotNull
	private OrgType orgType;

	@NotBlank
	private String password;

	public OrgType getOrgType() {
		return this.orgType;
	}

	public void setOrgType(final OrgType orgType) {
		this.orgType = orgType;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
}
