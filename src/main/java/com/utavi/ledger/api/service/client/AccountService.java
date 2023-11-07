package com.utavi.ledger.api.service.client;

import com.utavi.ledger.api.dto.*;
import com.utavi.ledger.api.dto.IdentityRegistrationRequest.Builder;
import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.model.accounts.LocalEnrollment;
import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.model.enums.IdentityType;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.repository.AccountRepository;
import com.utavi.ledger.api.service.network.ca.HFCAServerService;
import com.utavi.ledger.api.repository.OrgInfoRepository;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAX509Certificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.utavi.ledger.api.util.UtilFunctions.notFoundBuilder;

@Service
@Transactional
public class AccountService {

	private final static Logger LOGGER = LoggerFactory.getLogger(AccountService.class.getName());
	public static final String DEFAULT_CA_UTAVI_ROOT_AFFILIATION = "";
	private final AccountRepository accountRepository;
	private final HFCAServerService hfcaServerService;
	private final OrgInfoRepository orgInfoRepository;
	public AccountService(final AccountRepository accountRepository, final OrgInfoRepository orgInfoRepository,
			final HFCAServerService hfcaServerService) {
		this.accountRepository = accountRepository;
		this.hfcaServerService = hfcaServerService;
		this.orgInfoRepository = orgInfoRepository;
	}

	public Set<AccountInfoDto> findAccounts() {
		return findPeerOrgAccounts().stream().map(this::getAccountInfo).collect(Collectors.toSet());
	}

	public Set<AccountInfoDto> findAccounts(final AccountType accountType) {
		return findPeerOrgAccounts(accountType).stream().map(this::getAccountInfo).collect(Collectors.toSet());
	}

	public Set<Account> findPeerOrgAccounts() {
		return new HashSet<>(this.accountRepository.findAllByOrgType(OrgType.PEER));
	}

	public Set<Account> findPeerOrgAccounts(final AccountType accountType) {
		return new HashSet<>(this.accountRepository.findByAccountTypeAndOrgType(accountType, OrgType.PEER));
	}

	public Account newAccount(final String accountName, final AccountType accountType) {
		final Account account = registerAccount(accountName, accountType);
		return this.accountRepository.save(account);
	}

	public Account findOrgByType(final String name) {
		return find(name).orElseThrow(notFoundBuilder.apply("Account", "name", name));
	}

	public AccountInfoDto findWithCAInfo(final String name) {
		final Account account = findOrgByType(name);
		return getAccountInfo(account);
	}

	private AccountInfoDto getAccountInfo(final Account account) {
		final OrgType orgType = account.getOrgType();
		final String name = account.getName();
		final Account registrar = findRegistrar(orgType);
		final Set<HFCAX509Certificate> hfcax509Certificates = this.hfcaServerService.findHFCAX509Certificates(registrar, name);
		final Set<CertificateInfoDto> certificateInfoDtos = hfcax509Certificates.stream()
				.map(HFCAX509Certificate::getX509)
				.map(x509Certificate -> new CertificateInfoDto(x509Certificate.getNotAfter(), x509Certificate.getNotBefore()))
				.collect(Collectors.toSet());
		final String membership = this.hfcaServerService.getCAAttributes(registrar, name).stream()
				.filter(attribute -> attribute.getName().equals("member.of"))
				.map(Attribute::getValue)
				.findFirst().orElse("NONE");
		account.setCertificateInfos(certificateInfoDtos);
		return new AccountInfoDto(name, account.getAccountType(), membership, certificateInfoDtos);
	}

	public void setAccountToNotActive(String accountName) {
		this.accountRepository.updateAccountSetActiveFalse(accountName);
	}


	public Optional<Account> findPeerAdmin() {
		return findOrgByType(AccountType.PEER_ADMIN).stream().findFirst();
	}

	public Account findOrdererAdmin() {
		return findOrgByType(AccountType.ORDERER_ADMIN).stream().findAny()
				.orElseThrow(notFoundBuilder.apply("Account", "accountType", AccountType.ORDERER_ADMIN));
	}

	public Account findRegistrar(final OrgType orgType) {
		var accountType = orgType == OrgType.PEER ? AccountType.PEER_ADMIN : AccountType.ORDERER_ADMIN;
		return findOrgByType(accountType, orgType).stream().findAny()
				.orElseThrow(notFoundBuilder.apply("Account", "accountType", accountType));
	}

	public Set<Account> findOrgByType(final AccountType accountType) {
		LOGGER.debug("Fetching an account of type {}", accountType);
		return this.accountRepository.findByAccountType(accountType);
	}

	public Set<Account> findOrgByType(final AccountType accountType, final OrgType orgType) {
		LOGGER.debug("Fetching an account of type {}", accountType);
		return this.accountRepository.findByAccountTypeAndOrgType(accountType, orgType);
	}

	public Optional<Account> find(final String accountName) {
		LOGGER.debug("Fetching an account by name {}", accountName);
		return this.accountRepository.findByName(accountName);
	}

