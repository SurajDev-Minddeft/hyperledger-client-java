package com.utavi.ledger.api.service.network.chaincode;

import static com.utavi.ledger.api.util.ChaincodeUtils.prepareProposalValidationResponse;

import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.service.network.FabricClient;
import com.utavi.ledger.api.service.network.channel.HFChannelService;
import com.utavi.ledger.api.util.ChaincodeUtils.ProposalValidationResponse;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.hyperledger.fabric.protos.peer.ProposalResponsePackage;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HFChaincodeInvokeService {

	private final static Logger LOGGER = LoggerFactory.getLogger(HFChaincodeInvokeService.class.getName());

	private final FabricClient fabricClient;
	private final HFChannelService HFChannelService;

	public HFChaincodeInvokeService(final FabricClient fabricClient, final HFChannelService HFChannelService) {
		this.fabricClient = fabricClient;
		this.HFChannelService = HFChannelService;
	}

	public Map<Peer, ProposalResponsePackage.Response> readState(
			final Account account,
			final String channelName,
			final String functionName,
			final String args,
			final Map<String, byte[]> transientParams,
			final String chaincodeName) {
		LOGGER.info("Invoking function {} on Channel={}", functionName, channelName);
		final QueryByChaincodeRequest queryByChaincodeRequest = this.fabricClient
				.newQueryRequest(chaincodeName, functionName, transientParams, args, account);
		final Collection<ProposalResponse> proposalResponses = this.HFChannelService.queryByChaincode(queryByChaincodeRequest, channelName);
		prepareProposalValidationResponse(proposalResponses);

		final Map<Peer, ProposalResponsePackage.Response> peerResponseMap = proposalResponses.stream().collect(
				Collectors.toMap(ProposalResponse::getPeer, proposalResponse -> proposalResponse.getProposalResponse().getResponse()));

		LOGGER.info("Successfully invoked function {} on Channel={}", functionName, channelName);
		return peerResponseMap;
	}

	public CompletableFuture<TransactionEvent> sendTransactionBlocking(
			final Account account,
			final String channelName,
			final String functionName,
			final String args,
			final Map<String, byte[]> transientParams,
			final String chaincodeName) {
		LOGGER.info("Invoking function {} on Channel={}", functionName, channelName);
		final TransactionProposalRequest transactionProposalRequest = this.fabricClient
				.newTransactionProposal(chaincodeName, functionName, args, transientParams, account);
		final Collection<ProposalResponse> proposalResponses = this.HFChannelService.sendTransactionProposal(transactionProposalRequest, channelName);
		prepareProposalValidationResponse(proposalResponses);
		return this.HFChannelService.sendBlockingTransaction(account, channelName, proposalResponses);
	}

	public Optional<ProposalValidationResponse> sendTransaction(
			final Account account,
			final String channelName,
			final String functionName,
			final String args,
			final Map<String, byte[]> transientParams,
			final String chaincodeName) {
		LOGGER.info("Invoking function {} on Channel={}", functionName, channelName);
		final TransactionProposalRequest transactionProposalRequest = this.fabricClient
				.newTransactionProposal(chaincodeName, functionName, args, transientParams, account);
		final Collection<ProposalResponse> proposalResponses = this.HFChannelService.sendTransactionProposal(transactionProposalRequest, channelName);
		final Optional<ProposalValidationResponse> proposalValidationResponses = prepareProposalValidationResponse(proposalResponses);
		this.HFChannelService.sendTransaction(account, channelName, proposalResponses);
		return proposalValidationResponses;
	}

}
