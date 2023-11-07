package com.utavi.ledger.api.model;

import com.utavi.ledger.api.service.manage.NetworkNodeConfigManager.NodeConfigWrapper;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "lc_peer_info")
public class PeerInfo extends BaseOrganizationComponent {

	public PeerInfo() {
	}

	public PeerInfo(final NodeConfigWrapper wrapper) {
		super(wrapper.getName(), wrapper.getLocation(), wrapper.getOrgInfo(), wrapper.getTlsCertificate());
	}

	@ManyToMany(mappedBy = "channelPeers")
	private Set<ChannelDetails> peerChannels;

	public Set<ChannelDetails> getPeerChannels() {
		return this.peerChannels;
	}
}
