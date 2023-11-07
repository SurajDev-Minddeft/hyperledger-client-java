package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.model.enums.IdentityType;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class IdentityRegistrationRequest {

	private final String name;
	private final String secret;
	private final String affiliation;
	private final IdentityType identityType;
	private final AccountType accountType;
	private final String membershipInfo;

	private IdentityRegistrationRequest(final String name, final String secret, final String affiliation, final IdentityType identityType,
			final AccountType accountType, final String membershipInfo) {
		this.name = name;
		this.secret = secret;
		this.affiliation = affiliation;
		this.identityType = identityType;
		this.accountType = accountType;
		this.membershipInfo = membershipInfo;
	}

	public String getName() {
		return this.name;
	}

	public String getSecret() {
		return this.secret;
	}

	public String getAffiliation() {
		return this.affiliation;
	}

	public AccountType getAccountType() {
		return this.accountType;
	}

	public IdentityType getIdentityType() {
		return this.identityType;
	}

	public String getMembershipInfo() {
		return this.membershipInfo;
	}

	public static class Builder {

		private String name;
		private String secret;
		private String affiliation;
		private IdentityType identityType;
		private AccountType accountType;
		private String membershipInfo;

		public Builder setMembershipInfo(final String membershipInfo) {
			this.membershipInfo = membershipInfo;
			return this;
		}

		public Builder setName(final String name) {
			this.name = name;
			return this;
		}

		public Builder setSecret(final String secret) {
			this.secret = secret;
			return this;
		}

		public Builder setAffiliation(final String affiliation) {
			this.affiliation = affiliation;
			return this;
		}

		public Builder setIdentityType(final IdentityType identityType) {
			this.identityType = identityType;
			return this;
		}

		public Builder setAccountType(final AccountType accountType) {
			this.accountType = accountType;
			return this;
		}

		public IdentityRegistrationRequest build() {
			return new IdentityRegistrationRequest(this.name, this.secret, this.affiliation, this.identityType, this.accountType, this.membershipInfo);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", this.name)
				.append("secret", this.secret)
				.append("affiliation", this.affiliation)
				.append("identityType", this.identityType)
				.append("accountType", this.accountType)
				.append("membershipInfo", this.membershipInfo)
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
		final IdentityRegistrationRequest that = (IdentityRegistrationRequest) o;
		return Objects.equals(this.name, that.name) &&
				Objects.equals(this.secret, that.secret) &&
				Objects.equals(this.membershipInfo, that.membershipInfo) &&
				Objects.equals(this.affiliation, that.affiliation) &&
				this.identityType == that.identityType &&
				this.accountType == that.accountType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.secret, this.affiliation, this.identityType, this.accountType, this.membershipInfo);
	}
}
