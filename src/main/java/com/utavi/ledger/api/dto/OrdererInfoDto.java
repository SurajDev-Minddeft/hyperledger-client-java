package com.utavi.ledger.api.dto;

public class OrdererInfoDto {

	private String ordererName;
	private String location;

	public String getOrdererName() {
		return this.ordererName;
	}

	public String getLocation() {
		return this.location;
	}

	public void setOrdererName(final String ordererName) {
		this.ordererName = ordererName;
	}

	public void setLocation(final String location) {
		this.location = location;
	}
}
