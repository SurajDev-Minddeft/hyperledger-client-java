package com.utavi.ledger.api.service.network.chaincode.management;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ChaincodeManagerTest {
//
//	@InjectMocks
//	private ChaincodeManager chaincodeManager;
//
//	@Mock
//	private HFChaincodeManager hfChaincodeManager;
//
//	@Mock
//	private ChaincodeInvoker chaincodeInvoker;
//
//	@Mock
//	private PeerInfoService peerService;
//
//	@Mock
//	private OrgInfoService orgInfoService;
//
//	@Mock
//	private ChaincodeInfoWriter chaincodeInfoWriter;
//
//	@Test
//	public void whenInstall_NoErrors_SuccessfulInstalled() {
//		final ChaincodeInstallRequest request = makeChaincodeInstallRequest();
//		final Peer peer = Mockito.mock(Peer.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set<Peer> peers = new HashSet<>();
//		peers.add(peer);
//		when(this.peerService.getFabricPeersForNames(anySet())).thenReturn(peers);
//		this.chaincodeManager.install(request);
//		verify(this.hfChaincodeManager).install(request.getChaincodeName(), request.getChaincodeVersion(), peers);
//	}
//
//	@Test
//	public void whenInstantiate_NoErrors_SuccessfulInstantiated() {
//		final OrgInfo orgInfo = Mockito.mock(OrgInfo.class, Mockito.RETURNS_DEEP_STUBS);
//		when(this.orgInfoService.findPeerOrg()).thenReturn(orgInfo);
//		when(orgInfo.getName()).thenReturn("main-utavi");
//		final ChaincodeInstantiateRequest instantiateRequest = makeChaincodeInstantiateRequest();
//		this.chaincodeManager.instantiate(instantiateRequest);
//		verify(this.hfChaincodeManager).deploy(any(ChaincodeDeployRequest.class));
//	}
//
//	@Test
//	public void whenUpgrade_NoErrors_SuccessfulUpgraded() {
//		final OrgInfo orgInfo = Mockito.mock(OrgInfo.class, Mockito.RETURNS_DEEP_STUBS);
//		when(this.orgInfoService.findPeerOrg()).thenReturn(orgInfo);
//		when(orgInfo.getName()).thenReturn("main-utavi");
//		final ChaincodeMetaInf chaincodeMetaInf = new ChaincodeMetaInf();
//		chaincodeMetaInf.setLang(Type.GO_LANG);
//		chaincodeMetaInf.setName(CHAINCODE_NAME);
//		chaincodeMetaInf.setPath(CHANNEL_NAME);
//		chaincodeMetaInf.setVersion(VERSION);
////		when(this.chaincodeInfoWriter.downloadChaincodeMetaInf()).thenReturn(chaincodeMetaInf);
//		final ChaincodeInstantiateRequest instantiateRequest = new ChaincodeInstantiateRequest();
//		instantiateRequest.setChannelName(CHANNEL_NAME);
//		this.chaincodeManager.upgrade(instantiateRequest);
//		verify(this.hfChaincodeManager).deploy(any(ChaincodeDeployRequest.class));
//	}
}
