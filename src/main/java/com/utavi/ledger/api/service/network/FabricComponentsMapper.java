package com.utavi.ledger.api.service.network;

import com.utavi.ledger.api.model.OrdererInfo;
import com.utavi.ledger.api.model.PeerInfo;
import com.utavi.ledger.api.service.network.FabricClient;
import com.utavi.ledger.api.util.TlsUtils;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
public class FabricComponentsMapper {

	private final FabricClient fabricClient;

	public FabricComponentsMapper(final FabricClient fabricClient) {
		this.fabricClient = fabricClient;
	}

	public Peer mapPeer(final PeerInfo peerInfo) {
		final String name = peerInfo.getName();
		final String location = peerInfo.getLocation();
		final Properties properties = new Properties();
		properties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
		Properties tlsProperties = TlsUtils.makeCAServerTlsProperties(peerInfo.getTlsCertificate().getBytes(), peerInfo.getName());
		properties.putAll(tlsProperties);
		return this.fabricClient.newPeer(name, location, properties);
	}

	public Orderer mapOrderer(final OrdererInfo ordererInfo) {
		final String name = ordererInfo.getName();
		final String location = ordererInfo.getLocation();
		final Properties properties = new Properties();
		properties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 103809024);
		Properties tlsProperties = TlsUtils.makeCAServerTlsProperties(ordererInfo.getTlsCertificate().getBytes(), ordererInfo.getName());
		properties.putAll(tlsProperties);
		return this.fabricClient.newOrderer(name, location, properties);
	}
}
