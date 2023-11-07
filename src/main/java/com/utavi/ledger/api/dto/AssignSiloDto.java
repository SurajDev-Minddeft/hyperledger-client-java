package com.utavi.ledger.api.dto;

import com.utavi.ledger.api.model.enums.SiloType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AssignSiloDto {

	@NotBlank
	private String prevSiloName;

	@NotBlank
	private String newSiloName;

	@NotNull
	private SiloType newSiloType;

	public SiloType getNewSiloType() {
		return this.newSiloType;
	}

	public void setNewSiloType(final SiloType newSiloType) {
		this.newSiloType = newSiloType;
	}

	public String getPrevSiloName() {
		return this.prevSiloName;
	}

	public void setPrevSiloName(final String prevSiloName) {
		this.prevSiloName = prevSiloName;
	}

	public String getNewSiloName() {
		return this.newSiloName;
	}

	public void setNewSiloName(final String newSiloName) {
		this.newSiloName = newSiloName;
	}
}
