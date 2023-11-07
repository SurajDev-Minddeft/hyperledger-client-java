package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.AccountType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateOrganizationAdminDto {

	@NotNull
	private Long orgId;

	@NotBlank
	private String name;

	@NotNull
	private AccountType accountType;

	public AccountType getAccountType() {
		return this.accountType;
	}

	public void setAccountType(final AccountType accountType) {
		this.accountType = accountType;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setOrgId(final Long orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("orgId", this.orgId)
				.append("name", this.name)
				.toString();
	}
}