	public void changeCaPassword(final String name, final ChangePasswordRequest request) {
		LOGGER.info("Received change password request for account {}", name);
		final OrgType orgType = request.getOrgType();
		final Account account = findOrgByType(name);
		account.setEnrollmentSecret(request.getPassword());
		this.hfcaServerService.changePassword(name, request.getPassword(), findRegistrar(orgType), orgType);
		this.accountRepository.save(account);
		LOGGER.info("The password for {} has been changed successfully", name);
	}

	public void registerAdmin(
			OrgType orgType,
			String bootstrapUserName,
			String bootstrapUserMsp,
			String bootstrapUserPrivateKey,
			String bootstrapUserPem) {
		AccountType adminAccountType;
		if (orgType == OrgType.PEER) {
			adminAccountType = AccountType.PEER_ADMIN;
		} else if (orgType == OrgType.ORDERER) {
			adminAccountType = AccountType.ORDERER_ADMIN;
		} else {
			throw new IllegalArgumentException(String.format("Unknown org type %s", orgType));
		}
		final Account adminAccount = newActiveAccountEntity(String.format("admin-%s", orgType.name()), adminAccountType, orgType);
		adminAccount.setEnrollmentSecret("admin-secret");
		registerIdentity(
			buildUser(bootstrapUserName, bootstrapUserPrivateKey, bootstrapUserPem, bootstrapUserMsp, ""),
			adminAccount,
			IdentityType.ADMIN,
			orgType);
		final Enrollment enrollment = this.hfcaServerService.enroll(adminAccount.getName(), adminAccount.getEnrollmentSecret(), orgType);
		LocalEnrollment localEnrollment = new LocalEnrollment(enrollment.getKey(), enrollment.getCert());
		adminAccount.setLocalEnrollment(localEnrollment);
		save(adminAccount);
		LOGGER.info("Created admin identity {}", adminAccount.getName());
	}

	private User buildUser(
			String bootstrapUserName,
			String bootstrapUserPrivateKey,
			String bootstrapUserPem,
			String bootstrapUserMsp,
			String affiliation) {
		return new User() {
			@Override
			public String getName() {
				return bootstrapUserName;
			}

			@Override
			public Set<String> getRoles() {
				return null;
			}

			@Override
			public String getAccount() {
				return null;
			}

			@Override
			public String getAffiliation() {
				return affiliation;
			}

			@Override
			public Enrollment getEnrollment() {
				return new Enrollment() {
					@Override
					public PrivateKey getKey() {
						try {
							return Identities.readPrivateKey(new StringReader(bootstrapUserPrivateKey));
						} catch (IOException | InvalidKeyException e) {
							throw new RuntimeException(e);
						}
					}

					@Override
					public String getCert() {
						return bootstrapUserPem;
					}
				};
			}

			@Override
			public String getMspId() {
				return bootstrapUserMsp;
			}
		};
	}

	private void registerIdentity(User registrar, final Account userToRegister, final IdentityType identityType, final OrgType orgType) {
		final IdentityRegistrationRequest identityRegistrationRequest = new Builder()
				.setName(userToRegister.getName())
				.setSecret(userToRegister.getEnrollmentSecret())
				.setAffiliation(DEFAULT_CA_UTAVI_ROOT_AFFILIATION)
				.setIdentityType(identityType)
				.setAccountType(userToRegister.getAccountType())
				.setMembershipInfo("")
				.build();
		this.hfcaServerService.registerUser(registrar, orgType, identityRegistrationRequest);
	}

	public void save(final Account account) {
		this.accountRepository.saveAndFlush(account);
	}

	private Account registerAccount(final String accountName, final AccountType accountType) {
		final Account account = newActiveAccountEntity(accountName, accountType, OrgType.PEER);
		final Account registrar = findRegistrar(OrgType.PEER);
		registerIdentity(registrar, account, IdentityType.USER, OrgType.PEER);
		final Enrollment enrollment = this.hfcaServerService.enroll(accountName, account.getEnrollmentSecret(), OrgType.PEER);
		LocalEnrollment localEnrollment = new LocalEnrollment(enrollment.getKey(), enrollment.getCert());
		account.setLocalEnrollment(localEnrollment);
		return account;
	}

	private Account newActiveAccountEntity(final String accountName, final AccountType accountType, final OrgType orgType) {
		final OrgInfo orgInfo = this.findOrgByType(orgType);
		final Account account = new Account(orgInfo);
		account.setName(accountName);
		account.setAccountType(accountType);
		account.setMspId(orgInfo.getMspId());
		account.setActive(true);
		return account;
	}

	public void delete(final String name) {
		LOGGER.info("Received a request to delete {}", name);
		this.accountRepository.deleteByName(name);
		LOGGER.info("Account {} has been deleted successfully", name);
	}

	public OrgInfo findOrgByType(final OrgType orgType) {
		return this.orgInfoRepository.findByType(orgType).orElseThrow(notFoundBuilder.apply("OrgInfo", "orgType", orgType));
	}

}

