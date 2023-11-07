package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.model.enums.IdentityType;
import com.utavi.ledger.api.model.enums.OrgType;

public class CreateAccount {

	private String name;
	private String secret;
	private AccountType accountType;
	private IdentityType identityType;
	private OrgType orgType;
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(final String secret) {
		this.secret = secret;
	}

	public AccountType getAccountType() {
		return this.accountType;
	}

	public void setAccountType(final AccountType accountType) {
		this.accountType = accountType;
	}

	public IdentityType getIdentityType() {
		return this.identityType;
	}

	public void setIdentityType(final IdentityType identityType) {
		this.identityType = identityType;
	}

	public OrgType getOrgType() {
		return this.orgType;
	}

	public void setOrgType(final OrgType orgType) {
		this.orgType = orgType;
	}
}
