package com.utavi.ledger.api.service.client;

import com.utavi.ledger.api.model.ChannelDetails;
import com.utavi.ledger.api.service.network.FabricComponentsMapper;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
public class NetworkNodesService {

	private final FabricComponentsMapper fabricComponentsMapper;

	public NetworkNodesService(final FabricComponentsMapper fabricComponentsMapper) {
		this.fabricComponentsMapper = fabricComponentsMapper;
	}

	public Set<Peer> getFabricPeersForChannel(final ChannelDetails channelDetails) {
		return channelDetails.getChannelPeers().stream().map(this.fabricComponentsMapper::mapPeer).collect(toSet());
	}

	public Set<Orderer> getFabricOrderersForChannel(final ChannelDetails channelDetails) {
		return channelDetails.getChannelOrderers().stream().map(this.fabricComponentsMapper::mapOrderer).collect(toSet());
	}
}
