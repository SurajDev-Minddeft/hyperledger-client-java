package com.utavi.ledger.api.model;

import com.utavi.ledger.api.dto.OrgInfoDto;
import com.utavi.ledger.api.exceptions.EntityNotFoundException;
import com.utavi.ledger.api.model.enums.OrgType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.modelmapper.PropertyMap;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lc_org_info")
public class OrgInfo extends BaseEntity {

	@NotBlank
	@Column(name = "msp_id")
	private String mspId;

//	@NotBlank
	//todo tymczasowo
	@Column(name = "root_affiliation")
	private String rootAffiliation;

	@Column(name = "number_of_ica")
	private int numberOfIca;

	@Column(name = "number_of_nodes")
	private int numberOfNodes;

	@Column(name = "tls")
	private boolean tls;

	@NotNull
	@Column(name = "org_type")
	@Enumerated(EnumType.STRING)
	private OrgType orgType;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orgInfo")
	private final List<Account> organizationAdmins = new ArrayList<>();

	public OrgInfo(String name, String mspId, String rootAffiliation, int numberOfIca, int numberOfNodes, boolean tls, OrgType orgType) {
		this.name = name;
		this.mspId = mspId;
		this.rootAffiliation = rootAffiliation;
		this.numberOfIca = numberOfIca;
		this.numberOfNodes = numberOfNodes;
		this.tls = tls;
		this.orgType = orgType;
	}

	private OrgInfo() {
	}

	public int getNumberOfIca() {
		return this.numberOfIca;
	}

	public int getNumberOfNodes() {
		return this.numberOfNodes;
	}

	public boolean isTls() {
		return this.tls;
	}

	public String getMspId() {
		return this.mspId;
	}

	public Account getOrgAdmin() {
		return getOrganizationAdmins().stream().findAny()
				.orElseThrow(() -> new EntityNotFoundException("There are no org admins defined for the orgInfo"));
	}

	public String getRootAffiliation() {
		return this.rootAffiliation;
	}

	private List<Account> getOrganizationAdmins() {
		return this.organizationAdmins;
	}

	public OrgType getOrgType() {
		return this.orgType;
	}

	public static PropertyMap<OrgInfo, OrgInfoDto> mappedFields = new PropertyMap<OrgInfo, OrgInfoDto>() {
		protected void configure() {
			map().setMspId(this.source.getMspId());
			map().setName(this.source.getName());
			map().setOrgType(this.source.getOrgType());
			map().setRootAffiliation(this.source.getRootAffiliation());
		}
	};

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final OrgInfo that = (OrgInfo) o;
		return Objects.equals(this.mspId, that.mspId) &&
				Objects.equals(this.rootAffiliation, that.rootAffiliation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.mspId, this.rootAffiliation);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("mspId", this.mspId)
				.append("rootAffiliation", this.rootAffiliation)
				.append("id", getId())
				.append("name", getName())
				.toString();
	}
}
