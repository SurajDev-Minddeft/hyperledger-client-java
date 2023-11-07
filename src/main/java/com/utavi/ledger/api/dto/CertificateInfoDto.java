package com.utavi.ledger.api.dto;

import java.util.Date;

public class CertificateInfoDto {

	private Date endOfValidity;
	private Date startOfValidity;

	public CertificateInfoDto(final Date endOfValidity, final Date startOfValidity) {
		this.endOfValidity = endOfValidity;
		this.startOfValidity = startOfValidity;
	}

	public Date getEndOfValidity() {
		return this.endOfValidity;
	}

	public void setEndOfValidity(final Date endOfValidity) {
		this.endOfValidity = endOfValidity;
	}

	public Date getStartOfValidity() {
		return this.startOfValidity;
	}

	public void setStartOfValidity(final Date startOfValidity) {
		this.startOfValidity = startOfValidity;
	}

}
