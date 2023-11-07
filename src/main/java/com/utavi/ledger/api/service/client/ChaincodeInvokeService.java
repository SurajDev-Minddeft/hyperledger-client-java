package com.utavi.ledger.api.service.client;

import com.utavi.ledger.api.dto.request.ChaincodeRequest;
import com.utavi.ledger.api.dto.request.balance.get.GetWalletEntryRequest;
import com.utavi.ledger.api.dto.request.balance.getMany.GetWalletsEntriesRequest;
import com.utavi.ledger.api.dto.request.balance.move.MoveBalanceRequest;
import com.utavi.ledger.api.dto.request.credits.CreateCreditsRequest;
import com.utavi.ledger.api.dto.request.history.GetHistoryRequest;
import com.utavi.ledger.api.dto.response.GetWalletEntryResponsePayload;
import com.utavi.ledger.api.dto.response.GetWalletsEntriesResponsePayload;
import com.utavi.ledger.api.dto.response.HistoryResponsePayload;
import com.utavi.ledger.api.dto.response.TransactionResponse;
import com.utavi.ledger.api.exceptions.InvalidStateException;
import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.BaseEntity;
import com.utavi.ledger.api.model.ChannelDetails;
import com.utavi.ledger.api.service.network.chaincode.HFChaincodeInvokeService;
import com.utavi.ledger.api.util.ChaincodeUtils.ProposalValidationResponse;
import com.utavi.ledger.api.util.Utils;
import com.utavi.ledger.api.service.network.channel.ChannelDetailsService;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.utavi.ledger.api.util.Utils.fromJson;
import static com.utavi.ledger.api.util.Utils.toJson;

@Service
public class ChaincodeInvokeService {

	private final static Logger LOGGER = LoggerFactory.getLogger(ChaincodeInvokeService.class.getName());

	private final HFChaincodeInvokeService hfChaincodeInvokeService;
	private final AccountService accountService;
	private final ChannelDetailsService channelDetailsService;
	private final static String GET_WALLET = "GetWallet";
	private final static String MOVE_BALANCE = "MoveBalance";
	private final static String CREATE_CREDITS = "CreateCredits";
	private final static String ADD_API_KEY = "AddApiKey";
	private final static String GET_WALLETS = "GetWallets";
	private final static String GET_HISTORY = "GetHistory";
	private final String CHAINCODE_NAME = "utavi-chaincode";

	public ChaincodeInvokeService(final HFChaincodeInvokeService hfChaincodeInvokeService,
								  final AccountService accountService,
								  final ChannelDetailsService channelDetailsService) {
		this.hfChaincodeInvokeService = hfChaincodeInvokeService;
		this.accountService = accountService;
		this.channelDetailsService = channelDetailsService;
	}

	public CompletableFuture<TransactionResponse> addApiKey(final String channelName, final String apiKey) {
		final Map<String, byte[]> transientParams = new HashMap<>();
		transientParams.put("apiKey", apiKey.getBytes());
		return this.accountService.findPeerAdmin()
				.map(peerAdmin -> this.hfChaincodeInvokeService
						.sendTransactionBlocking(peerAdmin, channelName, ADD_API_KEY, "", transientParams, CHAINCODE_NAME)
						.thenApply(this::buildInvokeResponse)).orElseThrow(RuntimeException::new);
	}

	public Optional<ProposalValidationResponse> createCredits(final CreateCreditsRequest request) {
		return sendTransaction(request, CREATE_CREDITS, getTransientParams(request.getSiloName()));
	}

	public Optional<ProposalValidationResponse> moveBalance(final MoveBalanceRequest request) {
		final Map<String, byte[]> transientParams = getTransientParams(request.getSiloName());
		transientParams.put("txType", request.getTxType().name().getBytes());
		return sendTransaction(request, MOVE_BALANCE, transientParams);
	}

	public GetWalletEntryResponsePayload getEntry(final GetWalletEntryRequest request) {
		return buildQueryResponse(getState(request, GET_WALLET), GetWalletEntryResponsePayload.class);
	}

