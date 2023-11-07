package com.utavi.ledger.api.dto.channel.config;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class AddOrgAdminRequest {

	@NotBlank
	private String name;

	@NotBlank
	private String orgName;

	@NotEmpty
	private Set<String> channels;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(final String orgName) {
		this.orgName = orgName;
	}

	public Set<String> getChannels() {
		return this.channels;
	}

	public void setChannels(final Set<String> channels) {
		this.channels = channels;
	}

}
