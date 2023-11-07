package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.OrgType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OrgInfoDto {

	@NotBlank
	private String name;

	@NotBlank
	private String mspId;

	@NotBlank
	private String rootAffiliation;

	@NotNull
	private OrgType orgType;

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

	public String getRootAffiliation() {
		return this.rootAffiliation;
	}

	public void setRootAffiliation(final String rootAffiliation) {
		this.rootAffiliation = rootAffiliation;
	}

	public OrgType getOrgType() {
		return this.orgType;
	}

	public void setOrgType(final OrgType orgType) {
		this.orgType = orgType;
	}
}
