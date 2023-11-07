package com.utavi.ledger.api.dto;

import java.time.Instant;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResponseOrganizationDto {

	@NotBlank
	private String name;

	@NotBlank
	private Instant createDate;

	@NotBlank
	private Instant updateDate;

	@NotBlank
	private String mspId;

	@NotBlank
	private String affiliation;

	@NotBlank
	private String orgType;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Instant getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(final Instant createDate) {
		this.createDate = createDate;
	}

	public Instant getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(final Instant updateDate) {
		this.updateDate = updateDate;
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

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(final String orgType) {
		this.orgType = orgType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", this.name)
				.append("createDate", this.createDate)
				.append("updateDate", this.updateDate)
				.append("mspId", this.mspId)
				.append("affiliation", this.affiliation)
				.append("orgType", this.orgType)
				.toString();
	}
}
