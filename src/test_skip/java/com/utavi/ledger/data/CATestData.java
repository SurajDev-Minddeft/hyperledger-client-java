package com.utavi.ledger.data;

import com.utavi.ledger.api.model.CAServerInfo;
import com.utavi.ledger.api.model.OrgInfo;

public final class CATestData {

	public static CAServerInfo makeCAServerInfo(final String name, final String fabricHost, final boolean isTls, final int startPort, final OrgInfo orgInfo) {
		final String location = (isTls ? "https" : "http") + "://" + fabricHost + ":" + startPort;
		return new CAServerInfo(name, location, orgInfo);
	}

}
