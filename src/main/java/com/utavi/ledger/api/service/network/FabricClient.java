package com.utavi.ledger.api.service.network;

import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.service.client.AccountService;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuite.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

@Component
public class FabricClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(FabricClient.class.getName());

    private final AccountService accountService;
    @Value("${hl.proposal-wait-time:90000}")
    private Long userTxProposalWaitTime;

    @Value("${hl.proposal-wait-time:120000}")
    private Long adminTxProposalWaitTime;

    private static HFClient HF_CLIENT;

    public FabricClient(final AccountService accountService) throws Exception {
        this.accountService = accountService;
        final CryptoSuite cryptoSuite = Factory.getCryptoSuite();
        HF_CLIENT = HFClient.createNewInstance();
        HF_CLIENT.setCryptoSuite(cryptoSuite);
    }

    public Orderer newOrderer(final String name, final String location, final Properties properties) {
        try {
            initializePeerAdminContext();
            return HF_CLIENT.newOrderer(name, location, properties);
        } catch (final InvalidArgumentException e) {
            throw new RuntimeException(String.format("Failed to map orderer %s", name), e);
        }
    }

    public Peer newPeer(final String name, final String location, final Properties properties) {
        try {
            initializePeerAdminContext();
            return HF_CLIENT.newPeer(name, location, properties);
        } catch (final InvalidArgumentException e) {
            throw new RuntimeException(String.format("Failed to map peer %s", name), e);
        }
    }

    public QueryByChaincodeRequest newQueryRequest(
            final String chaincodeName,
            final String functionName,
            final Map<String, byte[]> transientParams,
            final String args,
            final Account account) {
        try {
            final QueryByChaincodeRequest queryByChaincodeRequest = HF_CLIENT.newQueryProposalRequest();
            queryByChaincodeRequest.setArgs(args);
            queryByChaincodeRequest.setFcn(functionName);
            queryByChaincodeRequest.setChaincodeName(chaincodeName);
            queryByChaincodeRequest.setTransientMap(transientParams);
            queryByChaincodeRequest.setUserContext(account);
            queryByChaincodeRequest.setProposalWaitTime(this.userTxProposalWaitTime);
            return queryByChaincodeRequest;
        } catch (final InvalidArgumentException e) {
            throw new RuntimeException(String.format("Failed to query function %s of chaincode %s", functionName), e);
        }
    }

    public TransactionProposalRequest newTransactionProposal(
            final String chaincodeName,
            final String functionName,
            final String args,
            final Map<String, byte[]> transientParams,
            final Account account) {
        try {
            final TransactionProposalRequest transactionProposalRequest = HF_CLIENT.newTransactionProposalRequest();
            transactionProposalRequest.setFcn(functionName);
//            transactionProposalRequest.setProposalWaitTime(this.userTxProposalWaitTime);
            transactionProposalRequest.setArgs(args);
            transactionProposalRequest.setTransientMap(transientParams);
            transactionProposalRequest.setUserContext(account);
            transactionProposalRequest.setChaincodeName(chaincodeName);
            return transactionProposalRequest;
        } catch (final InvalidArgumentException e) {
            throw new RuntimeException(String.format("Failed to invoke function %s of chaincode %s", functionName, chaincodeName), e);
        }
    }

    public Channel getChannelWithAdminCtx(final String channelName) {
        initializePeerAdminContext();
        return getChannel(channelName);
    }

    public Channel getChannel(final String channelName) {
        return Optional.ofNullable(HF_CLIENT.getChannel(channelName)).orElseGet(() -> newChannel(channelName));
    }

    private Channel newChannel(final String channelName) {
        try {
            return HF_CLIENT.newChannel(channelName);
        } catch (final InvalidArgumentException e) {
            throw new RuntimeException("Failed to create channel object for " + channelName, e);
        }
    }

    public Channel newChannel(final String channelName, final Orderer orderer, final ChannelConfiguration channelConfiguration,
                              final byte[]... channelConfigurationSignatures) {
        try {
            initializePeerAdminContext();
            final Channel channel = HF_CLIENT.newChannel(channelName, orderer, channelConfiguration, channelConfigurationSignatures);
            initializeChannel(channel);
            return channel;
        } catch (final TransactionException | InvalidArgumentException e) {
            throw new RuntimeException("Failed to create new channel with name " + channelName, e);
        }
    }

    public byte[] getChannelConfigurationSignature(final ChannelConfiguration channelConfiguration, final OrgType orgType) {
        initializeAdminContext(orgType);
        final User signer = HF_CLIENT.getUserContext();
        try {
            return HF_CLIENT.getChannelConfigurationSignature(channelConfiguration, signer);
        } catch (final InvalidArgumentException e) {
            throw new RuntimeException("Failed to get Channel Configuration Signature", e);
        }
    }

    public byte[] getUpdateChannelConfigurationSignature(final UpdateChannelConfiguration config, final OrgType orgType) {
        initializeAdminContext(orgType);
        final User signer = HF_CLIENT.getUserContext();
        try {
            return HF_CLIENT.getUpdateChannelConfigurationSignature(config, signer);
        } catch (final InvalidArgumentException e) {
            throw new RuntimeException("Failed to get Channel Configuration Signature", e);
        }
    }

    private void initializeAdminContext(final OrgType orgType) {
        if (orgType == OrgType.PEER) {
            initializePeerAdminContext();
        } else if (orgType == OrgType.ORDERER) {
            initializeOrdererAdminContext();
        } else {
            throw new IllegalArgumentException(String.format("Unknown org type %s", orgType));
        }
    }

    public Set<String> readPeerChannels(final Peer peer) {
        try {
            initializePeerAdminContext();
            return HF_CLIENT.queryChannels(peer);
        } catch (final ProposalException | InvalidArgumentException e) {
            throw new RuntimeException("Failed to read channels set on peer" + peer.getName(), e);
        }
    }

    public boolean doesChannelExist(final Set<Peer> peers, final String channelName) {
        return peers.stream().map(this::readPeerChannels).anyMatch(peerChannels -> peerChannels.contains(channelName));
    }

    public void initializeChannel(final Channel channel) {
        final String channelName = channel.getName();
        try {
            LOGGER.info("Initializing Channel={}", channelName);
            channel.initialize();
            LOGGER.info("Successfully initialized channel {}", channelName);
        } catch (final TransactionException | InvalidArgumentException e) {
            throw new RuntimeException("Failed to initialize channel " + channelName, e);
        }
    }

    private void initializeOrdererAdminContext() {
        final Account ordererAdmin = this.accountService.findOrdererAdmin();
        setUserContext(ordererAdmin);
    }

    private void initializePeerAdminContext() {
        this.accountService.findPeerAdmin().ifPresent(this::setUserContext);
    }

    private void setUserContext(final User adminContext) {
        HF_CLIENT.setUserContext(adminContext);
    }

}
