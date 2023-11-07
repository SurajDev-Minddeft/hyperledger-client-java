package com.utavi.ledger.api.model;

import com.utavi.ledger.api.dto.AccountInfoDto;
import com.utavi.ledger.api.dto.CertificateInfoDto;
import com.utavi.ledger.api.model.accounts.LocalEnrollment;
import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.model.enums.OrgType;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.modelmapper.PropertyMap;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.utavi.ledger.api.util.Utils.randomEnrollmentSecret;

@Entity
@Table(name = "lc_account")
@EntityListeners(AccountEntityListener.class)
public class Account extends BaseEntity implements User {

	static class EnrollmentSecretWrapper {

		private final String secret;

		EnrollmentSecretWrapper(final String secret) {
			if (StringUtils.isBlank(secret)) {
				throw new IllegalArgumentException("enrollment secret cannot be null for EnrollmentWrapper");
			}
			this.secret = secret;
		}
	}

	private Account() {
		this.enrollmentSecretWrapper = new EnrollmentSecretWrapper(randomEnrollmentSecret());
	}

	public Account(final OrgInfo orgInfo) {
		if (orgInfo == null) {
			throw new IllegalArgumentException("Org info cannot be null");
		}
		this.orgInfo = orgInfo;
		this.affiliation = orgInfo.getRootAffiliation();
		this.enrollmentSecretWrapper = new EnrollmentSecretWrapper(randomEnrollmentSecret());
	}

	@Column(name = "affiliation")
	private String affiliation;

	@NotBlank
	@Column(name = "msp_id")
	private String mspId;

	@NotBlank
	@Column(name = "fabric_account")
	private String fabricAccount;

	@NotNull
	@Column(name = "account_type")
	private AccountType accountType;

	@Column(name = "encrypted_enrollment", columnDefinition = "text")
	private String encryptedEnrollment;

	@Transient
	private Set<String> roles = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "organization_id")
	protected OrgInfo orgInfo;

	@NotBlank
	@Column(name = "enrollment_secret")
	private String encryptedEnrollmentSecret;

	@Transient
	private LocalEnrollment localEnrollment;

	@Transient
	private Set<CertificateInfoDto> certificateInfos;

	@Transient
	private EnrollmentSecretWrapper enrollmentSecretWrapper;

	@Column(name = "is_active")
	private boolean isActive;

	public String getEnrollmentSecret() {
		if (this.enrollmentSecretWrapper == null) {
			return null;
		}
		return this.enrollmentSecretWrapper.secret;
	}

	public String getOrgName() {
		return this.orgInfo.name;
	}

	public OrgType getOrgType() {
		return this.orgInfo.getOrgType();
	}

	public void setEnrollmentSecret(final String runtimeSecret) {
		this.enrollmentSecretWrapper = new EnrollmentSecretWrapper(runtimeSecret);
	}

	String getEncryptedEnrollment() {
		return this.encryptedEnrollment;
	}

	public void setEncryptedEnrollment(final String encryptedEnrollment) {
		this.encryptedEnrollment = encryptedEnrollment;
	}

	public void setLocalEnrollment(final LocalEnrollment localEnrollment) {
		this.encryptedEnrollment = null;
		this.localEnrollment = localEnrollment;
	}

	void setEncryptedEnrollmentSecret(final String encryptedEnrollmentSecret) {
		this.encryptedEnrollmentSecret = encryptedEnrollmentSecret;
	}

	@Override
	public String getAffiliation() {
		return this.affiliation;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
		setFabricAccount(name);
	}

	String getEncryptedEnrollmentSecret() {
		return this.encryptedEnrollmentSecret;
	}

	@Override
	public Enrollment getEnrollment() {
		return localEnrollment.toEnrollment();

	}
	public void setRoles(Set<String> roles) {
		 this.roles  = roles;
	}

	public LocalEnrollment getLocalEnrollment() {
		return this.localEnrollment;
	}

	@Override
	public Set<String> getRoles() {
		return this.roles;
	}

	@Override
	public String getAccount() {
		return this.fabricAccount;
	}

	@Override
	public String getMspId() {
		return this.mspId;
	}

	public AccountType getAccountType() {
		return this.accountType;
	}

	public boolean isUserAccount() {
		return this.accountType == AccountType.BUSINESS || this.accountType == AccountType.INVESTOR;
	}

	public boolean isUserBootstrapAccount() {
		return this.accountType == AccountType.BOOTSTRAP_ACCOUNT;
	}

	private void setFabricAccount(final String fabricAccount) {
		this.fabricAccount = fabricAccount;
	}

	public void setAccountType(final AccountType accountType) {
		this.accountType = accountType;
		if (this.accountType == AccountType.BUSINESS || this.accountType == AccountType.INVESTOR) {
			this.affiliation = this.affiliation + ".client";
		}
	}

	public void setMspId(final String mspId) {
		this.mspId = mspId;
	}

	public Set<CertificateInfoDto> getCertificateInfos() {
		return this.certificateInfos;
	}

	public void setCertificateInfos(final Set<CertificateInfoDto> certificateInfos) {
		this.certificateInfos = certificateInfos;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public static PropertyMap<Account, AccountInfoDto> mappedFields = new PropertyMap<Account, AccountInfoDto>() {
		protected void configure() {
			map().setAccountType(this.source.getAccountType());
			map().setName(this.source.getName());
			map().setCertificateInfos(this.source.getCertificateInfos());
		}
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Account account = (Account) o;
		return isActive == account.isActive && Objects.equals(affiliation, account.affiliation) && Objects.equals(mspId, account.mspId) && Objects.equals(fabricAccount, account.fabricAccount) && accountType == account.accountType && Objects.equals(encryptedEnrollment, account.encryptedEnrollment) && Objects.equals(roles, account.roles) && Objects.equals(orgInfo, account.orgInfo) && Objects.equals(encryptedEnrollmentSecret, account.encryptedEnrollmentSecret) && Objects.equals(localEnrollment, account.localEnrollment) && Objects.equals(certificateInfos, account.certificateInfos) && Objects.equals(enrollmentSecretWrapper, account.enrollmentSecretWrapper);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), affiliation, mspId, fabricAccount, accountType, encryptedEnrollment, roles, orgInfo, encryptedEnrollmentSecret, localEnrollment, certificateInfos, enrollmentSecretWrapper, isActive);
	}

	@Override
	public String toString() {
		return "Account{" +
				   "affiliation='" + affiliation + '\'' +
				   ", mspId='" + mspId + '\'' +
				   ", fabricAccount='" + fabricAccount + '\'' +
				   ", accountType=" + accountType +
				   ", encryptedEnrollment='" + encryptedEnrollment + '\'' +
				   ", roles=" + roles +
				   ", orgInfo=" + orgInfo +
				   ", encryptedEnrollmentSecret='" + encryptedEnrollmentSecret + '\'' +
				   ", localEnrollment=" + localEnrollment +
				   ", certificateInfos=" + certificateInfos +
				   ", enrollmentSecretWrapper=" + enrollmentSecretWrapper +
				   ", isActive=" + isActive +
				   ", id=" + id +
				   ", name='" + name + '\'' +
				   '}';
	}
}
