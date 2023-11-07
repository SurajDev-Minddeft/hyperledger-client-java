package com.utavi.ledger.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.utavi.ledger.api.model.enums.OrgType;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateOrgInfoDto {

	@NotBlank
	private String name;

	@NotBlank
	private String mspId;

	@NotBlank
	private String affiliation;

	@NotNull
	private OrgType orgType;

	public OrgType getOrgType() {
		return this.orgType;
	}

	public void setOrgType(final OrgType orgType) {
		this.orgType = orgType;
	}

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

	public String getAffiliation() {
		return this.affiliation;
	}

	public void setAffiliation(final String affiliation) {
		this.affiliation = affiliation;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", this.name)
				.append("mspId", this.mspId)
				.append("affiliation", this.affiliation)
				.append("orgTy[e", this.orgType)
				.toString();
	}
}
