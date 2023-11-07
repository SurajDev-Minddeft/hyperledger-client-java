package com.utavi.ledger.api.service.network;

import static com.utavi.ledger.data.AccountTestData.ACCOUNT_NAME;
import static com.utavi.ledger.data.AccountTestData.makeBusinessUserWithSilo;
import static com.utavi.ledger.data.AccountTestData.makePeerAdmin;
import static com.utavi.ledger.data.DtoTestData.makeClientRegistrationRequest;
import static com.utavi.ledger.data.DtoTestData.makeUserIdentityRegistrationRequest;
import static com.utavi.ledger.data.DtoTestData.makeUserLeaveSiloUpdateCAIdentityRequest;
import static com.utavi.ledger.data.OrgInfoTestData.ORGANIZATION_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import com.utavi.ledger.api.dto.IdentityRegistrationRequest;
import com.utavi.ledger.api.dto.MembershipStatus;
import com.utavi.ledger.api.dto.UpdateCAAttributesRequest;
import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.model.OrganizationAdminEnrollment;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.service.misc.CryptoUtils;
import com.utavi.ledger.api.service.network.ca.HFCAClientService;
import com.utavi.ledger.api.service.network.ca.HFCAServerService;
import com.utavi.ledger.api.util.Utils;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAIdentity;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.IdentityException;
import org.hyperledger.fabric_ca.sdk.exception.RevocationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HFClient.class, CryptoSuite.class, Utils.class, HFCAServerService.class})
@PowerMockIgnore({"javax.crypto.*"})
public class HFCAServerInfoServiceTest {

	@InjectMocks
	private HFCAServerService hfCAServerService;

	@Mock
	private HFCAClientService hfcaClientService;

	@Mock
	private HFCAClient hfcaClient;

	@Mock
	private HFCAIdentity hfcaIdentity;

	@Mock
	private CryptoUtils cryptoUtils;

	@Captor
	private ArgumentCaptor<RegistrationRequest> captorRegistrationRequest;

	@Test
	public void whenRegisterNetworkIdentity_NoErrors_IdentitySuccessfullyRegistered() throws Exception {
		final User mockedRegistrar = Mockito.mock(User.class, Mockito.RETURNS_DEEP_STUBS);
		final IdentityRegistrationRequest userRegistrationRequest = makeUserIdentityRegistrationRequest(makeBusinessUserWithSilo());
		final Collection<HFCAIdentity> hfcaIdentities = new ArrayList<>();
		final RegistrationRequest expectedRegistrationRequest = makeClientRegistrationRequest(userRegistrationRequest);
		final OrgType orgType = OrgType.PEER;

		when(this.hfcaClientService.createFabricCAClient(orgType)).thenReturn(this.hfcaClient);
		when(this.hfcaClient.getHFCAIdentities(mockedRegistrar)).thenReturn(hfcaIdentities);

		this.hfCAServerService.registerUser(mockedRegistrar, orgType, userRegistrationRequest);

		verify(this.hfcaClientService).createFabricCAClient(orgType);
		verify(this.hfcaClient).getHFCAIdentities(mockedRegistrar);
		verify(this.hfcaClient).register(this.captorRegistrationRequest.capture(), eq(mockedRegistrar));

		final RegistrationRequest actualRegistrationRequest = this.captorRegistrationRequest.getValue();

		assertThat(actualRegistrationRequest).isEqualToIgnoringGivenFields(expectedRegistrationRequest, "attrs");

		final Iterator expectedAttribute = expectedRegistrationRequest.getAttributes().iterator();
		for (final Attribute actualAttribute : actualRegistrationRequest.getAttributes()) {
			assertThat(actualAttribute).isEqualToComparingFieldByField(expectedAttribute.next());
		}
	}

