package com.utavi.ledger.api.service.network;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import com.utavi.ledger.api.service.network.chaincode.HFChaincodeInvokeService;
import com.utavi.ledger.api.util.ChaincodeUtils;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HFChaincodeInvokeService.class, ChaincodeID.class, ChaincodeUtils.class})
@PowerMockIgnore({"javax.crypto.*"})
public class HFChaincodeInvokeServiceTest {
//
//	@InjectMocks
//	private HFChaincodeInvoker hfChaincodeInvoker;
//
//	@Mock
//	private Channel channel;
//
//	@Mock
//	private QueryByChaincodeRequest queryByChaincodeRequest;
//
//	@Mock
//	private TransactionProposalRequest transactionProposalRequest;
//
//	@Mock
//	private BlockEvent.TransactionEvent transactionEvent;
//
//	@Mock
//	private FabricClient fabricClient;
//
//	@Mock
//	private ChaincodeID.Builder builder;
//
//	@Mock
//	private ChaincodeID chaincodeID;
//
//	@Mock
//	private ChaincodeInfoService chaincodeInfoService;
//
//	@Mock
//	private ChannelUtils channelUtils;
//
//	@Test
//	public void whenReadState_NoErrors_ReturnPeerResponseMap() {
//		final Account account = makeBusinessUserWithSilo();
//		final String channelName = account.getMembership(SILO_NAME).get().getChannelInfo().getName();
//		final String functionName = "functionName";
//		final String args = "args";
//		final Map<String, byte[]> transientParams = new HashMap<>();
//		final String param = "apiKey";
//		transientParams.put(param, param.getBytes());
//		mockStatic(ChaincodeID.class);
//		final Collection proposalResponses = Mockito.mock(Collection.class, Mockito.RETURNS_DEEP_STUBS);
//		final Map peerResponseMap = Mockito.mock(Map.class, Mockito.RETURNS_DEEP_STUBS);
//		mockStatic(ChaincodeUtils.class);
//
//		final ChaincodeInfo chaincodeInfo = createChaincodeInfo();
//		when(ChaincodeID.newBuilder()).thenReturn(this.builder);
//		when(this.builder.setVersion(chaincodeInfo.getVersion())).thenReturn(this.builder);
//		when(this.builder.setPath(chaincodeInfo.getPath())).thenReturn(this.builder);
//		when(this.builder.setName(chaincodeInfo.getName())).thenReturn(this.builder);
//		when(this.builder.build()).thenReturn(this.chaincodeID);
//		when(this.chaincodeInfoService.getCurrentCCId()).thenReturn(this.chaincodeID);
//
//		when(this.fabricClient.getChannelWithAdminCtx(channelName)).thenReturn(this.channel);
//		when(this.fabricClient.newQueryRequest(this.chaincodeID, functionName, transientParams, args, account)).thenReturn(this.queryByChaincodeRequest);
//		when(this.channelUtils.queryByChaincode(this.queryByChaincodeRequest, CHANNEL_NAME)).thenReturn(proposalResponses);
//		when(proposalResponses.stream().collect(any())).thenReturn(peerResponseMap);
//
//		this.hfChaincodeInvoker.readState(account, channelName, functionName, args, transientParams, this.chaincodeID);
//
//		verifyStatic(ChaincodeUtils.class);
//		ChaincodeUtils.validateProposalResponses(proposalResponses);
//		verify(this.fabricClient).newQueryRequest(this.chaincodeID, functionName, transientParams, args, account);
//		verify(this.channelUtils).queryByChaincode(this.queryByChaincodeRequest, CHANNEL_NAME);
//		verify(proposalResponses, times(2)).stream();
//	}
//
//	@Test
//	public void whenSendTransaction_NoErrors_ReturnTransactionEvent() {
//		final Account account = makeBusinessUserWithSilo();
//		final String channelName = account.getMembership(SILO_NAME).get().getChannelInfo().getName();
//		final String functionName = "functionName";
//		final String args = "args";
//		final Map<String, byte[]> transientParams = new HashMap<>();
//		final String param = "param";
//		transientParams.put(param, param.getBytes());
//		mockStatic(ChaincodeID.class);
//		final Collection proposalResponses = Mockito.mock(Collection.class, Mockito.RETURNS_DEEP_STUBS);
//		mockStatic(ChaincodeUtils.class);
//		final CompletableFuture completableFutureTransactionEvent = Mockito.mock(CompletableFuture.class, Mockito.RETURNS_DEEP_STUBS);
//
//		final ChaincodeInfo chaincodeInfo = createChaincodeInfo();
//		when(ChaincodeID.newBuilder()).thenReturn(this.builder);
//		when(this.builder.setVersion(chaincodeInfo.getVersion())).thenReturn(this.builder);
//		when(this.builder.setPath(chaincodeInfo.getPath())).thenReturn(this.builder);
//		when(this.builder.setName(chaincodeInfo.getName())).thenReturn(this.builder);
//		when(this.builder.build()).thenReturn(this.chaincodeID);
//		when(this.chaincodeInfoService.getCurrentCCId()).thenReturn(this.chaincodeID);
//
//		when(this.fabricClient.getClientChannel(channelName)).thenReturn(this.channel);
//		when(this.fabricClient.newTransactionProposal(this.chaincodeID, functionName, args, transientParams, account)).thenReturn(this.transactionProposalRequest);
//		when(this.channelUtils.sendTransactionProposal(this.transactionProposalRequest, CHANNEL_NAME)).thenReturn(proposalResponses);
//		when(this.channelUtils.sendTransaction(account, CHANNEL_NAME, proposalResponses)).thenReturn(completableFutureTransactionEvent);
//		when(completableFutureTransactionEvent.join()).thenReturn(this.transactionEvent);
//
//		this.hfChaincodeInvoker.sendTransaction(account, channelName, functionName, args, transientParams, this.chaincodeID);
//
//		verifyStatic(ChaincodeUtils.class);
//		ChaincodeUtils.validateProposalResponses(proposalResponses);
//		verify(this.fabricClient).newTransactionProposal(this.chaincodeID, functionName, args, transientParams, account);
//		verify(this.channelUtils).sendTransactionProposal(this.transactionProposalRequest, CHANNEL_NAME);
//		verify(this.channelUtils).sendTransaction(account, CHANNEL_NAME, proposalResponses);
//	}

}
