package com.utavi.ledger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.service.client.CAServerInfoService;
import com.utavi.ledger.api.service.client.OrgInfoService;
import com.utavi.ledger.api.service.network.ResourceServerService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import({TestFlywayMigration.class})
public class OrganizationConfigDtoIntIT {

//	@Autowired
//	private NetworkConfigManager networkConfigManager;

	@Autowired
	private ResourceServerService resourceServerService;

//	@Autowired
//	private PeerInfoService peerInfoService;
//
//	@Autowired
//	private OrdererInfoService ordererInfoService;

	@Autowired
	private CAServerInfoService caServerInfoService;

	@Autowired
	private OrgInfoService orgInfoService;

//	@Test
//	public void test() {
//		this.networkConfigManager.writeNetworkConfiguration();
//		final NetworkConfigDto networkConfigDto = this.resourceServerService.getNetworkConfig();
//		final PeerOrg peerOrgInfo = networkConfigDto.getPeerOrgs();
//		final String peerOrgName = peerOrgInfo.getName();
//		final OrgInfo peerOrg = this.orgInfoService.findPeerOrg();
//
//		assertNotNull(peerOrg);
//		assertEquals(peerOrgName, peerOrg.getName());
//
//		final OrdererOrg ordererOrgInfo = networkConfigDto.getOrdererOrgs();
//		final String ordererOrgName = ordererOrgInfo.getName();
//		final OrgInfo ordererOrg = this.orgInfoService.findOrdererOrg();
//
//		assertNotNull(ordererOrg);
//		assertEquals(ordererOrgName, ordererOrg.getName());
//
//		final int peersNum = peerOrgInfo.getPeersNum();
//		final Set<PeerInfo> peers = this.peerInfoService.findPeers();
//		assertEquals(peersNum, peers.size());
//
//		final Set<CAServerInfo> peerIcaServers = this.caServerInfoService.findByOrgType(OrgType.PEER);
//		final int peerIcaNum = peerOrgInfo.getIcaNum();
//		assertEquals(peerIcaNum, peerIcaServers.size());
//
//		final Set<CAServerInfo> ordererIcaServers = this.caServerInfoService.findByOrgType(OrgType.ORDERER);
//		final int ordererIcaNum = ordererOrgInfo.getIcaNum();
//		assertEquals(ordererIcaNum, ordererIcaServers.size());
//
//		final Set<OrdererInfo> orderers = this.ordererInfoService.findOrderers();
//		final int orderersNum = ordererOrgInfo.getOrderersNum();
//		assertEquals(orderersNum, orderers.size());
//	}
}
