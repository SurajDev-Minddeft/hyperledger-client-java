package com.utavi.ledger.api.service.client;

import static com.utavi.ledger.data.OrgInfoTestData.ORGANIZATION_NAME;
import static com.utavi.ledger.data.OrgInfoTestData.ROOT_AFFILIATION;
import static com.utavi.ledger.data.OrgInfoTestData.makeOrgInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.service.network.ResourceServerService;
import com.utavi.ledger.api.dto.manage.NetworkConfigDto;
import com.utavi.ledger.api.dto.manage.NetworkConfigDto.OrgConfig;
import com.utavi.ledger.api.repository.OrgInfoRepository;
import com.utavi.ledger.api.service.manage.OrganizationConfigManagerService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class OrgInfoServiceTest {

	@InjectMocks
	private OrganizationConfigManagerService configManager;

	@Mock
	private OrgInfoRepository orgInfoRepository;

	@Mock
	private ResourceServerService resourceServerService;

	@Captor
	private ArgumentCaptor<List<OrgInfo>> organizationCaptor;

	@Test
	public void whenFindAllOrganizations_FindSuccessful_ReturnOrganizations() {
		final OrgInfo orgInfo = makeOrgInfo(ORGANIZATION_NAME, OrgType.PEER, false);
		final List<OrgInfo> expectedOrgInfos = new ArrayList<>();
		expectedOrgInfos.add(orgInfo);
		when(this.orgInfoRepository.findAll()).thenReturn(expectedOrgInfos);
		final List<OrgInfo> actualOrgInfos = this.configManager.findAll();
		verify(this.orgInfoRepository).findAll();
		assertThat(actualOrgInfos).isEqualTo(expectedOrgInfos);
	}

	@Test
	public void whenFindOrganizationByName_OrganizationExist_ReturnOrganization() {
		final OrgInfo expectedOrgInfo = makeOrgInfo(ORGANIZATION_NAME, OrgType.PEER, false);
		when(this.orgInfoRepository.findByName(ORGANIZATION_NAME)).thenReturn(java.util.Optional.of(expectedOrgInfo));
		final OrgInfo actualOrgInfo = this.configManager.find(ORGANIZATION_NAME);
		verify(this.orgInfoRepository).findByName(ORGANIZATION_NAME);
		assertThat(actualOrgInfo).isEqualTo(expectedOrgInfo);
	}

	@Test
	public void whenFindPeerOrganization_OrganizationExist_ReturnPeerOrganization() {
		final OrgInfo expectedOrgInfo = makeOrgInfo(ORGANIZATION_NAME, OrgType.PEER, false);
		when(this.orgInfoRepository.findByType(OrgType.PEER)).thenReturn(java.util.Optional.of(expectedOrgInfo));
		final OrgInfo actualOrgInfo = this.configManager.findPeerOrg();
		verify(this.orgInfoRepository).findByType(OrgType.PEER);
		assertThat(actualOrgInfo).isEqualTo(expectedOrgInfo);
	}

	@Test
	public void whenFindOrdererOrganization_OrganizationExist_ReturnOrdererOrganization() {
		final OrgInfo expectedOrgInfo = makeOrgInfo(ORGANIZATION_NAME, OrgType.ORDERER, false);
		when(this.orgInfoRepository.findByType(OrgType.ORDERER)).thenReturn(java.util.Optional.of(expectedOrgInfo));
		final OrgInfo actualOrgInfo = this.configManager.findOrdererOrg();
		verify(this.orgInfoRepository).findByType(OrgType.ORDERER);
		assertThat(actualOrgInfo).isEqualTo(expectedOrgInfo);
	}

	@Test
	public void whenCreateOrganization_CreateSuccessful_CreateOrganization() {
		final NetworkConfigDto networkConfigDto = new NetworkConfigDto();
		networkConfigDto.setTls(false);
		networkConfigDto.setVersionHash("VERSION_HASH");
		final String ordererOrgName = "ordererOrg";
		final OrgConfig ordererOrg = createOrgConfig(ordererOrgName);
		final String peerOrgName = "peerOrg";
		final OrgConfig peerOrg = createOrgConfig(peerOrgName);
		networkConfigDto.setOrdererOrgs(ordererOrg);
		networkConfigDto.setPeerOrgs(peerOrg);
		when(this.resourceServerService.getNetworkConfig()).thenReturn(networkConfigDto);
		final OrgInfo expectedOrdererOrg = makeOrgInfo(ordererOrgName, OrgType.ORDERER, false);
		final OrgInfo expectedPeerOrg = makeOrgInfo(peerOrgName, OrgType.PEER, false);
		when(this.orgInfoRepository.findByNameAndType(OrgType.ORDERER, expectedOrdererOrg.getName())).thenReturn(Optional.empty());
		when(this.orgInfoRepository.findByNameAndType(OrgType.PEER, expectedPeerOrg.getName())).thenReturn(Optional.empty());
		this.configManager.writeOrgConfig();
		final ArrayList<OrgInfo> organizations = new ArrayList<>();
		organizations.add(expectedPeerOrg);
		organizations.add(expectedOrdererOrg);
		verify(this.orgInfoRepository).saveAll(this.organizationCaptor.capture());
		assertThat(this.organizationCaptor.getValue()).usingElementComparatorIgnoringFields("id", "createDate").containsAll(organizations);
	}

	private OrgConfig createOrgConfig(final String orgName) {
		final OrgConfig orgConfig = new OrgConfig();
		orgConfig.setNumberOfNodes(2);
		orgConfig.setIcaNum(1);
		orgConfig.setRootAffiliation(orgName + ROOT_AFFILIATION);
		orgConfig.setName(orgName);
		return orgConfig;
	}

}
