package com.utavi.ledger.api.dto.chaincode;

import javax.validation.constraints.NotBlank;

abstract class ChaincodeManagementRequest {

	@NotBlank
	private String chaincodeName;

	@NotBlank
	private String chaincodeVersion;

	public String getChaincodeName() {
		return this.chaincodeName;
	}

	public void setChaincodeName(final String chaincodeName) {
		this.chaincodeName = chaincodeName;
	}

	public String getChaincodeVersion() {
		return this.chaincodeVersion;
	}

	public void setChaincodeVersion(final String chaincodeVersion) {
		this.chaincodeVersion = chaincodeVersion;
	}
}
