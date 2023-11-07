package com.utavi.ledger.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.utavi.ledger.api.model.enums.OrgType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseOrganizationComponent extends BaseEntity {

	BaseOrganizationComponent() {
	}

	public BaseOrganizationComponent(final String name, final String location, final OrgInfo orgInfo, final String tlsCertificate) {
		this.name = name;
		this.location = location;
		this.orgInfo = orgInfo;
		this.tlsCertificate = tlsCertificate;
	}

	@NotBlank
	@Column(name = "location")
	private String location;

	@ManyToOne
	@JoinColumn(name = "organization_id")
	@JsonBackReference
	protected OrgInfo orgInfo;

	@NotNull
	@Column(name = "tls_certificate")
	private String tlsCertificate;

	@Transient
	public OrgType getOrgType() {
		return this.orgInfo.getOrgType();
	}

	@Transient
	public String getOrgName() {
		return this.orgInfo.getName();
	}

	public String getLocation() {
		return this.location;
	}

	public OrgInfo getOrgInfo() {
		return orgInfo;
	}

	public String getTlsCertificate() {
		return tlsCertificate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		BaseOrganizationComponent that = (BaseOrganizationComponent) o;
		return Objects.equals(location, that.location) && Objects.equals(orgInfo, that.orgInfo) && Objects.equals(tlsCertificate, that.tlsCertificate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), location, orgInfo, tlsCertificate);
	}

	@Override
	public String toString() {
		return "BaseOrganizationComponent{" +
				   "location='" + location + '\'' +
				   ", orgInfo=" + orgInfo +
				   ", tlsCertificate='" + tlsCertificate + '\'' +
				   ", id=" + id +
				   ", name='" + name + '\'' +
				   '}';
	}
}
