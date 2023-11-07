package com.utavi.ledger.api.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateOrdererInfoDto extends BaseOrganizationComponentDto {

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", getName())
				.append("location", getLocation())
				.append("orgName", getOrganizationName())
				.toString();
	}
}
