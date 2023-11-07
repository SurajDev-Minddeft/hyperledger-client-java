package com.utavi.ledger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets/chaincode")
@TestPropertySource(properties = {"chaincode-location=classpath"})
@Import({TestFlywayMigration.class})
public class ChaincodeIT {

//	@Autowired
//	MockMvc mockMvc;
//
//	@Autowired
//	PeerInfoService peerInfoService;
//
//	@Autowired
//	ChaincodeInfoService chaincodeInfoService;
//
//	@Autowired
//	HFChaincodeInvoker hfChaincodeInvoker;
//
//	@Autowired
//	AccountService accountService;
//
//	private final static String TEST_CC_NAME = "testcc" + System.currentTimeMillis();
//	private final static String TEST_CC_PATH = "testcc/go";
//
//	@Test
//	public void test_Install_Instantiate_Upgrade_E2E_Flow() throws Exception {
//		final String channelName = "testchannel" + System.currentTimeMillis();
//		final Set<String> peers = getPeers();
//
//		final int[] intArray = {1, 2, 3, 4, 5};
//		final String payload = Arrays.stream(intArray).mapToObj(String::valueOf).collect(Collectors.joining(" "));
//		final Account account = this.accountService.findPeerAdmin();
//
//		createNewChannel(channelName, peers);
//
//		final String v1 = "v1";
//		writeChaincodeInfo(v1);
//		install(v1, peers);
//		instantiate(channelName, v1);
//
//		final ChaincodeID v1ccId = ChaincodeID.newBuilder().setPath(TEST_CC_PATH).setName(TEST_CC_NAME).setVersion(v1).build();
//
//		sendTransaction(account, channelName, "ADD", payload, v1ccId);
//		queryState(account, channelName, "GET_SUM", "SUM", v1ccId, 15);
//
//		final String v2 = "v2";
//		writeChaincodeInfo(v2);
//		install(v2, peers);
//		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
//		executor.schedule(() -> checkDeployStatus(channelName, v2), 10, TimeUnit.SECONDS);
//		upgrade(channelName, v2);
//
//		final ChaincodeID v2ccId = ChaincodeID.newBuilder().setPath(TEST_CC_PATH).setName(TEST_CC_NAME).setVersion(v2).build();
//		sendTransaction(account, channelName, "MUL", payload, v2ccId);
//		queryState(account, channelName, "GET_MUL", "MUL", v2ccId, 120);
//	}
//
//	private void checkDeployStatus(final String channelName, final String version) {
//		try {
//			this.mockMvc.perform(get("/v1/chaincode/upgrade-status")
//					.param("channelName", channelName)
//					.param("chaincodeName", TEST_CC_NAME))
//					.andExpect(status().isOk())
//					.andExpect(jsonPath("$.chaincodeName", is(TEST_CC_NAME)))
//					.andExpect(jsonPath("$.upgradeStatus", is(ChaincodeUpgradeStatus.IN_PROGRESS.name())))
//					.andExpect(jsonPath("$.deployVersion", is(version)))
//					.andExpect(jsonPath("$.channelName", is(channelName)))
//					.andDo(document("upgrade-status", preprocessRequest(prettyPrint())));
//		} catch (final Exception e) {
//			fail(e.getMessage());
//		}
//	}
//
//	private void createNewChannel(final String channelName, final Set<String> peers) throws Exception {
//		final CreateChannelDto dto = new CreateChannelDto();
//		dto.setChannelName(channelName);
//		dto.setPeers(peers);
//		dto.setOrgType(OrgType.PEER);
//		dto.setOrgName("main-utavi");
//		this.mockMvc.perform(post("/v1/channel")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(dto)))
//				.andExpect(status().isCreated());
//	}
//
//	private ChaincodeInstallRequest createChaincodeInstallRequest(final String version, final Set<String> peers) {
//		final ChaincodeInstallRequest installRequest = new ChaincodeInstallRequest();
//		installRequest.setPeerNames(peers);
//		installRequest.setChaincodeName(TEST_CC_NAME);
//		installRequest.setChaincodeVersion(version);
//		return installRequest;
//	}
//
//	private void instantiate(final String channelName, final String version) throws Exception {
//		this.mockMvc.perform(post("/v1/chaincode/instantiate")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(createChaincodeInstantiateRequest(channelName, version))))
//				.andExpect(status().isAccepted()).andDo(document("instantiate-chaincode", preprocessRequest(prettyPrint())));
//	}
//
//	private void upgrade(final String channelName, final String version) throws Exception {
//		this.mockMvc.perform(post("/v1/chaincode/upgrade")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(createChaincodeInstantiateRequest(channelName, version))))
//				.andExpect(status().isAccepted()).andDo(document("upgrade-chaincode", preprocessRequest(prettyPrint())));
//	}
//
//	private void install(final String version, final Set<String> peers) throws Exception {
//		this.mockMvc.perform(post("/v1/chaincode/install")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(createChaincodeInstallRequest(version, peers))))
//				.andExpect(status().isAccepted()).andDo(document("install-chaincode", preprocessRequest(prettyPrint())));
//	}
//
//	private ChaincodeInstantiateRequest createChaincodeInstantiateRequest(final String channel, final String version) {
//		final ChaincodeInstantiateRequest request = new ChaincodeInstantiateRequest();
//		request.setChannelName(channel);
//		request.setChaincodeName(TEST_CC_NAME);
//		request.setChaincodeVersion(version);
//		return request;
//	}
//
//	private void writeChaincodeInfo(final String version) throws Exception {
//		this.mockMvc.perform(post("/v1/chaincode/info")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(createChaincodeMetaInf(version))))
//				.andExpect(status().isAccepted()).andDo(document("write-new-chaincode-info", preprocessRequest(prettyPrint())));
//	}
//
//	private ChaincodeMetaInf createChaincodeMetaInf(final String version) {
//		final ChaincodeMetaInf metaInf = new ChaincodeMetaInf();
//		metaInf.setVersion(version);
//		metaInf.setName(TEST_CC_NAME);
//		metaInf.setLang(Type.GO_LANG);
//		metaInf.setPath(TEST_CC_PATH);
//		return metaInf;
//	}
//
//	private void sendTransaction(final Account account, final String channelName, final String function, final String payload,
//			final ChaincodeID chaincodeID) {
//		this.hfChaincodeInvoker.sendTransaction(account, channelName, function, payload, Collections.emptyMap(), chaincodeID).join();
//	}
//
//	private void queryState(final Account account, final String channelName, final String function, final String payload,
//			final ChaincodeID chaincodeID, final int expectedValue) {
//		this.hfChaincodeInvoker.readState(account, channelName, function, payload, Collections.emptyMap(), chaincodeID)
//				.forEach((k, v) -> assertEquals(expectedValue, Integer.parseInt(v.getPayload().toStringUtf8())));
//	}
//
//	private Set<String> getPeers() {
//		return this.peerInfoService.findPeers().stream().limit(3).map(PeerInfo::getName).collect(Collectors.toSet());
//	}

}
