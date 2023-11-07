package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.ChaincodeUpgradeStatus;
import java.time.Instant;

public class ChaincodeUpgradeLogDto {

	private String chaincodeName;
	private ChaincodeUpgradeStatus upgradeStatus;
	private String deployVersion;
	private Instant startedAt;
	private String channelName;

	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	public String getChaincodeName() {
		return this.chaincodeName;
	}

	public void setChaincodeName(final String chaincodeName) {
		this.chaincodeName = chaincodeName;
	}

	public ChaincodeUpgradeStatus getUpgradeStatus() {
		return this.upgradeStatus;
	}

	public void setUpgradeStatus(final ChaincodeUpgradeStatus upgradeStatus) {
		this.upgradeStatus = upgradeStatus;
	}

	public String getDeployVersion() {
		return this.deployVersion;
	}

	public void setDeployVersion(final String deployVersion) {
		this.deployVersion = deployVersion;
	}

	public Instant getStartedAt() {
		return this.startedAt;
	}

	public void setStartedAt(final Instant startedAt) {
		this.startedAt = startedAt;
	}
}
