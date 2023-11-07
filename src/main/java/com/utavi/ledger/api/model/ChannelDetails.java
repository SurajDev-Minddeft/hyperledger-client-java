package com.utavi.ledger.api.model;

import com.utavi.ledger.api.dto.ChannelInfoDto;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "lc_channel_info")
@AttributeOverride(name = "name", column = @Column(name = "channel_name"))
public class ChannelDetails extends BaseEntity {

	@Column(name = "silo_name")
	private String siloName;

	@Column(name = "api_key")
	private String apiKey;

	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSiloName() {
		return this.siloName;
	}

	public void setSiloName(final String siloName) {
		this.siloName = siloName;
	}

	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(
			name = "lc_channel_peers",
			joinColumns = @JoinColumn(name = "channel_details_id"),
			inverseJoinColumns = @JoinColumn(name = "peer_details_id"))
	private Set<PeerInfo> channelPeers;

	public Set<PeerInfo> getChannelPeers() {
		return this.channelPeers;
	}

	public void addPeerDetails(final Set<PeerInfo> peerDetails) {
		if (this.channelPeers == null) {
			this.channelPeers = new HashSet<>();
		}
		this.channelPeers.addAll(peerDetails);
	}

	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(
			name = "lc_channel_orderers",
			joinColumns = @JoinColumn(name = "channel_details_id"),
			inverseJoinColumns = @JoinColumn(name = "orderer_details_id"))
	private Set<OrdererInfo> channelOrderers;

	public Set<OrdererInfo> getChannelOrderers() {
		return this.channelOrderers;
	}

	public void addOrdererDetails(final Set<OrdererInfo> ordererInfos) {
		if (this.channelOrderers == null) {
			this.channelOrderers = new HashSet<>();
		}
		this.channelOrderers.addAll(ordererInfos);
	}

	public ChannelInfoDto toDto() {
		final ChannelInfoDto dto = new ChannelInfoDto();
		dto.setChannelName(getName());
		dto.setSiloName(getSiloName());
		return dto;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		final ChannelDetails that = (ChannelDetails) o;
		return Objects.equals(getSiloName(), that.getSiloName()) &&
				Objects.equals(getName(), that.getName()) &&
				Objects.equals(getApiKey(), that.getApiKey());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getSiloName(), getName(), getApiKey());
	}
}
