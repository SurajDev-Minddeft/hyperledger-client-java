package com.utavi.ledger.data;

import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.dto.manage.NetworkConfigDto.OrgConfig;

public final class OrgInfoTestData {

	public static String ORGANIZATION_NAME = "ORGANIZATION_NAME";
	public static final String ORGANIZATION_ADMIN_NAME = "ORGANIZATION_ADMIN_NAME";
	public static final String ROOT_AFFILIATION = "domain.org";

	public static OrgInfo makeOrgInfo(final String orgName, final OrgType orgType, final boolean tls) {
		final OrgConfig orgConfig = new OrgConfig();
		orgConfig.setName(orgName);
		orgConfig.setRootAffiliation(orgName + ROOT_AFFILIATION);
		orgConfig.setIcaNum(1);
		orgConfig.setNumberOfNodes(2);
		return new OrgInfo(orgType, orgConfig, tls);
	}

}
