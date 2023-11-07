package com.utavi.ledger.data;

import static com.utavi.ledger.data.OrgInfoTestData.ORGANIZATION_ADMIN_NAME;
import static com.utavi.ledger.data.OrgInfoTestData.ORGANIZATION_NAME;
import static com.utavi.ledger.data.OrgInfoTestData.makeOrgInfo;

import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.model.enums.OrgType;

public final class AccountTestData {

	public static final String ACCOUNT_NAME = "AccountName";
	public static final String SILO_ADMIN_ID = "SiloAdminId";
	private static final AccountType BUSINESS_ACCOUNT_TYPE = AccountType.BUSINESS;

	public static Account makeBusinessUserWithSilo() {
		final OrgInfo orgInfo = makeOrgInfo(ORGANIZATION_NAME, OrgType.PEER, false);
		final Account businessUser = new Account(orgInfo);
		businessUser.setName(ACCOUNT_NAME);
		businessUser.setAccountType(BUSINESS_ACCOUNT_TYPE);
//		final ChannelInfo channelInfo = makeChannelInfo();
		businessUser.setMspId(orgInfo.getMspId());
//		final Membership membership = createMembership(channelInfo);
//		businessUser.addMembership(membership);
		businessUser.setEncryptedEnrollment("HEX_ENROLLMENT");
		return businessUser;
	}

	public static Account makeBusinessUserWithoutMembership() {
		final OrgInfo orgInfo = makeOrgInfo(ORGANIZATION_NAME, OrgType.PEER, false);
		final Account businessUser = new Account(orgInfo);
		businessUser.setName(ACCOUNT_NAME);
		businessUser.setMspId(orgInfo.getMspId());
		businessUser.setAccountType(AccountType.BUSINESS);
		return businessUser;
	}

	public static Account makePeerAdmin() {
		final OrgInfo orgInfo = makeOrgInfo(ORGANIZATION_NAME, OrgType.PEER, false);
		final Account organizationAdmin = new Account(orgInfo);
		organizationAdmin.setName(ORGANIZATION_ADMIN_NAME);
		organizationAdmin.setMspId(orgInfo.getMspId());
		organizationAdmin.setAccountType(AccountType.PEER_ADMIN);
		return organizationAdmin;
	}

	public static Account makeRegistrar() {
		final OrgInfo orgInfo = makeOrgInfo(ORGANIZATION_NAME, OrgType.PEER, false);
		final Account organizationAdmin = new Account(orgInfo);
		organizationAdmin.setName(ORGANIZATION_ADMIN_NAME);
		organizationAdmin.setMspId(orgInfo.getMspId());
		organizationAdmin.setAccountType(AccountType.BOOTSTRAP_ACCOUNT);
		return organizationAdmin;
	}

}
