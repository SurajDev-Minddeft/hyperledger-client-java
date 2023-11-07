package com.utavi.ledger.api.service.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class OrdererInfoServiceTest {

//	private OrdererInfoService ordererInfoService;
//
//	@Mock
//	private OrdererInfoRepository ordererRepository;
//
//	@Mock
//	private OrgInfoService orgInfoService;
//
//	@Mock
//	private ResourceServerService resourceServerService;
//
//	@Mock
//	private FabricClient fabricClient;
//
//	@Captor
//	private ArgumentCaptor<Set<OrdererInfo>> ordererCaptor;
//
//	@Captor
//	private ArgumentCaptor<Properties> propertiesCaptor;
//
//	private String fabricHost;
//	private int startPort;
//
//	@Before
//	public void setup() {
//		this.fabricHost = "HOST";
//		this.startPort = 1000;
//		this.ordererInfoService = new OrdererInfoService(this.fabricHost, this.startPort, this.ordererRepository, this.orgInfoService,
//				this.resourceServerService, this.fabricClient);
//	}
//
//	@Test
//	public void whenFindOrderers_OrderersFindSuccessful_ReturnOrderers() {
//		final OrdererInfo orgOrderer = makeOrdererInfo("orderer0", this.fabricHost, false, this.startPort, makeOrgInfo(OrgType.ORDERER));
//		final List<OrdererInfo> orgOrderers = new ArrayList<>();
//		orgOrderers.add(orgOrderer);
//		final List<OrdererInfo> expectedOrgOrderers = new ArrayList<>();
//		expectedOrgOrderers.add(orgOrderer);
//		when(this.ordererRepository.findAll()).thenReturn(orgOrderers);
//		final Set<OrdererInfo> actualOrgOrderers = this.ordererInfoService.findOrderers();
//		verify(this.ordererRepository).findAll();
//		assertThat(actualOrgOrderers).containsExactlyInAnyOrder(expectedOrgOrderers.toArray(new OrdererInfo[actualOrgOrderers.size()]));
//	}
//
//	@Test
//	public void whenCreate_CreateSuccessful_CreateOrderer() {
//		final OrgInfo orgInfo = makeOrgInfo(OrgType.ORDERER);
//		final String ordererName = "orderer0";
//		final OrdererInfo expectedOrderer = makeOrdererInfo(ordererName, this.fabricHost, false, this.startPort, orgInfo);
//		when(this.orgInfoService.findOrdererOrg()).thenReturn(orgInfo);
//		when(this.ordererRepository.findByName(orgInfo.getName())).thenReturn(Optional.empty());
//		this.ordererInfoService.writeOrderers(1, false);
//		verify(this.ordererRepository).saveAll(this.ordererCaptor.capture());
//		verify(this.orgInfoService).findOrdererOrg();
//		assertThat(this.ordererCaptor.getValue()).usingElementComparatorIgnoringFields("createDate").contains(expectedOrderer);
//	}
//
//	@Test
//	public void givenTlsDisabled_thenMapOrderer_NoErrors_SuccessfullyReturnOrderer() {
//		final OrgInfo orgInfo = makeOrgInfo(OrgInfo.OrgType.PEER);
//		final OrdererInfo ordererInfo = makeOrdererInfo("orderer0", "HOST", false, 1000, orgInfo);
//		final Set<OrdererInfo> ordererInfos = new HashSet<>();
//		ordererInfos.add(ordererInfo);
//		final Orderer orderer = Mockito.mock(Orderer.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set<Orderer> expectedOrderers = new HashSet<>();
//		expectedOrderers.add(orderer);
//
//		when(this.ordererRepository.findAll()).thenReturn(new ArrayList<>(ordererInfos));
//		final Properties ordererProperties = new Properties();
//		ordererProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
//
//		when(this.fabricClient.newOrderer(ordererInfo.getName(), ordererInfo.getLocation(), ordererProperties)).thenReturn(orderer);
//
//		final Set<Orderer> actualOrderers = this.ordererInfoService.mapOrderers();
//
//		verify(this.ordererRepository).findAll();
//		verify(this.fabricClient).newOrderer(ordererInfo.getName(), ordererInfo.getLocation(), ordererProperties);
//		assertThat(actualOrderers).isEqualTo(expectedOrderers);
//	}
//
//	@Test
//	public void givenTlsEnabled_thenMapOrderer_NoErrors_SuccessfullyReturnOrderer() {
//		final OrgInfo orgInfo = makeOrgInfo(OrgInfo.OrgType.PEER);
//		final OrdererInfo ordererInfo = makeOrdererInfo("orderer0", "HOST", true, 1000, orgInfo);
//		final Set<OrdererInfo> ordererInfos = new HashSet<>();
//		ordererInfos.add(ordererInfo);
//		final Orderer orderer = Mockito.mock(Orderer.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set<Orderer> expectedOrderers = new HashSet<>();
//		expectedOrderers.add(orderer);
//		final Map<String, byte[]> adminKeys = new HashMap<>();
//		adminKeys.put("certificate", "clientCertBytes".getBytes());
//		adminKeys.put("privateKey", "clientKeyBytes".getBytes());
//		final Map<String, byte[]> keysMap = new HashMap<>();
//		keysMap.put("pemBytes", "pemBytes".getBytes());
//
//		final Map<String, byte[]> kmap = new HashMap<>();
//		kmap.put("clientCertBytes", adminKeys.get("certificate"));
//		kmap.put("clientKeyBytes", adminKeys.get("privateKey"));
//		kmap.putAll(keysMap);
//		final Properties ordererProperties = TlsUtils.makeComponentTlsProperties(ordererInfo.getName(), kmap);
//		ordererProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
//
//		when(this.ordererRepository.findAll()).thenReturn(new ArrayList<>(ordererInfos));
//		when(this.fabricClient.newOrderer(ordererInfo.getName(), ordererInfo.getLocation(), ordererProperties)).thenReturn(orderer);
//		when(this.resourceServerService.requestClientTLSKeysForOrderer(ORGANIZATION_NAME, ordererInfo.getName())).thenReturn(keysMap);
//		when(this.resourceServerService.loadAdminCertificates(ORGANIZATION_NAME)).thenReturn(adminKeys);
//
//		final Set<Orderer> actualOrderers = this.ordererInfoService.mapOrderers();
//
//		verify(this.ordererRepository).findAll();
//		verify(this.fabricClient).newOrderer(eq(ordererInfo.getName()), eq(ordererInfo.getLocation()), this.propertiesCaptor.capture());
//		verify(this.resourceServerService).requestClientTLSKeysForOrderer(ORGANIZATION_NAME, ordererInfo.getName());
//		verify(this.resourceServerService).loadAdminCertificates(ORGANIZATION_NAME);
//
//		assertThat(actualOrderers).isEqualTo(expectedOrderers);
//		assertThat(this.propertiesCaptor.getValue().get("pemBytes")).isEqualTo(ordererProperties.get("pemBytes"));
//		assertThat(this.propertiesCaptor.getValue().get("hostnameOverride")).isEqualTo(ordererProperties.get("hostnameOverride"));
//		assertThat(this.propertiesCaptor.getValue().get("ssl-target-name-override")).isEqualTo(ordererProperties.get("ssl-target-name-override"));
//		assertThat(this.propertiesCaptor.getValue().get("clientCertBytes")).isEqualTo(ordererProperties.get("clientCertBytes"));
//		assertThat(this.propertiesCaptor.getValue().get("clientKeyBytes")).isEqualTo(ordererProperties.get("clientKeyBytes"));
//		assertThat(this.propertiesCaptor.getValue().get("sslProvider")).isEqualTo(ordererProperties.get("sslProvider"));
//		assertThat(this.propertiesCaptor.getValue().get("negotiationType")).isEqualTo(ordererProperties.get("negotiationType"));
//	}

}
