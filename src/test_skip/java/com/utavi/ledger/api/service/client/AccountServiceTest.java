package com.utavi.ledger.api.service.client;

import static com.utavi.ledger.api.util.Utils.createAccountName;
import static com.utavi.ledger.data.AccountTestData.ACCOUNT_NAME;
import static com.utavi.ledger.data.AccountTestData.SILO_ADMIN_ID;
import static com.utavi.ledger.data.AccountTestData.makeBusinessUserWithSilo;
import static com.utavi.ledger.data.AccountTestData.makeBusinessUserWithoutMembership;
import static com.utavi.ledger.data.AccountTestData.makeRegistrar;
import static com.utavi.ledger.data.DtoTestData.makeJoinSiloRequestDto;
import static com.utavi.ledger.data.DtoTestData.makeLeaveSiloRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import com.utavi.ledger.api.dto.JoinSiloDto;
import com.utavi.ledger.api.dto.request.silo.LeaveSiloDto;
import com.utavi.ledger.api.exceptions.EntityNotFoundException;
import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.repository.AccountRepository;
import com.utavi.ledger.api.service.network.ca.HFCAServerService;
import com.utavi.ledger.api.util.Utils;
import com.utavi.ledger.config.channel.fabric.ChannelConfigService;
import java.util.Collections;
import java.util.Optional;
import org.hyperledger.fabric.sdk.Enrollment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Utils.class})
@PowerMockIgnore({"javax.crypto.*"})
public class AccountServiceTest {

	@InjectMocks
	private AccountService accountService;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private OrgInfoService orgInfoService;

//	@Mock
//	private ChannelInfoManager channelInfoManager;

	@Mock
	private HFCAServerService hfcaServerService;

	@Mock
	private ChannelConfigService channelConfigService;

	@Captor
	private ArgumentCaptor<Account> accountCaptor;

