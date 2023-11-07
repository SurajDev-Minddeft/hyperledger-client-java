package com.utavi.ledger.api.model;

import com.utavi.ledger.api.dto.ChaincodeUpgradeLogDto;
import com.utavi.ledger.api.model.chaincode.ExternalChaincodeInfo;
import com.utavi.ledger.api.model.enums.ChaincodeUpgradeStatus;
import com.utavi.ledger.api.model.enums.DeployType;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.modelmapper.PropertyMap;

@Entity
@Table(name = "lc_chaincode_upgrade_log")
public class ChaincodeUpgradeLog {

	public ChaincodeUpgradeLog() {
		this.startedAt = Instant.now();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "started_at", columnDefinition = "TIMESTAMPTZ")
	private final Instant startedAt;

	@Column(name = "completed_at", columnDefinition = "TIMESTAMPTZ")
	private Instant completedAt;

	@Column(name = "deploy_version")
	private String deployVersion;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "upgrade_status")
	private ChaincodeUpgradeStatus upgradeStatus;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column
	private DeployType deployType;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "external_chaincode_info_id")
	private ExternalChaincodeInfo externalChaincodeInfo;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "channel_info_id")
	private ChannelDetails channelDetails;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "peer_info_id")
	private PeerInfo peerInfo;

	public static PropertyMap<ChaincodeUpgradeLog, ChaincodeUpgradeLogDto> mappedFields = new PropertyMap<ChaincodeUpgradeLog, ChaincodeUpgradeLogDto>() {
		protected void configure() {
			map().setChaincodeName(this.source.getChaincodeName());
			map().setDeployVersion(this.source.getDeployVersion());
			map().setStartedAt(this.source.getStartedAt());
			map().setUpgradeStatus(this.source.getUpgradeStatus());
			map().setChannelName(this.source.getChannelName());
		}
	};

	public Instant getStartedAt() {
		return this.startedAt;
	}

	public Instant getCompletedAt() {
		return this.completedAt;
	}

	public void setCompletedAt(final Instant completedAt) {
		this.completedAt = completedAt;
	}

	public ChaincodeUpgradeStatus getUpgradeStatus() {
		return this.upgradeStatus;
	}

	public void setUpgradeStatus(final ChaincodeUpgradeStatus upgradeStatus) {
		this.upgradeStatus = upgradeStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PeerInfo getPeerInfo() {
		return peerInfo;
	}

	public ExternalChaincodeInfo getExternalChaincodeInfo() {
		return externalChaincodeInfo;
	}

	public void setExternalChaincodeInfo(ExternalChaincodeInfo externalChaincodeInfo) {
		this.externalChaincodeInfo = externalChaincodeInfo;
	}

	public ChannelDetails getChannelDetails() {
		return channelDetails;
	}

	public static PropertyMap<ChaincodeUpgradeLog, ChaincodeUpgradeLogDto> getMappedFields() {
		return mappedFields;
	}

	public static void setMappedFields(PropertyMap<ChaincodeUpgradeLog, ChaincodeUpgradeLogDto> mappedFields) {
		ChaincodeUpgradeLog.mappedFields = mappedFields;
	}

	public DeployType getDeployType() {
		return this.deployType;
	}

	public void setDeployType(final DeployType deployType) {
		this.deployType = deployType;
	}

	public String getDeployVersion() {
		return this.deployVersion;
	}

	public void setDeployVersion(final String deployVersion) {
		this.deployVersion = deployVersion;
	}

	public String getChaincodeName() {
		return this.externalChaincodeInfo.getName();
	}

	public String getChannelName() {
		return this.channelDetails.getName();
	}

	public void setChannelDetails(final ChannelDetails channelDetails) {
		this.channelDetails = channelDetails;
	}

	public void setPeerInfo(PeerInfo peerInfo) {
		this.peerInfo = peerInfo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChaincodeUpgradeLog that = (ChaincodeUpgradeLog) o;
		return Objects.equals(id, that.id) && Objects.equals(startedAt, that.startedAt) && Objects.equals(completedAt, that.completedAt) && Objects.equals(deployVersion, that.deployVersion) && upgradeStatus == that.upgradeStatus && deployType == that.deployType && Objects.equals(externalChaincodeInfo, that.externalChaincodeInfo) && Objects.equals(channelDetails, that.channelDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, startedAt, completedAt, deployVersion, upgradeStatus, deployType, externalChaincodeInfo, channelDetails);
	}

	@Override
	public String toString() {
		return "ChaincodeUpgradeLog{" +
				   "id=" + id +
				   ", startedAt=" + startedAt +
				   ", completedAt=" + completedAt +
				   ", deployVersion='" + deployVersion + '\'' +
				   ", upgradeStatus=" + upgradeStatus +
				   ", deployType=" + deployType +
				   ", externalChaincodeInfo=" + externalChaincodeInfo +
				   ", channelDetails=" + channelDetails +
				   '}';
	}
}
