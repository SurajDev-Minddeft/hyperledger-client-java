package com.utavi.ledger.api.service.client;

import static com.utavi.ledger.data.CATestData.makeCAServerInfo;
import static com.utavi.ledger.data.OrgInfoTestData.ORGANIZATION_NAME;
import static com.utavi.ledger.data.OrgInfoTestData.makeOrgInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utavi.ledger.api.model.CAServerInfo;
import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.repository.CAServerInfoRepository;
import com.utavi.ledger.api.service.manage.OrganizationConfigManagerService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CAServerInfoServiceTest {

	private CAServerInfoService caServerInfoService;

	@Mock
	private CAServerInfoRepository caServerInfoRepository;

	@Mock
	private OrganizationConfigManagerService organizationConfigManagerService;

	@Captor
	private ArgumentCaptor<Set<CAServerInfo>> caServerCaptor;

	private String fabricHost;
	private int peerIcaStartPort;
	private int ordererIcaStartPort;

	@Before
	public void setup() {
		this.fabricHost = "HOST";
		this.peerIcaStartPort = 1000;
		this.ordererIcaStartPort = 2000;
		this.caServerInfoService = new CAServerInfoService(this.fabricHost, this.ordererIcaStartPort, this.peerIcaStartPort,
				this.caServerInfoRepository, this.organizationConfigManagerService);
	}

	@Test
	public void whenFindCAServers_CAServerExist_ReturnCAServer() {
		final CAServerInfo caServerInfo = makeCAServerInfo("ica0", this.fabricHost, false, this.peerIcaStartPort, makeOrgInfo(ORGANIZATION_NAME, OrgType.PEER, false));
		final List<CAServerInfo> caServerInfos = new ArrayList<>();
		caServerInfos.add(caServerInfo);
		final List<CAServerInfo> expectedCaServerInfos = new ArrayList<>();
		expectedCaServerInfos.add(caServerInfo);
		when(this.caServerInfoRepository.findAll()).thenReturn(caServerInfos);
		final List<CAServerInfo> actualCaServerInfos = this.caServerInfoService.findCAServers();
		verify(this.caServerInfoRepository).findAll();
		assertThat(actualCaServerInfos).isEqualTo(expectedCaServerInfos);
	}

	@Test
	public void whenCreate_CreationSuccessful_CreateCAServer() {
		final OrgInfo orgInfo = Mockito.mock(OrgInfo.class, Mockito.RETURNS_DEEP_STUBS);
		when(this.organizationConfigManagerService.find(any(OrgType.class))).thenReturn(orgInfo);
		when(orgInfo.getOrgType()).thenReturn(OrgType.ORDERER);
		final CAServerInfo expectedCaServerInfo = makeCAServerInfo("ica-" + orgInfo.getOrgType().name().toLowerCase() + "0", this.fabricHost, false,
				this.ordererIcaStartPort, orgInfo);
		this.caServerInfoService.writeServerConfig(OrgType.ORDERER, 1, false);
		verify(this.caServerInfoRepository).saveAll(this.caServerCaptor.capture());
		verify(this.organizationConfigManagerService).find(any(OrgType.class));
		assertThat(this.caServerCaptor.getValue()).usingElementComparatorIgnoringFields("createDate").contains(expectedCaServerInfo);
	}

}