	@Test
	public void whenFindByName_AccountExist_ReturnAccount() {
		final String name = ACCOUNT_NAME;
		final Account expectedAccount = makeBusinessUserWithSilo();
		when(this.accountRepository.findByName(name)).thenReturn(java.util.Optional.of(expectedAccount));
		final Account actualAccount = this.accountService.findOrgByType(name);
		verify(this.accountRepository).findByName(name);
		assertThat(actualAccount).isEqualTo(expectedAccount);
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenFindByExternalId_AccountDoesntExist_ThrowException() {
		final String externalId = "externalId";
		this.accountService.findOrgByType(externalId);
	}

	@Test
	public void whenFindByEnrollmentId_AccountExist_ReturnAccount() {
		final Account expectedAccount = makeBusinessUserWithSilo();
		final String name = expectedAccount.getName();
		when(this.accountRepository.findByName(name)).thenReturn(Optional.of(expectedAccount));
		final Account actualAccount = this.accountService.findOrgByType(name);
		verify(this.accountRepository).findByName(name);
		assertThat(actualAccount).isEqualTo(expectedAccount);
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenFindByEnrollmentId_AccountDoesntExist_ThrowException() {
		final String name = "name";
		this.accountService.findOrgByType(name);
	}

	@Test
	public void whenRevoke_RevokedSuccessful_AccountIsRevoked() {
		final Account actualUser = makeBusinessUserWithSilo();
		final Account registrar = makeRegistrar();
		when(this.accountRepository.findByAccountTypeAndOrgType(AccountType.BOOTSTRAP_ACCOUNT, OrgType.PEER))
				.thenReturn(Collections.singleton(registrar));
		when(this.accountRepository.findByName(actualUser.getName())).thenReturn(Optional.of(actualUser));
//		this.accountService.revoke(actualUser.getName(), OrgType.PEER);
		verify(this.accountRepository).save(this.accountCaptor.capture());
		verify(this.hfcaServerService).revoke(registrar, actualUser, OrgType.PEER);
	}

	@Test
	public void whenReEnroll_ReEnrollSuccessful_AccountIsReEnrolled() {
		final Account actualUser = makeBusinessUserWithSilo();
		final Enrollment enrollment = Mockito.mock(Enrollment.class, RETURNS_DEEP_STUBS);
		final Account registrar = makeRegistrar();
		when(this.accountRepository.findByAccountTypeAndOrgType(AccountType.BOOTSTRAP_ACCOUNT, OrgType.PEER))
				.thenReturn(Collections.singleton(registrar));
		when(this.accountRepository.findByName(actualUser.getName())).thenReturn(Optional.of(actualUser));
		when(this.hfcaServerService.reEnroll(actualUser, OrgType.PEER)).thenReturn(enrollment);
//		this.accountService.reEnroll(actualUser.getName(), OrgType.PEER);
		verify(this.accountRepository).save(this.accountCaptor.capture());
		verify(this.hfcaServerService).reEnroll(eq(actualUser), eq(OrgType.PEER));
	}

	@Test
	public void whenJoinSilo_CorrectRequest_UserJoinSilo() {
		final Account actualUser = makeBusinessUserWithoutMembership();
		final Account registrar = makeRegistrar();
//		final OrgInfo orgInfo = actualUser.getOrgInfo();
		final JoinSiloDto joinSiloDto = makeJoinSiloRequestDto(actualUser);
//		final ChannelInfo channelInfo = makeChannelInfo();
		final Enrollment enrollment = Mockito.mock(Enrollment.class, RETURNS_DEEP_STUBS);

		final Account expectedUser = makeBusinessUserWithSilo();
		when(this.accountRepository.findByName(actualUser.getName())).thenReturn(Optional.empty());
		when(this.accountRepository.findByAccountTypeAndOrgType(AccountType.BOOTSTRAP_ACCOUNT, OrgType.PEER))
				.thenReturn(Collections.singleton(registrar));
//		when(this.channelInfoManager.findBySiloName(SILO_NAME)).thenReturn(channelInfo);
//		when(this.orgInfoService.find(OrgType.PEER)).thenReturn(orgInfo);
//		when(this.hfcaServerService.enroll(actualUser.getName(), actualUser.getEnrollmentSecret(), OrgType.PEER)).thenReturn(enrollment);
//		final Membership membership = new Membership();
//		membership.setSiloMembershipStatus(SiloMembershipStatus.APPROVED);
//		membership.setChannelInfo(channelInfo);
//		actualUser.addMembership(membership);

//		this.accountService.joinSilo(joinSiloDto);
//
//		verify(this.accountRepository).save(this.accountCaptor.capture());
//		verify(this.channelInfoManager).findBySiloName(SILO_NAME);
//		assertThat(this.accountCaptor.getValue())
//				.isEqualToIgnoringGivenFields(expectedUser, "createDate", "updateDate", "memberships", "enrollment", "encryptedEnrollment",
//						"enrollmentSecret", "enrollmentSecretWrapper");
//		final String siloName = channelInfo.getName();
//		assertEquals(this.accountCaptor.getValue().getMembership(siloName), expectedUser.getMembership(siloName));
	}

	@Test
	public void whenLeaveSilo_CorrectRequest_UserLeaveSilo() {
		mockStatic(Utils.class);
		when(createAccountName()).thenReturn(SILO_ADMIN_ID);
		when(Utils.randomEnrollmentSecret()).thenReturn("SECRET");
		final Account registrar = makeRegistrar();
		when(this.accountRepository.findByAccountTypeAndOrgType(AccountType.BOOTSTRAP_ACCOUNT, OrgType.PEER))
				.thenReturn(Collections.singleton(registrar));
		final LeaveSiloDto leaveSiloDto = makeLeaveSiloRequestDto();
		final Account actualUser = makeBusinessUserWithSilo();
//		final ChannelInfo channelInfo = makeChannelInfo();
		final Account expectedUser = makeBusinessUserWithSilo();
//		expectedUser.changeMembershipStatus(channelInfo.getSiloName(), SiloMembershipStatus.INACTIVE);
//		final UpdateCAAttributesRequest request = makeUserLeaveSiloUpdateCAIdentityRequest(actualUser, expectedUser.getJoinedMemberships());
//		when(this.accountRepository.findByName(actualUser.getName())).thenReturn(Optional.of(actualUser));
//		when(this.channelInfoManager.findBySiloName(channelInfo.getSiloName())).thenReturn(channelInfo);

//		this.accountService.leaveSilo(leaveSiloDto);

		verify(this.accountRepository).save(this.accountCaptor.capture());
//		verify(this.hfcaServerService).updateCAAttributes(eq(request), eq(OrgType.PEER), any(User.class));
//		assertThat(this.accountCaptor.getValue())
//				.isEqualToIgnoringGivenFields(expectedUser, "createDate", "updateDate", "memberships", "enrollmentSecretWrapper");
//		assertEquals(this.accountCaptor.getValue().getMembership(channelInfo.getSiloName()).get(),
//				expectedUser.getMembership(channelInfo.getSiloName()).get());
	}

	@Test
	public void whenGetAccount_NoErrors_ReturnResponseAccountDto() {
		final Account expectedAccount = makeBusinessUserWithSilo();
		when(this.accountRepository.findByName(expectedAccount.getName())).thenReturn(Optional.of(expectedAccount));

		final Account actualAccount = this.accountService.findOrgByType(expectedAccount.getName());

		verify(this.accountRepository).findByName(expectedAccount.getName());
		assertThat(actualAccount).isEqualToIgnoringGivenFields(expectedAccount, "createDate", "updateDate");
	}
}
