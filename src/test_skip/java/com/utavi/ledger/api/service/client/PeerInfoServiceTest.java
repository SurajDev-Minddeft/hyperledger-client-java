package com.utavi.ledger.api.service.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PeerInfoServiceTest {

//	private PeerInfoService peerService;
//
//	@Mock
//	private PeerInfoRepository peerRepository;
//
//	@Mock
//	private OrgInfoService orgInfoService;
//
//	@Captor
//	private ArgumentCaptor<Set<PeerInfo>> peerCaptor;
//
//	@Mock
//	private ResourceServerService resourceServerService;
//
//	@Mock
//	private FabricClient fabricClient;
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
//		this.peerService = new PeerInfoService(this.fabricHost, this.startPort, this.peerRepository, this.orgInfoService, this.resourceServerService,
//				this.fabricClient);
//	}
//
//	@Test
//	public void whenFindPeers_PeersExist_ReturnPeers() {
//		final PeerInfo peer = makePeerInfo("peer0", this.fabricHost, false, this.startPort, makeOrgInfo(OrgType.PEER));
//		final List<PeerInfo> peerInfos = new ArrayList<>();
//		peerInfos.add(peer);
//		final List<PeerInfo> expectedPeers = new ArrayList<>();
//		expectedPeers.add(peer);
//		when(this.peerRepository.findAll()).thenReturn(peerInfos);
//		final Set<PeerInfo> actualPeers = this.peerService.findPeers();
//		verify(this.peerRepository).findAll();
//		assertThat(actualPeers).containsExactlyInAnyOrder(expectedPeers.toArray(new PeerInfo[actualPeers.size()]));
//	}
//
//	@Test
//	public void whenWritePeers_WriteSuccessful_CreatePeer() {
//		final OrgInfo orgInfo = makeOrgInfo(OrgType.PEER);
//		final PeerInfo expectedPeer = makePeerInfo("peer0", this.fabricHost, false, this.startPort, orgInfo);
//		when(this.orgInfoService.findPeerOrg()).thenReturn(orgInfo);
//		this.peerService.writePeers(1, false);
//		verify(this.peerRepository).saveAll(this.peerCaptor.capture());
//		verify(this.orgInfoService).findPeerOrg();
//		assertThat(this.peerCaptor.getValue()).usingElementComparatorIgnoringFields("createDate").contains(expectedPeer);
//	}
//
//	@Test
//	public void whenFindByPeerNames_PeersExist_ReturnPeers() {
//		final PeerInfo peer = makePeerInfo("peer0", this.fabricHost, false, this.startPort, makeOrgInfo(OrgType.PEER));
//		final Set<PeerInfo> peerInfos = new LinkedHashSet<>();
//		peerInfos.add(peer);
//		final Set<String> peerNames = new HashSet<>();
//		peerNames.add(peer.getName());
//		final Set<PeerInfo> expectedPeers = new LinkedHashSet<>();
//		expectedPeers.add(peer);
//		when(this.peerRepository.findAllByName(peerNames)).thenReturn(peerInfos);
//		final Set<PeerInfo> actualPeers = this.peerService.findAllByName(peerNames);
//		verify(this.peerRepository).findAllByName(peerNames);
//		assertThat(actualPeers).isEqualTo(expectedPeers);
//	}
//
//	@Test
//	public void givenTlsDisabled_thenMapAllPeers_NoErrors_SuccessfullyReturnPeersSet() throws Exception {
//		final OrgInfo orgInfo = makeOrgInfo(OrgInfo.OrgType.PEER);
//		final PeerInfo peerInfo = makePeerInfo("peer0", "HOST", false, 1000, orgInfo);
//		final Set<PeerInfo> peerInfos = new HashSet<>();
//		peerInfos.add(peerInfo);
//		final Peer peer = Mockito.mock(Peer.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set<Peer> expectedPeers = new HashSet<>();
//		expectedPeers.add(peer);
//		final Properties properties = new Properties();
//		properties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
//		when(this.peerRepository.findAll()).thenReturn(new ArrayList<>(peerInfos));
//		when(this.fabricClient.newPeer(peerInfo.getName(), peerInfo.getLocation(), properties)).thenReturn(peer);
//
//		final Set<Peer> actualPears = this.peerService.mapAllPeers();
//
//		verify(this.peerRepository).findAll();
//		when(this.fabricClient.newPeer(peerInfo.getName(), peerInfo.getLocation(), properties)).thenReturn(peer);
//		assertThat(actualPears).isEqualTo(expectedPeers);
//	}
//
//	@Test
//	public void givenTlsEnable_thenMapAllPeers_NoErrors_SuccessfullyReturnPeersSet() {
//		final OrgInfo orgInfo = makeOrgInfo(OrgInfo.OrgType.PEER);
//		final PeerInfo peerInfo = makePeerInfo("peer0", "HOST", true, 1000, orgInfo);
//		final Set<PeerInfo> peerInfos = new HashSet<>();
//		peerInfos.add(peerInfo);
//		final Peer peer = Mockito.mock(Peer.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set<Peer> expectedPeers = new HashSet<>();
//		expectedPeers.add(peer);
//		final Map<String, byte[]> keysMap = new HashMap<>();
//		keysMap.put("pemBytes", "pemBytes".getBytes());
//		keysMap.put("clientCertBytes", "clientCertBytes".getBytes());
//		keysMap.put("clientKeyBytes", "clientKeyBytes".getBytes());
//		final Properties peerProperties = TlsUtils.makeComponentTlsProperties(peerInfo.getName(), keysMap);
//		peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
//		when(this.peerRepository.findAll()).thenReturn(new ArrayList<>(peerInfos));
//		when(this.fabricClient.newPeer(peerInfo.getName(), peerInfo.getLocation(), peerProperties)).thenReturn(peer);
//		when(this.resourceServerService.requestClientTLSKeys(ORGANIZATION_NAME, peerInfo.getName())).thenReturn(keysMap);
//
//		final Set<Peer> actualPears = this.peerService.mapAllPeers();
//
//		verify(this.peerRepository).findAll();
//		verify(this.fabricClient).newPeer(eq(peerInfo.getName()), eq(peerInfo.getLocation()), this.propertiesCaptor.capture());
//		verify(this.resourceServerService).requestClientTLSKeys(ORGANIZATION_NAME, peerInfo.getName());
//		assertThat(actualPears).isEqualTo(expectedPeers);
//	}
//
//	@Test
//	public void givenTlsDisabled_thenMapPeers_NoErrors_SuccessfullyReturnPeersSet() throws Exception {
//		final OrgInfo orgInfo = makeOrgInfo(OrgInfo.OrgType.PEER);
//		final PeerInfo peerInfo = makePeerInfo("peer0", "HOST", false, 1000, orgInfo);
//		final Set<PeerInfo> peerInfos = new HashSet<>();
//		peerInfos.add(peerInfo);
//		final Peer peer = Mockito.mock(Peer.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set<Peer> expectedPeers = new HashSet<>();
//		expectedPeers.add(peer);
//		final Properties properties = new Properties();
//		properties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
//		when(this.fabricClient.newPeer(peerInfo.getName(), peerInfo.getLocation(), properties)).thenReturn(peer);
//
//		final Set<Peer> actualPears = this.peerService.mapPeers(peerInfos);
//
//		when(this.fabricClient.newPeer(peerInfo.getName(), peerInfo.getLocation(), properties)).thenReturn(peer);
//		assertThat(actualPears).isEqualTo(expectedPeers);
//	}
//
//	@Test
//	public void givenTlsEnabled_thenMapPeers_NoErrors_SuccessfullyReturnPeersSet() {
//		final OrgInfo orgInfo = makeOrgInfo(OrgInfo.OrgType.PEER);
//		final PeerInfo peerInfo = makePeerInfo("peer0", "HOST", true, 1000, orgInfo);
//		final Set<PeerInfo> peerInfos = new HashSet<>();
//		peerInfos.add(peerInfo);
//		final Peer peer = Mockito.mock(Peer.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set<Peer> expectedPeers = new HashSet<>();
//		expectedPeers.add(peer);
//		final Map<String, byte[]> keysMap = new HashMap<>();
//		keysMap.put("pemBytes", "pemBytes".getBytes());
//		keysMap.put("clientCertBytes", "clientCertBytes".getBytes());
//		keysMap.put("clientKeyBytes", "clientKeyBytes".getBytes());
//		final Properties peerProperties = TlsUtils.makeComponentTlsProperties(peerInfo.getName(), keysMap);
//		peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
//		when(this.fabricClient.newPeer(peerInfo.getName(), peerInfo.getLocation(), peerProperties)).thenReturn(peer);
//		when(this.resourceServerService.requestClientTLSKeys(ORGANIZATION_NAME, peerInfo.getName())).thenReturn(keysMap);
//
//		final Set<Peer> actualPears = this.peerService.mapPeers(peerInfos);
//
//		verify(this.fabricClient).newPeer(eq(peerInfo.getName()), eq(peerInfo.getLocation()), this.propertiesCaptor.capture());
//		verify(this.resourceServerService).requestClientTLSKeys(ORGANIZATION_NAME, peerInfo.getName());
//
//		assertThat(actualPears).isEqualTo(expectedPeers);
//	}
//
//	@Test
//	public void givenTlsDisabled_thenMapPeersByName_NoErrors_SuccessfullyReturnPeersSet() throws Exception {
//		final Set<String> peerNames = new HashSet<>();
//		final OrgInfo orgInfo = makeOrgInfo(OrgInfo.OrgType.PEER);
//		final PeerInfo peerInfo = makePeerInfo("peer0", "HOST", false, 1000, orgInfo);
//		final Set<PeerInfo> peerInfos = new HashSet<>();
//		peerInfos.add(peerInfo);
//		final Peer peer = Mockito.mock(Peer.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set<Peer> expectedPeers = new HashSet<>();
//		expectedPeers.add(peer);
//		final Properties properties = new Properties();
//		properties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
//		when(this.peerRepository.findAllByName(peerNames)).thenReturn(peerInfos);
//		when(this.fabricClient.newPeer(peerInfo.getName(), peerInfo.getLocation(), properties)).thenReturn(peer);
//
//		final Set<Peer> actualPears = this.peerService.mapPeersByName(peerNames);
//
//		verify(this.peerRepository).findAllByName(peerNames);
//		when(this.fabricClient.newPeer(peerInfo.getName(), peerInfo.getLocation(), properties)).thenReturn(peer);
//		assertThat(actualPears).isEqualTo(expectedPeers);
//	}
//
//	@Test
//	public void givenTlsEnabled_thenMapPeersByName_NoErrors_SuccessfullyReturnPeersSet() {
//		final Set<String> peerNames = new HashSet<>();
//		final OrgInfo orgInfo = makeOrgInfo(OrgInfo.OrgType.PEER);
//		final PeerInfo peerInfo = makePeerInfo("peer0", "HOST", true, 1000, orgInfo);
//		final Set<PeerInfo> peerInfos = new HashSet<>();
//		peerInfos.add(peerInfo);
//		final Peer peer = Mockito.mock(Peer.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set<Peer> expectedPeers = new HashSet<>();
//		expectedPeers.add(peer);
//		final Map<String, byte[]> keysMap = new HashMap<>();
//		keysMap.put("pemBytes", "pemBytes".getBytes());
//		keysMap.put("clientCertBytes", "clientCertBytes".getBytes());
//		keysMap.put("clientKeyBytes", "clientKeyBytes".getBytes());
//		final Properties peerProperties = TlsUtils.makeComponentTlsProperties(peerInfo.getName(), keysMap);
//		peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
//		when(this.peerRepository.findAllByName(peerNames)).thenReturn(peerInfos);
//		when(this.fabricClient.newPeer(peerInfo.getName(), peerInfo.getLocation(), peerProperties)).thenReturn(peer);
//		when(this.resourceServerService.requestClientTLSKeys(ORGANIZATION_NAME, peerInfo.getName())).thenReturn(keysMap);
//
//		final Set<Peer> actualPears = this.peerService.mapPeersByName(peerNames);
//
//		verify(this.peerRepository).findAllByName(peerNames);
//		verify(this.fabricClient).newPeer(eq(peerInfo.getName()), eq(peerInfo.getLocation()), this.propertiesCaptor.capture());
//		verify(this.resourceServerService).requestClientTLSKeys(ORGANIZATION_NAME, peerInfo.getName());
//		assertThat(actualPears).isEqualTo(expectedPeers);
//	}

}
