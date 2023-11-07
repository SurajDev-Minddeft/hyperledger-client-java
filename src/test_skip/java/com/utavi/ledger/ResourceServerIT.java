package com.utavi.ledger;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.utavi.ledger.config.TestDbRunner;
import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.service.network.ResourceServerService;
import com.utavi.ledger.api.service.manage.OrganizationConfigManagerService;
import java.util.Map;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import({TestFlywayMigration.class, TestDbRunner.class})
public class ResourceServerIT {

	@Autowired
	ResourceServerService resourceServerService;

	@Autowired
	OrganizationConfigManagerService organizationConfigManagerService;

	@Test
	public void testLoadAdminCertificates() {
		final OrgInfo peerOrg = this.organizationConfigManagerService.findPeerOrg();
		final Map<String, byte[]> certificatesMap = this.resourceServerService.loadAdminCertificates(peerOrg.getName());

		assertThat(certificatesMap.size(), is(2));
		assertThat(certificatesMap, IsMapContaining.hasKey("certificate"));
		assertThat(certificatesMap, IsMapContaining.hasKey("privateKey"));

		final byte[] certificate = certificatesMap.get("certificate");
		assertNotNull(certificate);
		final byte[] privateKeys = certificatesMap.get("privateKey");
		assertNotNull(privateKeys);

		assertTrue(certificate.length > 0);
		assertTrue(privateKeys.length > 0);
	}

//	@Test
//	public void testGetNetworkConfig() {
//		final NetworkConfigDto networkConfigDto = this.resourceServerService.getNetworkConfig();
//		assertNotNull(networkConfigDto);
//		final OrdererOrg ordererOrgs = networkConfigDto.getOrdererOrgs();
//		assertNotNull(ordererOrgs);
//		assertTrue(ordererOrgs.getIcaNum() > 0);
//		assertTrue(ordererOrgs.getOrderersNum() > 0);
//		assertTrue(StringUtils.isNotBlank(ordererOrgs.getName()));
//		final PeerOrg peerOrgs = networkConfigDto.getPeerOrgs();
//		assertNotNull(peerOrgs);
//		assertTrue(peerOrgs.getIcaNum() > 0);
//		assertTrue(peerOrgs.getPeersNum() > 0);
//		assertTrue(StringUtils.isNotBlank(peerOrgs.getName()));
//	}
}

