package com.utavi.ledger.api.service.network.channel;

import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.service.network.FabricClient;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HFChannelService {

	private final static Logger LOGGER = LoggerFactory.getLogger(HFChannelService.class.getName());

	private final FabricClient fabricClient;

	public HFChannelService(final FabricClient fabricClient) {
		this.fabricClient = fabricClient;
	}

	public CompletableFuture<TransactionEvent> sendBlockingTransaction(final Account account, final String channelName, final Collection<ProposalResponse> proposalResponses) {
		final Channel channel = this.fabricClient.getChannel(channelName);
		return channel.sendTransaction(proposalResponses, account);
	}

	@Async
	public void sendTransaction(final Account account, final String channelName, final Collection<ProposalResponse> proposalResponses) {
		final Channel channel = this.fabricClient.getChannel(channelName);
		channel.sendTransaction(proposalResponses, account);
	}

	public Collection<ProposalResponse> queryByChaincode(final QueryByChaincodeRequest request, final String channelName) {
		final Channel channel = this.fabricClient.getChannel(channelName);
		try {
			return channel.queryByChaincode(request);
		} catch (final InvalidArgumentException | ProposalException e) {
			throw new RuntimeException(String.format("Failed to invoke function %s on channel %s", request.getFcn(), channelName), e);
		}
	}

	public Collection<ProposalResponse> sendTransactionProposal(final TransactionProposalRequest request, final String channelName) {
		final Channel channel = this.fabricClient.getChannel(channelName);
		try {
			return channel.sendTransactionProposal(request);
		} catch (final InvalidArgumentException | ProposalException e) {
			throw new RuntimeException(String.format("Failed to invoke function %s on channel %s", request.getFcn(), channelName), e);
		}
	}
}
