package com.utavi.ledger.api.service.client;

import com.utavi.ledger.api.service.network.ca.HFCAServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CAIdentityService {

	private final static Logger LOGGER = LoggerFactory.getLogger(CAIdentityService.class.getName());

	private final HFCAServerService hfcaServerService;
	private final AccountService accountService;

	public CAIdentityService(final HFCAServerService hfcaServerService, final AccountService accountService) {
		this.hfcaServerService = hfcaServerService;
		this.accountService = accountService;
	}

//	@Transactional
//	public void enroll(final String name, final OrgType orgType) {
//		LOGGER.info("Received enroll request for account {}", name);
//		final Account account = this.accountService.findOrgByType(name);
//		final Enrollment enrollment = this.hfcaServerService.enroll(name, account.getEnrollmentSecret(), orgType);
//		LocalEnrollment localEnrollment = new LocalEnrollment(enrollment.getKey(), enrollment.getCert());
//		account.setLocalEnrollment(localEnrollment);
//		this.accountService.save(account);
//		LOGGER.info("Successfully enrolled a new enrollment certificate for account {}", name);
//	}

	// Revocation is currentlty the process of setting account isActive flag to false.
	//
	// Hyperledger Fabric certificates revocation is not currently executed.
	// Hyperledger Fabric account revocation is the process which consist of few steps:
	// - revoking certificate in Fabric CA
	// - saving information about revoked certificate to CRL List of peers/orderers. Using these list
	// blockchain network know that these certificates are invalid.
	// Currently Hyperledger Fabric revocation is not executed. This process is complex and currently
	// certificates are not shared to end users, what means that they 100% managed by Utavi backend services.
	// Executing Hyperledger Fabric revocation would be currently  unnecessary overhead.
	// If certificates will be in future provided to the end users and be managed by users, then Hyperledger Fabric
	// revocation list would be required.
	public void revoke(final String accountName) {
		LOGGER.info("Received revoke request for account {}", accountName);
		this.accountService.setAccountToNotActive(accountName);
//		revokeInternal(account, channels);
	}

//	@Transactional
//	public void reEnroll(final String name, final OrgType orgType) {
//		final Account account = this.accountService.findOrgByType(name);
//		reEnroll(account, orgType);
//	}

//	@Transactional
//	public void reEnroll(final Account account, final OrgType orgType) {
//		final Enrollment enrollment = this.hfcaServerService.reEnroll(account, orgType);
//		LocalEnrollment localEnrollment = new LocalEnrollment(enrollment.getKey(), enrollment.getCert());
//		account.setLocalEnrollment(localEnrollment);
//		this.accountService.save(account);
//	}

}
