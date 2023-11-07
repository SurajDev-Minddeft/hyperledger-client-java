package com.utavi.ledger.api.dto.chaincode;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChaincodePrivateCollectionConfig {

	@NotBlank
	private String name;

	@NotBlank
	private String policy;

	@Min(value = 0, message = "The value must be positive")
	private int requiredPeerCount;

	@Min(value = 0, message = "The value must be positive")
	private int maximumPeerCount;

	@Min(value = 0, message = "The value must be positive")
	private int blockToLive;

	@NotNull
	@JsonProperty("SignaturePolicyEnvelope")
	private SignaturePolicyEnvelope signaturePolicyEnvelope;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPolicy() {
		return this.policy;
	}

	public void setPolicy(final String policy) {
		this.policy = policy;
	}

	public int getRequiredPeerCount() {
		return this.requiredPeerCount;
	}

	public void setRequiredPeerCount(final int requiredPeerCount) {
		this.requiredPeerCount = requiredPeerCount;
	}

	public int getMaximumPeerCount() {
		return this.maximumPeerCount;
	}

	public void setMaximumPeerCount(final int maximumPeerCount) {
		this.maximumPeerCount = maximumPeerCount;
	}

	public int getBlockToLive() {
		return this.blockToLive;
	}

	public void setBlockToLive(final int blockToLive) {
		this.blockToLive = blockToLive;
	}

	public SignaturePolicyEnvelope getSignaturePolicyEnvelope() {
		return this.signaturePolicyEnvelope;
	}

	public void setSignaturePolicyEnvelope(
			final SignaturePolicyEnvelope signaturePolicyEnvelope) {
		this.signaturePolicyEnvelope = signaturePolicyEnvelope;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final ChaincodePrivateCollectionConfig that = (ChaincodePrivateCollectionConfig) o;
		return this.requiredPeerCount == that.requiredPeerCount &&
				this.maximumPeerCount == that.maximumPeerCount &&
				this.blockToLive == that.blockToLive &&
				Objects.equals(this.name, that.name) &&
				Objects.equals(this.policy, that.policy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.policy, this.requiredPeerCount, this.maximumPeerCount, this.blockToLive);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", this.name)
				.append("policy", this.policy)
				.append("requiredPeerCount", this.requiredPeerCount)
				.append("maximumPeerCount", this.maximumPeerCount)
				.append("blockToLive", this.blockToLive)
				.toString();
	}

	public static class SignaturePolicyEnvelope {

		@NotNull
		@NotEmpty
		private List<Map<String, Role>> identities;

		@NotNull
		@NotEmpty
		private Map<String, List<PolicyValue>> policy;

		public List<Map<String, Role>> getIdentities() {
			return this.identities;
		}

		public void setIdentities(final List<Map<String, Role>> identities) {
			this.identities = identities;
		}

		public Map<String, List<PolicyValue>> getPolicy() {
			return this.policy;
		}

		public void setPolicy(final Map<String, List<PolicyValue>> policy) {
			this.policy = policy;
		}

		public static class PolicyValue {

			@NotBlank
			@JsonProperty("signed-by")
			private String signedBy;

			public String getSignedBy() {
				return this.signedBy;
			}

			public void setSignedBy(final String signedBy) {
				this.signedBy = signedBy;
			}
		}

		public static class Role {

			@NotNull
			private RoleValue role;

			public RoleValue getRole() {
				return this.role;
			}

			public void setRole(final RoleValue role) {
				this.role = role;
			}

			public static class RoleValue {

				@NotBlank
				private String name;

				@NotBlank
				private String mspId;

				public String getName() {
					return this.name;
				}

				public void setName(final String name) {
					this.name = name;
				}

				public String getMspId() {
					return this.mspId;
				}

				public void setMspId(final String mspId) {
					this.mspId = mspId;
				}
			}
		}
	}
}
