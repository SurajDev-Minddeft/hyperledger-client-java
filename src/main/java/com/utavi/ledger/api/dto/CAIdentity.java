package com.utavi.ledger.api.dto;

public class CAIdentity {

	private String name;
	private String affiliation;
	private String type;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getAffiliation() {
		return this.affiliation;
	}

	public void setAffiliation(final String affiliation) {
		this.affiliation = affiliation;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

}
