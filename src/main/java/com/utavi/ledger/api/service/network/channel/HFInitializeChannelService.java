package com.utavi.ledger.api.service.network.channel;

import com.utavi.ledger.api.model.ChannelDetails;
import com.utavi.ledger.api.service.client.NetworkNodesService;
import com.utavi.ledger.api.service.network.FabricClient;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class HFInitializeChannelService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HFInitializeChannelService.class.getName());

    private final FabricClient fabricClient;
    private final NetworkNodesService networkNodesService;


    public HFInitializeChannelService(final FabricClient fabricClient,
                                      final NetworkNodesService networkNodesService) {
        this.fabricClient = fabricClient;
        this.networkNodesService = networkNodesService;
    }

    public void initializeChannel(final ChannelDetails channelDetails) {
        final Set<Peer> peers = this.networkNodesService.getFabricPeersForChannel(channelDetails);
        final Set<Orderer> orderers = this.networkNodesService.getFabricOrderersForChannel(channelDetails);
        final Channel channel = this.fabricClient.getChannel(channelDetails.getName());
        peers.forEach(peer -> addToChannel(channel, peer));
        orderers.forEach(orderer -> addOrdererToChannel(channel, orderer));
        this.fabricClient.initializeChannel(channel);
    }

    private void addToChannel(final Channel channel, final Peer peer) {
        try {
            LOGGER.info("Adding Peer={}, to Channel={}", peer.getName(), channel.getName());
            channel.addPeer(peer);
            LOGGER.info("Successfully added Peer={}, to Channel={}", peer.getName(), channel.getName());
        } catch (final InvalidArgumentException e) {
            LOGGER.error("Failed to add Peer={}, to Channel={}, Error={}", peer.getName(), channel.getName(), e);
            throw new RuntimeException(String.format("Failed to add peer %s to channel %s", peer.getName(), channel.getName()), e);
        }
    }

    private void addOrdererToChannel(final Channel channel, final Orderer orderer) {
        try {
            LOGGER.info("Adding Orderer={}, to Channel={}", orderer.getName(), channel.getName());
            final Collection<Orderer> channelOrderers = channel.getOrderers();
            if (channelOrderers.stream().anyMatch(ord -> ord.getName().equals(orderer.getName()) && ord.getUrl().equals(orderer.getUrl()))) {
                LOGGER.info("Orderer {} is already in channel {}", orderer.getName(), channel.getName());
                return;
            }
            channel.addOrderer(orderer);
            LOGGER.info("Successfully added Orderer={}, to Channel={}", orderer.getName(), channel.getName());
        } catch (final InvalidArgumentException e) {
            throw new RuntimeException(String.format("Failed to add orderer %s to channel %s", orderer.getName(), channel.getName()), e);
        }
    }

}
