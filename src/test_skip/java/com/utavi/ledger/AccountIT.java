package com.utavi.ledger;

import static com.utavi.ledger.api.util.Utils.fromJson;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.utavi.ledger.config.CompleteBlockchainSetupRunner;
import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.service.network.ResourceServerService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets/account")
@Import({TestFlywayMigration.class, CompleteBlockchainSetupRunner.class})
public class AccountIT {

//	@Autowired
//	MockMvc mockMvc;
//
//	@Autowired
//	AccountService accountService;
//
//	@Autowired
//	ChannelInfoManager channelInfoManager;
//
//	@Autowired
//	ResourceServerService resourceServerService;
//
//	@Autowired
//	OrganizationProperties organizationProperties;
//
//	private final List<String> attributeKeys = new ArrayList<>();
//
//	@Autowired
//	CAServerInfoService caServerService;
//
//	@Autowired
//	HFCAClientUtil hfcaClientUtil;
//
//	@Value("${organization.peer.ica.bootstrap-user}")
//	private String bootstrapUser;
//
//	@Before
//	public void setup() {
//		this.attributeKeys.add("member.of");
//		this.attributeKeys.add("membership.status");
//		this.attributeKeys.add("account.type");
//	}
//
//	private String getSiloName() {
//		final String channel = this.organizationProperties.getTargetTestChannel(TestTarget.BLOCKCHAIN);
//		return this.channelInfoManager.find(channel).getSiloName();
//	}
//
//	private Map<String, String> attributesMap(final String memberOfValue, final AccountType accountType) {
//		final Map<String, String> attributes = new HashMap<>();
//		attributes.put("member.of", memberOfValue);
//		attributes.put("account.type", accountType.name());
//		return attributes;
//	}
//
//	@Test
//	public void testChangePassword() throws Exception {
//		final String accountName = createAccountName();
//		createAccount(accountName, IdentityType.ADMIN, AccountType.PEER_ADMIN);
//		final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
//		changePasswordRequest.setOrgType(OrgType.PEER);
//		changePasswordRequest.setPassword("qweasdzxc");
//		this.mockMvc.perform(put("/v1/account/{name}/password", accountName)
//				.content(toJson(changePasswordRequest))
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isNoContent())
//				.andDo(document("account-change-password", preprocessRequest(prettyPrint())));
//
//		enrollRequest(accountName).andExpect(status().isAccepted());
//	}
//
//	@Test
//	public void testPublishAdminCertToMsp() throws Exception {
//		final String accountName = createAccountName();
//		createAccount(accountName, IdentityType.ADMIN, AccountType.PEER_ADMIN);
//
//		this.mockMvc.perform(put("/v1/account/admin/{name}", accountName)
//				.param("orgType", "PEER")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isAccepted())
//				.andDo(document("publish-admin-cert-to-msp", preprocessRequest(prettyPrint())));
//	}
//
//	@Test
//	public void testCreateAccount() throws Exception {
//		final String accountName = createAccountName();
//		createAccount(accountName, IdentityType.REGISTRAR, AccountType.BOOTSTRAP_ACCOUNT)
//				.andDo(document("create-account", preprocessRequest(prettyPrint())));
//
//		this.mockMvc.perform(get("/v1/account/{accountName}", accountName)
//				.param("includeCAInfo", "false")
//				.param("orgType", OrgType.PEER.name())
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.name", is(accountName)))
//				.andExpect(jsonPath("$.accountType", is(AccountType.BOOTSTRAP_ACCOUNT.name())));
//	}
//
//	private ResultActions createAccount(final String name, final IdentityType identityType, final AccountType accountType) throws Exception {
//		final CreateAccount createAccount = new CreateAccount();
//		createAccount.setName(name);
//		createAccount.setOrgType(OrgType.PEER);
//		createAccount.setIdentityType(identityType);
//		createAccount.setAccountType(accountType);
//		return this.mockMvc.perform(post("/v1/account")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(createAccount)))
//				.andExpect(status().isCreated());
//	}
//
//	@Test
//	public void findCAIdentitiesByRegistrar() throws Exception {
//		final String accountName = createAccountName();
//		joinSilo(getSiloName(), accountName, AccountType.INVESTOR);
//		this.mockMvc.perform(get("/v1/account/ca/{registrar}", this.bootstrapUser)
//				.param("account", accountName)
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$").isArray())
//				.andExpect(jsonPath("$").isNotEmpty())
//				.andDo(document("find-single-ca-identity-by-registrar", preprocessRequest(prettyPrint())));
//
//		this.mockMvc.perform(get("/v1/account/ca/{registrar}", this.bootstrapUser)
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$").isArray())
//				.andExpect(jsonPath("$").isNotEmpty())
//				.andDo(document("find-ca-identities-by-registrar", preprocessRequest(prettyPrint())));
//	}
//
//	@Test
//	public void findAccounts() throws Exception {
//		this.mockMvc.perform(get("/v1/account")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andDo(document("find-accounts", preprocessRequest(prettyPrint())));
//	}
//
//	private AccountInfoDto findAccount(final String accName) throws Exception {
//		return fromJson(this.mockMvc.perform(get("/v1/account/{accountName}", accName)
//				.param("includeCAInfo", "false")
//				.param("orgType", OrgType.PEER.name())
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andDo(document("find-account", preprocessRequest(prettyPrint())))
//				.andReturn().getResponse().getContentAsString(), AccountInfoDto.class);
//	}
//
//	@Test
//	public void joinSiloTest() throws Exception {
//		final String businessAccName = createAccountName();
//		final String investorAccName = createAccountName();
//
//		final String siloName = getSiloName();
//		joinSilo(siloName, investorAccName, AccountType.INVESTOR).andDo(document("account-join-silo", preprocessRequest(prettyPrint()),
//				requestFields(
//						fieldWithPath("accountId").description("Id of an account to be joined"),
//						fieldWithPath("siloName").description("Id of a silo to join an account"),
//						fieldWithPath("accountType")
//								.description(String.format("Type of an account to be joined. Available types: %s", Arrays.toString(AccountType.values())))
//				)));
//		joinSilo(siloName, businessAccName, AccountType.BUSINESS);
//
//		final String businessAccMembership = findAccount(businessAccName).getMembershipInfo();
//		final String investorAccMembership = findAccount(investorAccName).getMembershipInfo();
//
//		checkAttributesForIdentity(businessAccName, attributesMap(businessAccMembership, AccountType.BUSINESS));
//		checkAttributesForIdentity(investorAccName, attributesMap(investorAccMembership, AccountType.INVESTOR));
//	}
//
//	@Test
//	public void joinLeaveJoinTest() throws Exception {
//		final String businessAccName = createAccountName();
//		final String investorAccName = createAccountName();
//
//		final String siloName = getSiloName();
//		joinSilo(siloName, businessAccName, AccountType.BUSINESS);
//		joinSilo(siloName, investorAccName, AccountType.INVESTOR);
//
//		final String approvedFirstBusinessAccMembership = findAccount(businessAccName).getMembershipInfo();
//		final String approvedFirstInvestorAccMembership = findAccount(investorAccName).getMembershipInfo();
//
//		checkAttributesForIdentity(businessAccName, attributesMap(approvedFirstBusinessAccMembership, AccountType.BUSINESS));
//		checkAttributesForIdentity(investorAccName, attributesMap(approvedFirstInvestorAccMembership, AccountType.INVESTOR));
//
//		leaveSilo(siloName, businessAccName);
//		leaveSilo(siloName, investorAccName);
//
//		final String inactiveBusinessAccMembership = findAccount(businessAccName).getMembershipInfo();
//		final String inactiveInvestorAccMembership = findAccount(investorAccName).getMembershipInfo();
//
//		checkAttributesForIdentity(businessAccName, attributesMap(inactiveBusinessAccMembership, AccountType.BUSINESS));
//		checkAttributesForIdentity(investorAccName, attributesMap(inactiveInvestorAccMembership, AccountType.INVESTOR));
//
//		joinSilo(siloName, businessAccName, AccountType.BUSINESS);
//		joinSilo(siloName, investorAccName, AccountType.INVESTOR);
//
//		final String approvedSecondBusinessAccMembership = findAccount(businessAccName).getMembershipInfo();
//		final String approvedSecondInvestorAccMembership = findAccount(investorAccName).getMembershipInfo();
//
//		checkAttributesForIdentity(businessAccName, attributesMap(approvedSecondBusinessAccMembership, AccountType.BUSINESS));
//		checkAttributesForIdentity(investorAccName, attributesMap(approvedSecondInvestorAccMembership, AccountType.INVESTOR));
//	}
//
//	@Test
//	public void leaveSiloTest() throws Exception {
//		final String businessAccName = createAccountName();
//		final String investorAccName = createAccountName();
//
//		final String siloName = getSiloName();
//
//		joinSilo(siloName, investorAccName, AccountType.INVESTOR);
//		joinSilo(siloName, businessAccName, AccountType.BUSINESS);
//
//		leaveSilo(siloName, businessAccName).andDo(document("account-leave-silo", preprocessRequest(prettyPrint())));
//		leaveSilo(siloName, investorAccName);
//
//		final String businessAccMembership = findAccount(businessAccName).getMembershipInfo();
//		final String investorAccMembership = findAccount(investorAccName).getMembershipInfo();
//
//		checkAttributesForIdentity(businessAccName, attributesMap(businessAccMembership, AccountType.BUSINESS));
//		checkAttributesForIdentity(investorAccName, attributesMap(investorAccMembership, AccountType.INVESTOR));
//	}
//
//	@Test
//	public void revokeAccount() throws Exception {
//		final String name = createAccountName();
//		final String siloName = getSiloName();
//		joinSilo(siloName, name, AccountType.BUSINESS);
//		revokeAccountRequest(name);
//	}
//
//	@Test
//	public void reEnrollAccount() throws Exception {
//		final String name = createAccountName();
//		final String siloName = getSiloName();
//		joinSilo(siloName, name, AccountType.BUSINESS);
//		reEnrollRequest(name);
//	}
//
//	@Test
//	public void enrollAccount() throws Exception {
//		final String name = createAccountName();
//		final String siloName = getSiloName();
//		joinSilo(siloName, name, AccountType.BUSINESS);
//		enrollRequest(name)
//				.andDo(document("enroll-account", preprocessRequest(prettyPrint())))
//				.andReturn();
//	}
//
//	private ResultActions enrollRequest(final String enrollmentId) throws Exception {
//		return this.mockMvc.perform(put("/v1/account/enroll/{enrollmentId}", enrollmentId)
//				.param("orgType", OrgType.PEER.name())
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isAccepted());
//	}
//
//	private void reEnrollRequest(final String enrollmentId) throws Exception {
//		this.mockMvc.perform(put("/v1/account/re-enroll/{enrollmentId}", enrollmentId)
//				.param("orgType", OrgType.PEER.name())
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isAccepted())
//				.andDo(document("re-enroll-account", preprocessRequest(prettyPrint())))
//				.andReturn();
//	}
//
//	private void revokeAccountRequest(final String enrollmentId) throws Exception {
//		this.mockMvc.perform(put("/v1/account/revoke/{enrollmentId}", enrollmentId)
//				.param("orgType", OrgType.PEER.name())
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isAccepted())
//				.andDo(document("revoke-enrollment", preprocessRequest(prettyPrint())))
//				.andReturn();
//	}
//
//	private ResultActions leaveSilo(final String siloName, final String externalId) throws Exception {
//		return this.mockMvc
//				.perform(post("/v1/account/leave").contentType(MediaType.APPLICATION_JSON).content(toJson(createLeaveSiloDto(siloName, externalId))))
//				.andExpect(status().isAccepted());
//	}
//
//	private ResultActions joinSilo(final String channelName, final String externalId, final AccountType accountType) throws Exception {
//		return this.mockMvc.perform(post("/v1/account/join")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(createJoinSiloDto(channelName, externalId, accountType))))
//				.andExpect(status().isAccepted());
//	}
//
//	private void checkAttributesForIdentity(final String name, final Map<String, String> attributes) throws Exception {
//		final CAServerInfo caServerInfo = this.caServerService.findOneByOrgType(OrgType.PEER);
//
//		final Properties caProperties;
//		final String orgName = caServerInfo.getOrgName();
//		if (caServerInfo.isTls()) {
//			final byte[] bytes = this.resourceServerService.requestCATlsClientCert(orgName);
//			caProperties = TlsUtils.makeCAServerTlsProperties(bytes, caServerInfo.getName());
//		} else {
//			caProperties = new Properties();
//		}
//
//		final Account registrar = this.accountService.findRegistrar(OrgType.PEER);
//		final HFCAClient hfcaClient = HFCAClient.createNewInstance(caServerInfo.getLocation(), caProperties);
//		hfcaClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
//		hfcaClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
//		hfcaClient.getHFCAIdentities(registrar).stream().filter(hfcaIdentity -> name.equals(hfcaIdentity.getEnrollmentId()))
//				.forEach(hfcaIdentity -> hfcaIdentity.getAttributes().stream().filter(attribute -> this.attributeKeys.contains(attribute.getName()))
//						.forEach(attribute -> compareAttributeValues(attributes, attribute, name)));
//	}
//
//	private void compareAttributeValues(final Map<String, String> attrs, final Attribute attribute, final String name) {
//		final String attributeName = attribute.getName();
//		final String value = attribute.getValue();
//		final String expectedAttributeValue = attrs.get(attributeName);
//		assertNotNull(expectedAttributeValue);
//		final String message =
//				String.format("Account %s has an attribute %s with the value %s, but expected %s", name, attributeName, value, expectedAttributeValue);
//		assertEquals(message, value, expectedAttributeValue);
//	}

}
