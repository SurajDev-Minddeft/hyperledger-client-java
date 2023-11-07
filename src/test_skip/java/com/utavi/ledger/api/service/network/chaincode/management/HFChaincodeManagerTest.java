package com.utavi.ledger.api.service.network.chaincode.management;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import com.utavi.ledger.api.service.network.ResourceServerService;
import com.utavi.ledger.api.util.ChaincodeUtils;
import com.utavi.ledger.api.util.Utils;

import javax.json.Json;

import org.hyperledger.fabric.protos.peer.Query;
import org.hyperledger.fabric.sdk.ChaincodeCollectionConfiguration;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		HFChaincodeManager.class,
		Utils.class,
		Query.ChaincodeInfo.class,
		ChaincodeID.class,
		ChaincodeUtils.class,
		Json.class,
		ChaincodeCollectionConfiguration.class
})
@PowerMockIgnore({"javax.crypto.*"})
public class HFChaincodeManagerTest {

//	@InjectMocks
//	private HFChaincodeManager hfChaincodeManager;
//
//	@Mock
//	private HFCAServerService hfCAServerService;
//
//	@Mock
//	private PeerInfoService peerInfoService;
//
//	@Mock
//	private Peer mockedPeer;
//
//	@Mock
//	private FabricClient fabricClient;
//
//	@Mock
//	private ChaincodeInfoService chaincodeInfoService;
//
//	@Mock
//	private ChaincodeDownloader chaincodeDownloader;
//
//	@Mock
//	private ResourceServerService resourceServerService;
//
//	@Mock
//	private ChannelUtils channelUtils;
//
//	@Test
//	public void whenInstall_NoErrors_SuccessfullyInstalled() {
//		final Set<Peer> peers = new HashSet<>();
//		peers.add(this.mockedPeer);
//
//		final InputStream chaincodeStream = Mockito.mock(InputStream.class, Mockito.RETURNS_DEEP_STUBS);
//		final Query.ChaincodeInfo chaincodeInfo = createMock(Query.ChaincodeInfo.class);
//		final List<Query.ChaincodeInfo> chaincodeInfos = new ArrayList<>();
//		chaincodeInfos.add(chaincodeInfo);
//		mockStatic(ChaincodeID.class);
//		final InstallProposalRequest mockedInstallProposalRequest = Mockito.mock(InstallProposalRequest.class, Mockito.RETURNS_DEEP_STUBS);
//		final Collection proposalResponses = Mockito.mock(Collection.class, Mockito.RETURNS_DEEP_STUBS);
//		mockStatic(ChaincodeUtils.class);
//
//		final ChaincodeInfo cInfo = createChaincodeInfo();
//		final ChaincodeID chaincodeID = Mockito.mock(ChaincodeID.class, RETURNS_DEEP_STUBS);
//		when(this.fabricClient.queryInstalledChaincodes(this.mockedPeer)).thenReturn(chaincodeInfos);
//		EasyMock.expect(chaincodeInfo.getName()).andStubReturn(cInfo.getName());
//		EasyMock.expect(chaincodeInfo.getVersion()).andStubReturn("0");
//		EasyMock.replay(chaincodeInfo);
//		when(this.fabricClient.newInstallRequest()).thenReturn(mockedInstallProposalRequest);
//		when(this.fabricClient.sendInstallProposal(mockedInstallProposalRequest, peers)).thenReturn(proposalResponses);
//		when(this.peerInfoService.mapPeers(anySet())).thenReturn(peers);
//		when(this.chaincodeInfoService.findCurrent()).thenReturn(cInfo);
//		when(this.chaincodeDownloader.getChaincodeStream(cInfo.getVersion())).thenReturn(chaincodeStream);
//		when(chaincodeID.getName()).thenReturn(cInfo.getName());
//		when(chaincodeID.getVersion()).thenReturn(cInfo.getVersion());
//		when(this.chaincodeInfoService.getChaincodeID(cInfo.getName(), cInfo.getVersion())).thenReturn(chaincodeID);
//		this.hfChaincodeManager.install(cInfo.getName(), cInfo.getVersion(), peers);
//
//		verify(this.fabricClient).sendInstallProposal(mockedInstallProposalRequest, peers);
//		verify(this.fabricClient).queryInstalledChaincodes(this.mockedPeer);
//		EasyMock.verify(chaincodeInfo);
//		verify(this.fabricClient).newInstallRequest();
//		verify(this.fabricClient).sendInstallProposal(mockedInstallProposalRequest, peers);
//		ChaincodeUtils.validateProposalResponses(proposalResponses);
//	}
//
//	@Test
//	public void whenInstantiate_NoErrors_SuccessfullyInstantiated() throws Exception {
//		final ChaincodeDeployRequest chaincodeDeployRequest = makeChaincodeDeployRequest("main-utavi", DeployType.INSTANTIATE);
//		mockStatic(ChaincodeCollectionConfiguration.class);
//		final ChaincodeCollectionConfiguration privateCollectionConfig = Mockito
//				.mock(ChaincodeCollectionConfiguration.class, Mockito.RETURNS_DEEP_STUBS);
//		mockStatic(Json.class);
//		mockStatic(Utils.class);
//		final JsonArrayBuilder arrayBuilder = Mockito.mock(JsonArrayBuilder.class, Mockito.RETURNS_DEEP_STUBS);
//		final JsonReader jsonReader = Mockito.mock(JsonReader.class, Mockito.RETURNS_DEEP_STUBS);
//		final JsonObjectBuilder jsonObjectBuilder = Mockito.mock(JsonObjectBuilder.class, Mockito.RETURNS_DEEP_STUBS);
//		final Set peers = Mockito.mock(Set.class, Mockito.RETURNS_DEEP_STUBS);
//		final Query.ChaincodeInfo chaincodeInfo = createMock(Query.ChaincodeInfo.class);
//		final List<Query.ChaincodeInfo> chaincodeInfos = new ArrayList<>();
//		chaincodeInfos.add(chaincodeInfo);
//		final InstantiateProposalRequest instantiateProposalRequest = Mockito.mock(InstantiateProposalRequest.class, Mockito.RETURNS_DEEP_STUBS);
//		final Collection proposalResponses = Mockito.mock(Collection.class, Mockito.RETURNS_DEEP_STUBS);
//		mockStatic(ChaincodeUtils.class);
//		final CompletableFuture future = Mockito.mock(CompletableFuture.class, Mockito.RETURNS_DEEP_STUBS);
//		final ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = Mockito.mock(ChaincodeEndorsementPolicy.class, Mockito.RETURNS_DEEP_STUBS);
//
//		final ChaincodeID chaincodeID = Mockito.mock(ChaincodeID.class, RETURNS_DEEP_STUBS);
//		final ChaincodeInfo cInfo = createChaincodeInfo();
//
//		when(chaincodeID.getName()).thenReturn(CHAINCODE_NAME);
//		when(chaincodeID.getVersion()).thenReturn(VERSION);
//		when(this.chaincodeInfoService.findOne(CHAINCODE_NAME, VERSION)).thenReturn(cInfo);
//		when(this.chaincodeInfoService.createCCID(cInfo)).thenReturn(chaincodeID);
//		when(Json.createObjectBuilder()).thenReturn(jsonObjectBuilder);
//		when(Json.createReader((Reader) any())).thenReturn(jsonReader);
//		when(toJson(any())).thenReturn("config");
//		when(Json.createArrayBuilder()).thenReturn(arrayBuilder);
//		when(ChaincodeCollectionConfiguration.fromJsonObject(chaincodeDeployRequest.privateCollectionConfigAsJson()))
//				.thenReturn(privateCollectionConfig);
//		when(this.resourceServerService.getChaincodeEndorsementPolicy(CHANNEL_NAME, CHAINCODE_NAME, VERSION)).thenReturn(chaincodeEndorsementPolicy);
//		when(peers.stream().findFirst()).thenReturn(Optional.of(this.mockedPeer));
//		when(this.channelUtils.queryInstantiatedChaincodes(CHANNEL_NAME)).thenReturn(chaincodeInfos);
//		EasyMock.expect(chaincodeInfo.getName()).andStubReturn(cInfo.getName());
//		EasyMock.expect(chaincodeInfo.getVersion()).andStubReturn(cInfo.getVersion());
//		EasyMock.replay(chaincodeInfo);
//		when(this.fabricClient.newInstantiationProposalRequest(chaincodeID, chaincodeEndorsementPolicy, privateCollectionConfig))
//				.thenReturn(instantiateProposalRequest);
//		when(this.channelUtils.sendInstantiationProposal(CHANNEL_NAME, instantiateProposalRequest)).thenReturn(proposalResponses);
//		when(this.channelUtils.sendTransaction(CHANNEL_NAME, proposalResponses)).thenReturn(future);
//
//		this.hfChaincodeManager.deploy(chaincodeDeployRequest);
//
//		verifyStatic(ChaincodeCollectionConfiguration.class);
//		ChaincodeCollectionConfiguration.fromJsonObject(any());
//		verifyStatic(Utils.class, times(2));
//		toJson(any());
//		verifyStatic(Json.class, times(2));
//		Json.createObjectBuilder();
//		verifyStatic(Json.class, times(2));
//		Json.createReader((Reader) any());
//		verifyStatic(Json.class, times(2));
//		Json.createArrayBuilder();
//		verify(peers).stream();
//		verify(this.channelUtils).queryInstantiatedChaincodes(CHANNEL_NAME);
//		EasyMock.verify(chaincodeInfo);
//		verify(this.fabricClient).newInstantiationProposalRequest(chaincodeID, chaincodeEndorsementPolicy, privateCollectionConfig);
//		verify(this.channelUtils).sendInstantiationProposal(CHANNEL_NAME, instantiateProposalRequest);
//		ChaincodeUtils.validateProposalResponses(proposalResponses);
//		verify(this.channelUtils).sendTransaction(CHANNEL_NAME, proposalResponses);
//		verify(future).join();
//	}
//
//	@Test
//	public void whenUpgrade_NoErrors_SuccessfullyUpgraded() throws Exception {
//		final ChaincodeDeployRequest chaincodeDeployRequest = makeChaincodeDeployRequest("main-utavi", DeployType.UPGRADE);
//		mockStatic(ChaincodeCollectionConfiguration.class);
//		final ChaincodeCollectionConfiguration privateCollectionConfig = Mockito
//				.mock(ChaincodeCollectionConfiguration.class, Mockito.RETURNS_DEEP_STUBS);
//		mockStatic(Json.class);
//		mockStatic(Utils.class);
//		final JsonArrayBuilder arrayBuilder = Mockito.mock(JsonArrayBuilder.class, Mockito.RETURNS_DEEP_STUBS);
//		final JsonReader jsonReader = Mockito.mock(JsonReader.class, Mockito.RETURNS_DEEP_STUBS);
//		final JsonObjectBuilder jsonObjectBuilder = Mockito.mock(JsonObjectBuilder.class, Mockito.RETURNS_DEEP_STUBS);
//		final UpgradeProposalRequest upgradeProposalRequest = Mockito.mock(UpgradeProposalRequest.class, Mockito.RETURNS_DEEP_STUBS);
//		mockStatic(ChaincodeID.class);
//		final Set peers = Mockito.mock(Set.class, Mockito.RETURNS_DEEP_STUBS);
//		final Collection proposalResponses = Mockito.mock(Collection.class, Mockito.RETURNS_DEEP_STUBS);
//		mockStatic(ChaincodeUtils.class);
//		final ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = Mockito.mock(ChaincodeEndorsementPolicy.class, Mockito.RETURNS_DEEP_STUBS);
//		final CompletableFuture future = Mockito.mock(CompletableFuture.class, Mockito.RETURNS_DEEP_STUBS);
//		final Query.ChaincodeInfo chaincodeInfo = createMock(Query.ChaincodeInfo.class);
//		final List<Query.ChaincodeInfo> chaincodeInfos = new ArrayList<>();
//		chaincodeInfos.add(chaincodeInfo);
//
//		final ChaincodeInfo cInfo = createChaincodeInfo();
//		cInfo.setVersion("2.0");
//		final ChaincodeID chaincodeID = Mockito.mock(ChaincodeID.class, RETURNS_DEEP_STUBS);
//		when(chaincodeID.getName()).thenReturn(cInfo.getName());
//		when(chaincodeID.getVersion()).thenReturn(cInfo.getVersion());
//		when(this.chaincodeInfoService.findOne(CHAINCODE_NAME, VERSION)).thenReturn(cInfo);
//		when(this.chaincodeInfoService.createCCID(cInfo)).thenReturn(chaincodeID);
//		when(Json.createObjectBuilder()).thenReturn(jsonObjectBuilder);
//		when(Json.createReader((Reader) any())).thenReturn(jsonReader);
//		when(toJson(any())).thenReturn("config");
//		when(Json.createArrayBuilder()).thenReturn(arrayBuilder);
//		when(ChaincodeCollectionConfiguration.fromJsonObject(chaincodeDeployRequest.privateCollectionConfigAsJson()))
//				.thenReturn(privateCollectionConfig);
//		when(peers.stream().findFirst()).thenReturn(Optional.of(this.mockedPeer));
//		when(this.fabricClient.newUpgradeProposalRequest(chaincodeID, chaincodeEndorsementPolicy, privateCollectionConfig))
//				.thenReturn(upgradeProposalRequest);
//		when(this.channelUtils.sendUpgradeProposal(CHANNEL_NAME, upgradeProposalRequest)).thenReturn(proposalResponses);
//		when(this.channelUtils.sendTransaction(CHANNEL_NAME, proposalResponses)).thenReturn(future);
//		when(this.resourceServerService.getChaincodeEndorsementPolicy(CHANNEL_NAME, cInfo.getName(), cInfo.getVersion()))
//				.thenReturn(chaincodeEndorsementPolicy);
//		EasyMock.expect(chaincodeInfo.getName()).andStubReturn(cInfo.getName());
//		EasyMock.expect(chaincodeInfo.getVersion()).andStubReturn(cInfo.getVersion());
//		EasyMock.replay(chaincodeInfo);
//
//		this.hfChaincodeManager.deploy(chaincodeDeployRequest);
//
//		verifyStatic(ChaincodeCollectionConfiguration.class);
//		ChaincodeCollectionConfiguration.fromJsonObject(any());
//		verifyStatic(Utils.class, times(2));
//		toJson(any());
//		verifyStatic(Json.class, times(2));
//		Json.createObjectBuilder();
//		verifyStatic(Json.class, times(2));
//		Json.createReader((Reader) any());
//		verifyStatic(Json.class, times(2));
//		Json.createArrayBuilder();
//		verify(peers).stream();
//		verify(this.channelUtils).queryInstantiatedChaincodes(CHANNEL_NAME);
//		EasyMock.verify(chaincodeInfo);
//		verify(this.fabricClient).newUpgradeProposalRequest(chaincodeID, chaincodeEndorsementPolicy, privateCollectionConfig);
//		verify(this.channelUtils).sendUpgradeProposal(CHANNEL_NAME, upgradeProposalRequest);
//		ChaincodeUtils.validateProposalResponses(proposalResponses);
//		verify(this.channelUtils).sendTransaction(CHANNEL_NAME, proposalResponses);
//		verify(future).join();
//	}

}
