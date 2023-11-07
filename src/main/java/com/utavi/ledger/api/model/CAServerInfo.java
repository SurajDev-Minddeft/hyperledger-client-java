package com.utavi.ledger.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.utavi.ledger.api.model.enums.OrgType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

@Entity
@Table(name = "lc_ca_server_info")
public class CAServerInfo extends BaseOrganizationComponent {

	@NotNull
	@Column(name = "org_type")
	@Enumerated(EnumType.STRING)
	private OrgType orgType;

	public CAServerInfo() {
	}

	public CAServerInfo(final String name, final String location, final OrgInfo orgInfo, final String tlsCertificate) {
		super(name, location, orgInfo, tlsCertificate);
		this.orgType = orgInfo.getOrgType();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		CAServerInfo that = (CAServerInfo) o;
		return orgType == that.orgType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), orgType);
	}

	@Override
	public String toString() {
		return "CAServerInfo{" +
				   "orgType=" + orgType +
				   ", orgInfo=" + orgInfo +
				   ", id=" + id +
				   ", name='" + name + '\'' +
				   '}';
	}
}
