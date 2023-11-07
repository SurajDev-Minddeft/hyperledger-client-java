//todo seems to be not used
//package com.utavi.ledger.api.cron;
//
//import com.utavi.ledger.api.model.enums.OrgType;
//import com.utavi.ledger.api.service.client.AccountService;
//import com.utavi.ledger.api.service.network.ca.HFCAServerService;
//import java.io.IOException;
//import java.util.Arrays;
//
//import org.hyperledger.fabric_ca.sdk.HFCAX509Certificate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import sun.security.x509.X500Name;
//
//@Component
//@ConditionalOnProperty(value = "scheduling.reenroll.enabled", havingValue = "true")
//public class ReenrollmentJob {
//
//	private final static Logger LOGGER = LoggerFactory.getLogger(ReenrollmentJob.class.getName());
//
//	private final HFCAServerService hfcaServerService;
//	private final AccountService accountService;
//
//	public ReenrollmentJob(final HFCAServerService hfcaServerService, final AccountService accountService) {
//		this.hfcaServerService = hfcaServerService;
//		this.accountService = accountService;
//	}
//
//	@Scheduled(cron = "0 1 1 * * ?")
//	public void reenrollAccounts() {
//		LOGGER.info("Running re-enrollment job...");
//		Arrays.stream(OrgType.values()).forEach(orgType -> this.hfcaServerService.findToBeExpired(this.accountService.findRegistrar(orgType))
//				.stream()
//				.map(HFCAX509Certificate.class::cast).forEach(cert -> reenroll(getCn(cert), orgType)));
//		LOGGER.info("Re-enrollment job finished");
//	}
//
//	private String getCn(final HFCAX509Certificate certificate) {
//		try {
//			return X500Name.asX500Name(certificate.getX509().getSubjectX500Principal()).getCommonName();
//		} catch (final IOException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	private void reenroll(final String enrollmentId, final OrgType orgType) {
//		LOGGER.info("Reenrolling account {} from org {}", enrollmentId, orgType);
////		this.accountService.reEnroll(enrollmentId, orgType);
//		LOGGER.info("Successfully reenrolled account {} from org {}", enrollmentId, orgType);
//	}
//
//}
