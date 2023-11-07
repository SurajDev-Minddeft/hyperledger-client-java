package com.utavi.ledger.api.dto;

import javax.validation.constraints.NotBlank;

public abstract class BaseOrganizationComponentDto {

	@NotBlank
	private String name;

	@NotBlank
	private String location;

	@NotBlank
	private String organizationName;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public String getOrganizationName() {
		return this.organizationName;
	}

	public void setOrganizationName(final String organizationName) {
		this.organizationName = organizationName;
	}
}
