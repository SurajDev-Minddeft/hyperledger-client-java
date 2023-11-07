package com.utavi.ledger.api.dto.channel.config;

import javax.validation.constraints.NotBlank;

public class OrgConfigRequest {

	public OrgConfigRequest(@NotBlank final String newOrgName, @NotBlank final String orgConfig) {
		this.newOrgName = newOrgName;
		this.orgConfig = orgConfig;
	}

	public OrgConfigRequest() {
	}

	@NotBlank
	private String newOrgName;

	@NotBlank
	private String orgConfig;

	public String getNewOrgName() {
		return this.newOrgName;
	}

	public void setNewOrgName(final String newOrgName) {
		this.newOrgName = newOrgName;
	}

	public String getOrgConfig() {
		return this.orgConfig;
	}

	public void setOrgConfig(final String orgConfig) {
		this.orgConfig = orgConfig;
	}

}
