package com.utavi.ledger.api.service.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import com.utavi.ledger.api.util.ChaincodeUtils;
import com.utavi.ledger.api.util.Utils;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ChaincodeInvokeService.class, ChaincodeID.class, ChaincodeUtils.class, Utils.class})
@PowerMockIgnore({"javax.crypto.*"})
public class ChaincodeInvokeServiceTest {

//	@InjectMocks
//	private ChaincodeInvoker chaincodeInvoker;
//
//	@Mock
//	private HFChaincodeInvoker hfChaincodeInvoker;
//
//	@Mock
//	private BlockEvent.TransactionEvent transactionEvent;
//
//	@Mock
//	private BlockEvent blockEvent;
//
//	@Mock
//	private ChaincodeInfoService chaincodeInfoService;
//
//	@Mock
//	private AccountService accountService;
//
//	@Mock
//	private ChannelInfoManager channelInfoManager;
//
//	@Captor
//	private ArgumentCaptor<Map<String, byte[]>> transientParamsCaptor;
//
//	@Test
//	public void whenAddApiKey_NoError_ReturnTransactionResponse() throws Exception {
//		final TransactionResponse mockedTransactionResponse = createMock(TransactionResponse.class);
//		final AddApiKeyRequest addApiKeyRequest = new AddApiKeyRequest();
//		final Account account = Mockito.mock(Account.class, RETURNS_DEEP_STUBS);
//		addApiKeyRequest.setApiKey(API_KEY);
//		addApiKeyRequest.setChannelName(CHANNEL_NAME);
//
//		final ChannelInfo channelInfo = Mockito.mock(ChannelInfo.class, RETURNS_DEEP_STUBS);
//		final ChaincodeInfo chaincodeInfo = createChaincodeInfo();
//
//		when(this.channelInfoManager.find(addApiKeyRequest.getChannelName())).thenReturn(channelInfo);
//		when(this.chaincodeInfoService.findCurrent()).thenReturn(chaincodeInfo);
//		when(this.accountService.findPeerAdmin()).thenReturn(account);
//		when(this.hfChaincodeInvoker
//				.sendTransaction(eq(account), eq(addApiKeyRequest.getChannelName()), eq("AddApiKey"), eq(""), any(), any(ChaincodeID.class)))
//				.thenReturn(CompletableFuture.completedFuture(this.transactionEvent));
//		whenForInvokeResponse(mockedTransactionResponse);
//
//		this.chaincodeInvoker.addApiKey(addApiKeyRequest);
//
//		verify(this.hfChaincodeInvoker)
//				.sendTransaction(any(Account.class), eq(addApiKeyRequest.getChannelName()), eq("AddApiKey"), eq(""),
//						this.transientParamsCaptor.capture(), any(ChaincodeID.class));
//		PowerMock.verify(mockedTransactionResponse, TransactionResponse.class);
//	}
//
//	@Test
//	public void whenCreateCredits_NoErrors_ReturnTransactionResponse() throws Exception {
//		final Account account = makeBusinessUserWithSilo();
//		final CreateCreditsRequest createCreditsRequest = makeCreateCreditsRequest();
//		mockStatic(Utils.class);
//		final TransactionResponse mockedTransactionResponse = createMock(TransactionResponse.class);
//		final ChannelInfo channelInfo = Mockito.mock(ChannelInfo.class, RETURNS_DEEP_STUBS);
//		final ChaincodeInfo chaincodeInfo = createChaincodeInfo();
//
//		when(this.channelInfoManager.findBySiloName(createCreditsRequest.getSiloName())).thenReturn(channelInfo);
//		when(this.chaincodeInfoService.findCurrent()).thenReturn(chaincodeInfo);
//		when(this.accountService.find(account.getName())).thenReturn(account);
//		when(toJson(createCreditsRequest.getPayload())).thenReturn("mockedCreateCreditsInternalPayload");
//		when(this.hfChaincodeInvoker.sendTransaction(eq(account), eq(CHANNEL_NAME), eq("CreateCredits"),
//				eq("mockedCreateCreditsInternalPayload"), any(), any(ChaincodeID.class))).thenReturn(CompletableFuture.completedFuture(this.transactionEvent));
//		when(this.channelInfoManager.findBySiloName(SILO_NAME)).thenReturn(channelInfo);
//		when(channelInfo.getName()).thenReturn(CHANNEL_NAME);
//		when(channelInfo.getApiKey()).thenReturn(API_KEY);
//		whenForInvokeResponse(mockedTransactionResponse);
//
//		this.chaincodeInvoker.createCredits(createCreditsRequest);
//
//		verifyStatic(Utils.class);
//		toJson(createCreditsRequest.getPayload());
//		verify(this.hfChaincodeInvoker).sendTransaction(eq(account), eq(CHANNEL_NAME), eq("CreateCredits"),
//				eq("mockedCreateCreditsInternalPayload"), this.transientParamsCaptor.capture(), any(ChaincodeID.class));
//		PowerMock.verify(mockedTransactionResponse, TransactionResponse.class);
//	}
//
//	@Test
//	public void whenMoveBalance_NoErrors_ReturnTransactionResponse() throws Exception {
//		final MoveBalanceRequest moveBalanceRequest = makeMoveBalanceRequest(TxType.TRANSFER);
//		final Account account = makeBusinessUserWithSilo();
//		mockStatic(Utils.class);
//		final TransactionResponse mockedTransactionResponse = createMock(TransactionResponse.class);
//
//		final ChannelInfo channelInfo = Mockito.mock(ChannelInfo.class, RETURNS_DEEP_STUBS);
//		final ChaincodeInfo chaincodeInfo = createChaincodeInfo();
//
//		when(this.channelInfoManager.findBySiloName(moveBalanceRequest.getSiloName())).thenReturn(channelInfo);
//		when(this.chaincodeInfoService.findCurrent()).thenReturn(chaincodeInfo);
//
//		when(this.accountService.find(account.getName())).thenReturn(account);
//		when(toJson(moveBalanceRequest.getPayload())).thenReturn("mockedUpdateBalanceInternalPayload");
//		when(this.hfChaincodeInvoker.sendTransaction(eq(account), eq(CHANNEL_NAME), eq("MoveBalance"),
//				eq("mockedUpdateBalanceInternalPayload"), any(), any(ChaincodeID.class))).thenReturn(CompletableFuture.completedFuture(this.transactionEvent));
//		whenForInvokeResponse(mockedTransactionResponse);
//		when(this.channelInfoManager.findBySiloName(SILO_NAME)).thenReturn(channelInfo);
//		when(channelInfo.getName()).thenReturn(CHANNEL_NAME);
//		when(channelInfo.getApiKey()).thenReturn(API_KEY);
//		this.chaincodeInvoker.moveBalance(moveBalanceRequest);
//
//		verifyStatic(Utils.class);
//		toJson(moveBalanceRequest.getPayload());
//		verify(this.hfChaincodeInvoker).sendTransaction(eq(account), eq(CHANNEL_NAME), eq("MoveBalance"),
//				eq("mockedUpdateBalanceInternalPayload"), this.transientParamsCaptor.capture(), any(ChaincodeID.class));
//		PowerMock.verify(mockedTransactionResponse, TransactionResponse.class);
//	}
//
//	@Test
//	public void whenGetEntry_NoErrors_ReturnGetWalletEntryResponseExternalPayload() {
//		final GetWalletEntryRequest getWalletEntryRequest = makeGetWalletEntryRequest();
//		final Account account = makeBusinessUserWithSilo();
//		mockStatic(Utils.class);
//		final Map mockedMap = Mockito.mock(Map.class, Mockito.RETURNS_DEEP_STUBS);
//		final List responses = Mockito.mock(List.class, Mockito.RETURNS_DEEP_STUBS);
//		final GetWalletEntryResponsePayload mockesGetWalletEntryResponsePayload = createMock(
//				GetWalletEntryResponsePayload.class);
//
//		final ChannelInfo channelInfo = Mockito.mock(ChannelInfo.class, RETURNS_DEEP_STUBS);
//		final ChaincodeInfo chaincodeInfo = createChaincodeInfo();
//
//		when(this.channelInfoManager.findBySiloName(getWalletEntryRequest.getSiloName())).thenReturn(channelInfo);
//		when(this.chaincodeInfoService.findCurrent()).thenReturn(chaincodeInfo);
//		when(this.accountService.find(account.getName())).thenReturn(account);
//		when(toJson(getWalletEntryRequest.getPayload())).thenReturn("mockedGetWalletEntryRequestInternalPayload");
//		when(this.hfChaincodeInvoker.readState(eq(account), eq(CHANNEL_NAME), eq("GetWallet"),
//				eq("mockedGetWalletEntryRequestInternalPayload"), any(), any(ChaincodeID.class))).thenReturn(mockedMap);
//		when(mockedMap.values().stream().distinct().collect(any())).thenReturn(responses);
//		when(responses.size()).thenReturn(1);
//		when(responses.stream().map(any()).collect(any())).thenReturn(mockesGetWalletEntryResponsePayload);
//		when(this.channelInfoManager.findBySiloName(SILO_NAME)).thenReturn(channelInfo);
//		when(channelInfo.getApiKey()).thenReturn(API_KEY);
//		when(channelInfo.getName()).thenReturn(CHANNEL_NAME);
//		when(this.accountService.find(account.getName())).thenReturn(account);
//
//		this.chaincodeInvoker.getEntry(getWalletEntryRequest);
//
//		verifyStatic(Utils.class);
//		toJson(getWalletEntryRequest.getPayload());
//		verify(this.hfChaincodeInvoker).readState(eq(account), eq(CHANNEL_NAME), eq("GetWallet"),
//				eq("mockedGetWalletEntryRequestInternalPayload"), this.transientParamsCaptor.capture(), any(ChaincodeID.class));
//		verify(mockedMap, times(2)).values();
//		verify(responses, times(2)).stream();
//		verifyStatic(Utils.class);
//		Utils.toSingleton();
//	}
//
//	@Test
//	public void whenGetEntries_NoErrors_ReturnGetWalletsEntriesResponseExternalPayload() {
//		final GetWalletsEntriesRequest getWalletsEntriesRequest = makeGetWalletsEntriesRequest();
//		final Account account = makeBusinessUserWithSilo();
//		mockStatic(Utils.class);
//		final Map mockedMap = Mockito.mock(Map.class, Mockito.RETURNS_DEEP_STUBS);
//		final List responses = Mockito.mock(List.class, Mockito.RETURNS_DEEP_STUBS);
//		final GetWalletsEntriesResponsePayload mockedGetWalletsEntriesResponsePayload = createMock(
//				GetWalletsEntriesResponsePayload.class);
//		final ChannelInfo channelInfo = Mockito.mock(ChannelInfo.class, RETURNS_DEEP_STUBS);
//		final ChaincodeInfo chaincodeInfo = createChaincodeInfo();
//
//		when(this.channelInfoManager.findBySiloName(getWalletsEntriesRequest.getSiloName())).thenReturn(channelInfo);
//		when(this.chaincodeInfoService.findCurrent()).thenReturn(chaincodeInfo);
//		when(this.accountService.find(account.getName())).thenReturn(account);
//
//		when(toJson(getWalletsEntriesRequest.getPayload())).thenReturn("mockedGetWalletEntryRequestInternalPayload");
//		when(this.hfChaincodeInvoker.readState(eq(account), eq(CHANNEL_NAME), eq("GetWallets"),
//				eq("mockedGetWalletEntryRequestInternalPayload"), any(), any(ChaincodeID.class))).thenReturn(mockedMap);
//		when(mockedMap.values().stream().distinct().collect(any())).thenReturn(responses);
//		when(responses.size()).thenReturn(1);
//		when(responses.stream().map(any()).collect(any())).thenReturn(mockedGetWalletsEntriesResponsePayload);
//		when(this.channelInfoManager.findBySiloName(SILO_NAME)).thenReturn(channelInfo);
//		when(channelInfo.getName()).thenReturn(CHANNEL_NAME);
//		when(channelInfo.getApiKey()).thenReturn(API_KEY);
//
//		this.chaincodeInvoker.getEntries(getWalletsEntriesRequest);
//
//		verifyStatic(Utils.class);
//		toJson(getWalletsEntriesRequest.getPayload());
//		verify(this.hfChaincodeInvoker).readState(eq(account), eq(CHANNEL_NAME), eq("GetWallets"),
//				eq("mockedGetWalletEntryRequestInternalPayload"), this.transientParamsCaptor.capture(), any(ChaincodeID.class));
//		verify(mockedMap, times(2)).values();
//		verify(responses, times(2)).stream();
//		verifyStatic(Utils.class);
//		Utils.toSingleton();
//	}
//
//	private void whenForInvokeResponse(final TransactionResponse mockedTransactionResponse) throws Exception {
//		final Date timestamp = Date.from(Instant.now());
//		final long blockNumber = 1L;
//		final String transactionId = "TransactionID";
//
//		when(this.transactionEvent.isValid()).thenReturn(true);
//		when(this.transactionEvent.getBlockEvent()).thenReturn(this.blockEvent);
//		when(this.blockEvent.getBlockNumber()).thenReturn(blockNumber);
//		when(this.transactionEvent.getTransactionID()).thenReturn(transactionId);
//		when(this.transactionEvent.getTimestamp()).thenReturn(timestamp);
//		expectNew(TransactionResponse.class, transactionId, blockNumber, timestamp, true).andReturn(mockedTransactionResponse);
//		replay(mockedTransactionResponse, TransactionResponse.class);
//	}

}
