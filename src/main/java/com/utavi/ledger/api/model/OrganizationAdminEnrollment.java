package com.utavi.ledger.api.model;

import java.io.Serializable;
import java.security.PrivateKey;
import org.hyperledger.fabric.sdk.Enrollment;

public class OrganizationAdminEnrollment implements Enrollment, Serializable {

	private static final long serialVersionUID = -2784835212445309006L;
	private final PrivateKey privateKey;
	private final String certificate;

	public OrganizationAdminEnrollment(final PrivateKey privateKey, final String certificate) {
		this.certificate = certificate;
		this.privateKey = privateKey;
	}

	@Override
	public PrivateKey getKey() {
		return this.privateKey;
	}

	@Override
	public String getCert() {
		return this.certificate;
	}

}