	@Test
	public void whenUpdateCAAttribute_NoErrors_SuccessfullyUpdate()
			throws IdentityException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {
		final OrgInfo mockedOrgInfo = Mockito.mock(OrgInfo.class, Mockito.RETURNS_DEEP_STUBS);
		final Account registrar = makePeerAdmin();
		final String attrValue = "SILO_NAME".concat(":").concat(MembershipStatus.INACTIVE.name());
		final UpdateCAAttributesRequest updateCAAttributesRequest = makeUserLeaveSiloUpdateCAIdentityRequest(registrar, attrValue);
		final Set<Attribute> actualAttributes = new HashSet<>();
		final Set<Attribute> expectedAttributes = new HashSet<>();
		final OrgType orgType = OrgType.PEER;
		expectedAttributes.add(new Attribute("member.of", attrValue, Boolean.TRUE));

		when(this.hfcaClientService.createFabricCAClient(orgType)).thenReturn(this.hfcaClient);
		when(this.hfcaClient.newHFCAIdentity(updateCAAttributesRequest.getAccountName())).thenReturn(this.hfcaIdentity);
		when(mockedOrgInfo.getName()).thenReturn(ORGANIZATION_NAME);
		when(this.hfcaIdentity.getAttributes()).thenReturn(actualAttributes);

		this.hfCAServerService.updateCAAttributes(updateCAAttributesRequest, orgType, registrar);

		verify(this.hfcaIdentity).getAttributes();
		verify(this.hfcaIdentity).update(registrar);

		final Iterator expectedAttribute = expectedAttributes.iterator();
		for (final Attribute actualAttribute : this.hfcaIdentity.getAttributes()) {
			assertThat(actualAttribute).isEqualToComparingFieldByField(expectedAttribute.next());
		}
	}

	@Test
	public void whenSetEnrollment_SetForPeerAdmin_SuccessfullySetEnrollment() throws Exception {
		final Account actualPeerAdmin = makePeerAdmin();
		final Map<String, byte[]> stringMap = new HashMap<>();
		stringMap.put("certificate", "certificate".getBytes());
		stringMap.put("privateKey", "privateKey".getBytes());
		final PrivateKey mockedPrivateKey = Mockito.mock(PrivateKey.class, Mockito.RETURNS_DEEP_STUBS);
		mockStatic(Utils.class);
		final OrganizationAdminEnrollment mockedOrganizationAdminEnrollment = createMock(OrganizationAdminEnrollment.class);

		when(this.resourceServerService.loadAdminCertificates(ORGANIZATION_NAME)).thenReturn(stringMap);
		when(Utils.getCertificateFromBytes(stringMap.get("certificate"))).thenReturn(java.util.Optional.of("certificate"));
		when(Utils.getPrivateKeyFromBytes(stringMap.get("privateKey"))).thenReturn(Optional.ofNullable(mockedPrivateKey));
		expectNew(OrganizationAdminEnrollment.class, mockedPrivateKey, "certificate").andReturn(mockedOrganizationAdminEnrollment);
		replay(mockedOrganizationAdminEnrollment, OrganizationAdminEnrollment.class);

		this.hfCAServerService.enrollBaseAdmin(actualPeerAdmin.getName(), ORGANIZATION_NAME);

		verify(this.resourceServerService).loadAdminCertificates(ORGANIZATION_NAME);
		verifyStatic(Utils.class);
		Utils.getCertificateFromBytes(stringMap.get("certificate"));
		verifyStatic(Utils.class);
		Utils.getPrivateKeyFromBytes(stringMap.get("privateKey"));
		PowerMock.verify(mockedOrganizationAdminEnrollment, OrganizationAdminEnrollment.class);
	}

	@Test
	public void whenRevoke_NoErrors_RevokEntity() throws org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, RevocationException {
		final Account registrar = makePeerAdmin();
		final Account mockedRevokeeBusinessUser = Mockito.mock(Account.class, Mockito.RETURNS_DEEP_STUBS);
		final Enrollment mockedEnrollment = Mockito.mock(Enrollment.class, Mockito.RETURNS_DEEP_STUBS);
		final OrgType orgType = OrgType.PEER;

		when(mockedRevokeeBusinessUser.getName()).thenReturn(ACCOUNT_NAME);
		when(mockedRevokeeBusinessUser.getEnrollment()).thenReturn(mockedEnrollment);
		when(this.hfcaClientService.createFabricCAClient(any(OrgType.class))).thenReturn(this.hfcaClient);

		this.hfCAServerService.revoke(registrar, mockedRevokeeBusinessUser, orgType);

		verify(this.hfcaClientService).createFabricCAClient(any(OrgType.class));
		verify(this.hfcaClient).revoke(registrar, mockedRevokeeBusinessUser.getEnrollment(), "", true);
	}
}
