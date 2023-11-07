package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.OrgType;

public class CAServerInfoDto {

	private String name;
	private String location;
	private OrgType orgType;

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

	public OrgType getOrgType() {
		return this.orgType;
	}

	public void setOrgType(final OrgType orgType) {
		this.orgType = orgType;
	}
}