	public GetWalletsEntriesResponsePayload getEntries(final GetWalletsEntriesRequest request) {
		return buildQueryResponse(getState(request, GET_WALLETS), GetWalletsEntriesResponsePayload.class);
	}

	public HistoryResponsePayload getHistory(final GetHistoryRequest request) {
		return buildQueryResponse(getState(request, GET_HISTORY), HistoryResponsePayload.class);
	}

	private Map<Peer, ProposalResponsePackage.Response> getState(final ChaincodeRequest request, final String function) {
		final String siloName = request.getSiloName();
		final String accountId = request.getAccountId();
		LOGGER.info("Account {} from silo {} has issued a query {}", accountId, siloName, function);
		final String json = toJson(request.getPayload());
		return this.hfChaincodeInvokeService
				.readState(getAccount(accountId), getChannelName(siloName), function, json, getTransientParams(request.getSiloName()), CHAINCODE_NAME);
	}

	private Optional<ProposalValidationResponse> sendTransaction(
			final ChaincodeRequest request,
			final String function,
			final Map<String, byte[]> transientParams) {
		final String json = toJson(request.getPayload());
		final String siloName = request.getSiloName();
		final String accountId = request.getAccountId();
		final String channelName = getChannelName(siloName);
		final String txType = new String(transientParams.getOrDefault("txType", function.getBytes()));
		LOGGER.info("Account {} from silo {} has issued a transaction {}", accountId, siloName, txType);
		Account account = getAccount(accountId);
		return this.hfChaincodeInvokeService
				.sendTransaction(account, channelName, function, json, transientParams, CHAINCODE_NAME);
	}

	private String getChannelName(final String siloName) {
		return this.channelDetailsService.findBySiloName(siloName).map(BaseEntity::getName).orElseThrow(() -> new RuntimeException("Channel not found for given silo name."));
	}

	private Map<String, byte[]> getTransientParams(final String siloName) {
		final Map<String, byte[]> transientParams = new HashMap<>();
		final String apiKey = this.channelDetailsService.findBySiloName(siloName).map(ChannelDetails::getApiKey).orElseThrow(RuntimeException::new);
		if (apiKey == null) {
			throw new IllegalStateException("Api key has not been set yet. Provide an apiKey before calling the chaincode");
		}
		transientParams.put("apiKey", apiKey.getBytes());
		return transientParams;
	}

	private TransactionResponse buildInvokeResponse(final TransactionEvent transactionEvent) {
		final boolean valid = transactionEvent.isValid();
		final long blockNumber = transactionEvent.getBlockEvent().getBlockNumber();
		final String transactionID = transactionEvent.getTransactionID();
		final Date timestamp = transactionEvent.getTimestamp();
		return new TransactionResponse(transactionID, blockNumber, timestamp, valid);
	}

	private <T> T buildQueryResponse(final Map<Peer, ProposalResponsePackage.Response> proposalResponses, final Class<T> clazz) {
		final List<ProposalResponsePackage.Response> responses = wrapResponses(proposalResponses);
		return responses.stream().map(response -> {
			final String payload = response.getPayload().toStringUtf8();
			return fromJson(payload, clazz);
		}).collect(Utils.toSingleton());
	}

	private List<ProposalResponsePackage.Response> wrapResponses(final Map<Peer, ProposalResponsePackage.Response> proposalResponse) {
		final List<ProposalResponsePackage.Response> responses = proposalResponse.values().stream().distinct().collect(Collectors.toList());
		if (responses.size() != 1) {
			final StringBuilder logBuilder = new StringBuilder();
			logBuilder.append("Received distinct responses from the peers, responses={");
			proposalResponse.forEach((peer, response) -> {
				final String value = response.getPayload().toStringUtf8();
				logBuilder.append(String.format("Peer %s has responded with value %s, ", peer.getName(), value));
			});
			logBuilder.append(" }");
			LOGGER.warn(logBuilder.toString());
			throw new InvalidStateException(String.format("Received %d responses from the peers, but expected 1", responses.size()));
		}
		return responses;
	}

	private Account getAccount(final String accountName) {
		return this.accountService.findOrgByType(accountName);
	}

}
