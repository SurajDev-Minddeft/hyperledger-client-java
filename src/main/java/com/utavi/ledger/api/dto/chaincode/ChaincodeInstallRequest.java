package com.utavi.ledger.api.dto.chaincode;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChaincodeInstallRequest extends ChaincodeManagementRequest {

	@NotEmpty
	private Set<String> peerNames;

	public Set<String> getPeerNames() {
		return this.peerNames;
	}

	public void setPeerNames(final Set<String> peerNames) {
		this.peerNames = peerNames;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("peerNames", this.peerNames)
				.append("chaincodeName", getChaincodeName())
				.append("chaincodeVersion", getChaincodeVersion())
				.toString();
	}
}
