package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.AccountType;
import java.util.Set;

public class AccountInfoDto {

	private String name;
	private AccountType accountType;
	private String membership;
	private Set<CertificateInfoDto> certificateInfos;

	public AccountInfoDto(final String name, final AccountType accountType, final String membership,
			final Set<CertificateInfoDto> certificateInfos) {
		this.name = name;
		this.accountType = accountType;
		this.membership = membership;
		this.certificateInfos = certificateInfos;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public AccountType getAccountType() {
		return this.accountType;
	}

	public void setAccountType(final AccountType accountType) {
		this.accountType = accountType;
	}

	public String getMembership() {
		return this.membership;
	}

	public void setMembership(final String membership) {
		this.membership = membership;
	}

	public Set<CertificateInfoDto> getCertificateInfos() {
		return this.certificateInfos;
	}

	public void setCertificateInfos(final Set<CertificateInfoDto> certificateInfos) {
		this.certificateInfos = certificateInfos;
	}
}
