package com.utavi.ledger.api.service.manage;

import com.utavi.ledger.api.model.PeerInfo;
import com.utavi.ledger.api.repository.PeerDetailsRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component("peerConfigManager")
public class PeerConfigManager extends NetworkNodeConfigManager<PeerInfo> {

	public PeerConfigManager(final PeerDetailsRepository repository) {
		super(repository);
	}

    @Override
	public void save(Set<NodeConfigWrapper> nodeConfigWrappers) {
		final Set<PeerInfo> persistedPeers = nodeConfigWrappers
            .stream()
			.map(PeerInfo::new)
			.collect(toSet());
		this.repository.saveAll(persistedPeers);
	}

}
