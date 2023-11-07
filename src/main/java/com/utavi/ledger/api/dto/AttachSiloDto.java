package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.SiloType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AttachSiloDto {

	@NotNull
	private SiloType type;

	@NotBlank
	private String name;

	public SiloType getType() {
		return this.type;
	}

	public void setType(final SiloType type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", this.name)
				.append("type", this.type)
				.toString();
	}
}
