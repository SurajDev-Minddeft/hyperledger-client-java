package com.utavi.ledger;

import static com.utavi.ledger.api.util.Utils.createAccountName;
import static com.utavi.ledger.api.util.Utils.fromJson;
import static com.utavi.ledger.api.util.Utils.toJson;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.utavi.ledger.api.OrganizationProperties;
import com.utavi.ledger.api.OrganizationProperties.TestTarget;
import com.utavi.ledger.config.SingleChannelBlockchainSetupRunner;
import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.dto.CreateAccount;
import com.utavi.ledger.api.dto.OrgInfoDto;
import com.utavi.ledger.api.dto.channel.config.AbsoluteMaxBytesRequest;
import com.utavi.ledger.api.dto.channel.config.AddOrgAdminRequest;
import com.utavi.ledger.api.dto.channel.config.MaxMessageCountRequest;
import com.utavi.ledger.api.dto.channel.config.OrgConfigRequest;
import com.utavi.ledger.api.dto.channel.config.PreferredMaxBytesRequest;
import com.utavi.ledger.api.dto.channel.config.TimeoutRequest;
import com.utavi.ledger.api.dto.channel.config.TimeoutResponse;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.model.enums.IdentityType;
import com.utavi.ledger.api.repository.AccountRepository;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets/channel-config")
@Import({TestFlywayMigration.class, SingleChannelBlockchainSetupRunner.class})
public class UpdateChannelConfigIT {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	OrganizationProperties orgProperties;

	@Autowired
	AccountRepository accountRepository;

	@Test
	public void testAddOrgAdminToChannel() throws Exception {
		final OrgInfoDto orgInfoDto = fromJson(this.mockMvc.perform(get("/v1/org")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString(), new TypeReference<Set<OrgInfoDto>>() {}).stream()
				.filter(dto -> dto.getOrgType() == OrgType.PEER)
				.findAny().get();

		final CreateAccount createAccount = new CreateAccount();
		final String accountName = createAccountName();
		createAccount.setName(accountName);
		createAccount.setOrgType(OrgType.PEER);
		createAccount.setIdentityType(IdentityType.PEER);
		createAccount.setAccountType(AccountType.PEER_ADMIN);
		this.mockMvc.perform(post("/v1/account")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(createAccount)))
				.andExpect(status().isCreated());

		final String channelName = getChannelName();
		final AddOrgAdminRequest request = new AddOrgAdminRequest();
		request.setChannels(Collections.singleton(channelName));
		request.setOrgName(orgInfoDto.getName());
		request.setName(accountName);

		this.mockMvc.perform(post("/v1/channel-config/org/admin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(request)))
				.andExpect(status().isAccepted())
				.andDo(document("add-org-admin-to-channel", preprocessRequest(prettyPrint())));
	}

	@Test
	public void testUpdateBatchTimeout() throws Exception {
		final String channelName = getChannelName();

		final TimeoutRequest timeoutRequest = new TimeoutRequest();

		final TimeoutResponse batchTimeout = fromJson(getBatchTimeout(channelName).andReturn().getResponse().getContentAsString(),
				TimeoutResponse.class);
		final int timeout = getRandomRangeInt(1, 20) + Integer.valueOf(batchTimeout.getTimeout().replace("s", ""));
		timeoutRequest.setTimeout(timeout);

		this.mockMvc.perform(put("/v1/channel-config/{channelName}/timeout", channelName)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(timeoutRequest)))
				.andExpect(status().isAccepted())
				.andDo(document("update-batch-timeout", preprocessRequest(prettyPrint())));

		getBatchTimeout(channelName)
				.andExpect(jsonPath("$.timeout", is(timeout + "s")))
				.andDo(document("get-batch-timeout", preprocessRequest(prettyPrint())));
	}

	public ResultActions getBatchTimeout(final String channelName) throws Exception {
		return this.mockMvc.perform(get("/v1/channel-config/{channelName}/timeout", channelName)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdatePreferredMaxBytes() throws Exception {
		final String channelName = getChannelName();

		final PreferredMaxBytesRequest currentPreferredMaxBytes = fromJson(
				getPreferredMaxBytes(channelName).andReturn().getResponse().getContentAsString(), PreferredMaxBytesRequest.class);

		final PreferredMaxBytesRequest preferredMaxBytesRequest = new PreferredMaxBytesRequest();
		final int preferredMaxBytes = getRandomRangeInt(128, 256) + currentPreferredMaxBytes.getPreferredMaxBytes();
		preferredMaxBytesRequest.setPreferredMaxBytes(preferredMaxBytes);

		this.mockMvc.perform(put("/v1/channel-config/{channelName}/preferred-max-bytes", channelName)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(preferredMaxBytesRequest)))
				.andExpect(status().isAccepted())
				.andDo(document("update-preferred-max-bytes", preprocessRequest(prettyPrint())));

		getPreferredMaxBytes(channelName)
				.andExpect(jsonPath("$.preferredMaxBytes", is(preferredMaxBytes)))
				.andDo(document("get-preferred-max-bytes", preprocessRequest(prettyPrint())));
	}

	private ResultActions getPreferredMaxBytes(final String channelName) throws Exception {
		return this.mockMvc.perform(get("/v1/channel-config/{channelName}/preferred-max-bytes", channelName)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateMaxMessageCount() throws Exception {
		final String channelName = getChannelName();

		final MaxMessageCountRequest currentMaxMessageCount = fromJson(
				getMaxMessageCount(channelName).andReturn().getResponse().getContentAsString(), MaxMessageCountRequest.class);

		final MaxMessageCountRequest maxMessageCountRequest = new MaxMessageCountRequest();
		final int maxMessageCount = getRandomRangeInt(1, 10) + currentMaxMessageCount.getMaxMessageCount();
		maxMessageCountRequest.setMaxMessageCount(maxMessageCount);

		this.mockMvc.perform(put("/v1/channel-config/{channelName}/max-message-count", channelName)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(maxMessageCountRequest)))
				.andExpect(status().isAccepted())
				.andDo(document("update-max-message-count", preprocessRequest(prettyPrint())));

		getMaxMessageCount(channelName)
				.andExpect(jsonPath("$.maxMessageCount", is(maxMessageCount)))
				.andDo(document("get-max-message-count", preprocessRequest(prettyPrint())));
	}

	private ResultActions getMaxMessageCount(final String channelName) throws Exception {
		return this.mockMvc.perform(get("/v1/channel-config/{channelName}/max-message-count", channelName)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateAbsoluteMaxBytes() throws Exception {
		final String channelName = getChannelName();

		final AbsoluteMaxBytesRequest currentAbsMaxBytes = fromJson(
				getAbsoluteMaxBytes(channelName).andReturn().getResponse().getContentAsString(), AbsoluteMaxBytesRequest.class);

		final AbsoluteMaxBytesRequest absoluteMaxBytesRequest = new AbsoluteMaxBytesRequest();
		final int absoluteMaxBytes = getRandomRangeInt(8192, 10240) + currentAbsMaxBytes.getAbsoluteMaxBytes();
		absoluteMaxBytesRequest.setAbsoluteMaxBytes(absoluteMaxBytes);

		this.mockMvc.perform(put("/v1/channel-config/{channelName}/absolute-max-bytes", channelName)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(absoluteMaxBytesRequest)))
				.andExpect(status().isAccepted())
				.andDo(document("update-absolute-max-bytes", preprocessRequest(prettyPrint())));

		getAbsoluteMaxBytes(channelName)
				.andExpect(jsonPath("$.absoluteMaxBytes", is(absoluteMaxBytes)))
				.andDo(document("get-absolute-max-bytes", preprocessRequest(prettyPrint())));
	}

	private ResultActions getAbsoluteMaxBytes(final String channelName) throws Exception {
		return this.mockMvc.perform(get("/v1/channel-config/{channelName}/absolute-max-bytes", channelName)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateConfig_AddNewOrg() throws Exception {
		final String orgName = "partners-org" + getRandomRangeInt(1, 10000);
		final OrgConfigRequest orgConfigRequest = new OrgConfigRequest();
		orgConfigRequest.setNewOrgName(orgName);
		orgConfigRequest.setOrgConfig(this.newOrgMetaData.replaceAll("partners-org", orgName));
		this.mockMvc.perform(put("/v1/channel-config/{channelName}/org", getChannelName())
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(orgConfigRequest)))
				.andExpect(status().isAccepted())
				.andDo(document("update-channel-add-new-org", preprocessRequest(prettyPrint())));
	}

	private int getRandomRangeInt(final int min, final int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	private String getChannelName() {
		return this.orgProperties.getTargetTestChannel(TestTarget.CHANNEL);
	}

	//@formatter:off
	private final String newOrgMetaData =
			"{\"mod_policy\":\"Admins\","
			+ "            \"policies\":{\n"
			+ "              \"Admins\":{\n"
			+ "                \"mod_policy\":\"Admins\",\n"
			+ "                \"policy\":{\n"
			+ "                  \"type\":1,\n"
			+ "                  \"value\":{\n"
			+ "                    \"identities\":[\n"
			+ "                      {\n"
			+ "                        \"principal\":{\n"
			+ "                          \"msp_identifier\":\"partners-orgMSP\",\n"
			+ "                          \"role\":\"ADMIN\"\n"
			+ "                        },\n"
			+ "                        \"principal_classification\":\"ROLE\"\n"
			+ "                      }\n"
			+ "                    ],\n"
			+ "                    \"rule\":{\n"
			+ "                      \"n_out_of\":{\n"
			+ "                        \"n\":1,\n"
			+ "                        \"rules\":[\n"
			+ "                          {\n"
			+ "                            \"signed_by\":0\n"
			+ "                          }\n"
			+ "                        ]\n"
			+ "                      }\n"
			+ "                    },\n"
			+ "                    \"version\":0\n"
			+ "                  }\n"
			+ "                },\n"
			+ "                \"version\":\"0\"\n"
			+ "              },\n"
			+ "              \"Readers\":{\n"
			+ "                \"mod_policy\":\"Admins\",\n"
			+ "                \"policy\":{\n"
			+ "                  \"type\":1,\n"
			+ "                  \"value\":{\n"
			+ "                    \"identities\":[\n"
			+ "                      {\n"
			+ "                        \"principal\":{\n"
			+ "                          \"msp_identifier\":\"partners-orgMSP\",\n"
			+ "                          \"role\":\"MEMBER\"\n"
			+ "                        },\n"
			+ "                        \"principal_classification\":\"ROLE\"\n"
			+ "                      }\n"
			+ "                    ],\n"
			+ "                    \"rule\":{\n"
			+ "                      \"n_out_of\":{\n"
			+ "                        \"n\":1,\n"
			+ "                        \"rules\":[\n"
			+ "                          {\n"
			+ "                            \"signed_by\":0\n"
			+ "                          }\n"
			+ "                        ]\n"
			+ "                      }\n"
			+ "                    },\n"
			+ "                    \"version\":0\n"
			+ "                  }\n"
			+ "                },\n"
			+ "                \"version\":\"0\"\n"
			+ "              },\n"
			+ "              \"Writers\":{\n"
			+ "                \"mod_policy\":\"Admins\",\n"
			+ "                \"policy\":{\n"
			+ "                  \"type\":1,\n"
			+ "                  \"value\":{\n"
			+ "                    \"identities\":[\n"
			+ "                      {\n"
			+ "                        \"principal\":{\n"
			+ "                          \"msp_identifier\":\"partners-orgMSP\",\n"
			+ "                          \"role\":\"MEMBER\"\n"
			+ "                        },\n"
			+ "                        \"principal_classification\":\"ROLE\"\n"
			+ "                      }\n"
			+ "                    ],\n"
			+ "                    \"rule\":{\n"
			+ "                      \"n_out_of\":{\n"
			+ "                        \"n\":1,\n"
			+ "                        \"rules\":[\n"
			+ "                          {\n"
			+ "                            \"signed_by\":0\n"
			+ "                          }\n"
			+ "                        ]\n"
			+ "                      }\n"
			+ "                    },\n"
			+ "                    \"version\":0\n"
			+ "                  }\n"
			+ "                },\n"
			+ "                \"version\":\"0\"\n"
			+ "              }\n"
			+ "            },\n"
			+ "            \"values\":{\n"
			+ "              \"MSP\":{\n"
			+ "                \"mod_policy\":\"Admins\",\n"
			+ "                \"value\":{\n"
			+ "                  \"config\":{\n"
			+ "                    \"admins\":[\n"
			+ "                      \"LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUM4VENDQXBpZ0F3SUJBZ0lVUnNCT3J3QUlySkFhYmJlYXYrMkV1b3h5dVZVd0NnWUlLb1pJemowRUF3SXcKYmpFTE1Ba0dBMVVFQmhNQ1ZWTXhGekFWQmdOVkJBZ1REazV2Y25Sb0lFTmhjbTlzYVc1aE1SUXdFZ1lEVlFRSwpFd3RJZVhCbGNteGxaR2RsY2pFUE1BMEdBMVVFQ3hNR1kyeHBaVzUwTVI4d0hRWURWUVFERXhaeVkyRXRjR0Z5CmRHNWxjbk10YjNKbkxXRmtiV2x1TUI0WERURTRNVEF4T0RFeE1ERXdNRm9YRFRFNU1UQXhPREV4TURZd01Gb3cKZHpFTE1Ba0dBMVVFQmhNQ1ZWTXhGekFWQmdOVkJBZ1REazV2Y25Sb0lFTmhjbTlzYVc1aE1SUXdFZ1lEVlFRSwpFd3RJZVhCbGNteGxaR2RsY2pFY01BMEdBMVVFQ3hNR1kyeHBaVzUwTUFzR0ExVUVDeE1FYjNKbk1URWJNQmtHCkExVUVBeE1TWVdSdGFXNHRjR0Z5ZEc1bGNuTXRiM0puTUZrd0V3WUhLb1pJemowQ0FRWUlLb1pJemowREFRY0QKUWdBRThwK2xIVExJQlM5a1FLU1pKdmkwSmZoY3FmbEdYeXlqemF6eXJpZXErek1ObUpSVnhyZDI5ZXgzdEJ1RgpaRldabFRGWDlMc0VZVlNUQlh3dWRHMWRRS09DQVFrd2dnRUZNQTRHQTFVZER3RUIvd1FFQXdJSGdEQU1CZ05WCkhSTUJBZjhFQWpBQU1CMEdBMVVkRGdRV0JCVFNKRzBTRW93TmoxSGxyYm9DV3ozSGZlQ2dpVEFmQmdOVkhTTUUKR0RBV2dCUkhLdjQ0MXRnV2xELy9pcVlFaVRibWJTUXJRekFYQmdOVkhSRUVFREFPZ2d3d1lqRXlZalEwTURFdwpOVGt3Z1lzR0NDb0RCQVVHQndnQkJIOTdJbUYwZEhKeklqcDdJbUZpWVdNdWFXNXBkQ0k2SW5SeWRXVWlMQ0poClpHMXBiaUk2SW5SeWRXVWlMQ0pvWmk1QlptWnBiR2xoZEdsdmJpSTZJbTl5WnpFaUxDSm9aaTVGYm5KdmJHeHQKWlc1MFNVUWlPaUpoWkcxcGJpMXdZWEowYm1WeWN5MXZjbWNpTENKb1ppNVVlWEJsSWpvaVkyeHBaVzUwSW4xOQpNQW9HQ0NxR1NNNDlCQU1DQTBjQU1FUUNJRDFielpQaEVPbWZ3aUZzZXRNbjBXaGl0TkN5S1AvY3RidFY1aU5pCk5DQkxBaUJpcytlV2lXTncwcVJXbWpUc2pXSTFnRkM1NGpPaElmVVJ4Vkswblh0anp3PT0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=\"\n"
			+ "                    ],\n"
			+ "                    \"crypto_config\":{\n"
			+ "                      \"identity_identifier_hash_function\":\"SHA256\",\n"
			+ "                      \"signature_hash_family\":\"SHA2\"\n"
			+ "                    },\n"
			+ "                    \"intermediate_certs\":[\n"
			+ "                      \"LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUNQakNDQWVTZ0F3SUJBZ0lVS3R0S2FSQ2lNcytrMFEzd3NWRkF6enZhemdRd0NnWUlLb1pJemowRUF3SXcKYURFTE1Ba0dBMVVFQmhNQ1ZWTXhGekFWQmdOVkJBZ1REazV2Y25Sb0lFTmhjbTlzYVc1aE1SUXdFZ1lEVlFRSwpFd3RJZVhCbGNteGxaR2RsY2pFUE1BMEdBMVVFQ3hNR1JtRmljbWxqTVJrd0Z3WURWUVFERXhCeVkyRXRjR0Z5CmRHNWxjbk10YjNKbk1CNFhEVEU0TVRBeE9ERXhNREV3TUZvWERUSXpNVEF4TnpFeE1EWXdNRm93YmpFTE1Ba0cKQTFVRUJoTUNWVk14RnpBVkJnTlZCQWdURGs1dmNuUm9JRU5oY205c2FXNWhNUlF3RWdZRFZRUUtFd3RJZVhCbApjbXhsWkdkbGNqRVBNQTBHQTFVRUN4TUdZMnhwWlc1ME1SOHdIUVlEVlFRREV4WnlZMkV0Y0dGeWRHNWxjbk10CmIzSm5MV0ZrYldsdU1Ga3dFd1lIS29aSXpqMENBUVlJS29aSXpqMERBUWNEUWdBRXhEamhwdXpJRXJGSWJRaGwKR3BySjZWenF2bVNZb0JuNzUrQmZRY2NyTFpkN3FnTk5tOEQ1bnY4VzVnYXRwcTc1d2ZVcnd2NURScFhmSGtvUwpKc2V3YktObU1HUXdEZ1lEVlIwUEFRSC9CQVFEQWdFR01CSUdBMVVkRXdFQi93UUlNQVlCQWY4Q0FRQXdIUVlEClZSME9CQllFRkVjcS9qalcyQmFVUC8rS3BnU0pOdVp0SkN0RE1COEdBMVVkSXdRWU1CYUFGQWY1WHpFRS8zUHEKU25TL1VKWFFqVksyRkNkZU1Bb0dDQ3FHU000OUJBTUNBMGdBTUVVQ0lRRFVLM1hQaml1cEhTaGJjcUxLb3pMbgpHRkJGdGQ0UTFBaENLSktHVXkrVmx3SWdLMVhsbU41OWJ6SkdyaTJ0WnNBSjVQQ3NuczFtZWRvT2tJMlJ4OTJtCjdEQT0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=\"\n"
			+ "                    ],\n"
			+ "                    \"name\":\"partners-orgMSP\",\n"
			+ "                    \"root_certs\":[\n"
			+ "                      \"LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUNGakNDQWIyZ0F3SUJBZ0lVVVBTcXlUREJWL3RxN3lPSlRWRUx1enpyVEk0d0NnWUlLb1pJemowRUF3SXcKYURFTE1Ba0dBMVVFQmhNQ1ZWTXhGekFWQmdOVkJBZ1REazV2Y25Sb0lFTmhjbTlzYVc1aE1SUXdFZ1lEVlFRSwpFd3RJZVhCbGNteGxaR2RsY2pFUE1BMEdBMVVFQ3hNR1JtRmljbWxqTVJrd0Z3WURWUVFERXhCeVkyRXRjR0Z5CmRHNWxjbk10YjNKbk1CNFhEVEU0TVRBeE9ERXhNREV3TUZvWERUTXpNVEF4TkRFeE1ERXdNRm93YURFTE1Ba0cKQTFVRUJoTUNWVk14RnpBVkJnTlZCQWdURGs1dmNuUm9JRU5oY205c2FXNWhNUlF3RWdZRFZRUUtFd3RJZVhCbApjbXhsWkdkbGNqRVBNQTBHQTFVRUN4TUdSbUZpY21sak1Sa3dGd1lEVlFRREV4QnlZMkV0Y0dGeWRHNWxjbk10CmIzSm5NRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUVhMVl3UTVyc0k2MGpZL01yK3pqS2pKdjMKNEowNTd0UmJqc1FYbXd1OVdreCtBeDFWaTc4WERSTmNteGRUZjdBZVVrT1pXWVlNNHJ5VEVwaDFlVmc2ODZORgpNRU13RGdZRFZSMFBBUUgvQkFRREFnRUdNQklHQTFVZEV3RUIvd1FJTUFZQkFmOENBUUV3SFFZRFZSME9CQllFCkZBZjVYekVFLzNQcVNuUy9VSlhRalZLMkZDZGVNQW9HQ0NxR1NNNDlCQU1DQTBjQU1FUUNJQWRrSk9vQmFtTHAKSytRM0l0VU15KzAwS0sveDBoTHpMSDJFcHUvNnBFUG5BaUJuSVIzMDFCcHRPcXp6aVkzSjVoRU1ZYUVPbVVmMgo4UmlwQ3NVZ3ZYczlnZz09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K\"\n"
			+ "                    ],\n"
			+ "                    \"tls_intermediate_certs\":[\n"
			+ "                      \"LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUNQakNDQWVTZ0F3SUJBZ0lVS3R0S2FSQ2lNcytrMFEzd3NWRkF6enZhemdRd0NnWUlLb1pJemowRUF3SXcKYURFTE1Ba0dBMVVFQmhNQ1ZWTXhGekFWQmdOVkJBZ1REazV2Y25Sb0lFTmhjbTlzYVc1aE1SUXdFZ1lEVlFRSwpFd3RJZVhCbGNteGxaR2RsY2pFUE1BMEdBMVVFQ3hNR1JtRmljbWxqTVJrd0Z3WURWUVFERXhCeVkyRXRjR0Z5CmRHNWxjbk10YjNKbk1CNFhEVEU0TVRBeE9ERXhNREV3TUZvWERUSXpNVEF4TnpFeE1EWXdNRm93YmpFTE1Ba0cKQTFVRUJoTUNWVk14RnpBVkJnTlZCQWdURGs1dmNuUm9JRU5oY205c2FXNWhNUlF3RWdZRFZRUUtFd3RJZVhCbApjbXhsWkdkbGNqRVBNQTBHQTFVRUN4TUdZMnhwWlc1ME1SOHdIUVlEVlFRREV4WnlZMkV0Y0dGeWRHNWxjbk10CmIzSm5MV0ZrYldsdU1Ga3dFd1lIS29aSXpqMENBUVlJS29aSXpqMERBUWNEUWdBRXhEamhwdXpJRXJGSWJRaGwKR3BySjZWenF2bVNZb0JuNzUrQmZRY2NyTFpkN3FnTk5tOEQ1bnY4VzVnYXRwcTc1d2ZVcnd2NURScFhmSGtvUwpKc2V3YktObU1HUXdEZ1lEVlIwUEFRSC9CQVFEQWdFR01CSUdBMVVkRXdFQi93UUlNQVlCQWY4Q0FRQXdIUVlEClZSME9CQllFRkVjcS9qalcyQmFVUC8rS3BnU0pOdVp0SkN0RE1COEdBMVVkSXdRWU1CYUFGQWY1WHpFRS8zUHEKU25TL1VKWFFqVksyRkNkZU1Bb0dDQ3FHU000OUJBTUNBMGdBTUVVQ0lRRFVLM1hQaml1cEhTaGJjcUxLb3pMbgpHRkJGdGQ0UTFBaENLSktHVXkrVmx3SWdLMVhsbU41OWJ6SkdyaTJ0WnNBSjVQQ3NuczFtZWRvT2tJMlJ4OTJtCjdEQT0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=\"\n"
			+ "                    ],\n"
			+ "                    \"tls_root_certs\":[\n"
			+ "                      \"LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUNGakNDQWIyZ0F3SUJBZ0lVVVBTcXlUREJWL3RxN3lPSlRWRUx1enpyVEk0d0NnWUlLb1pJemowRUF3SXcKYURFTE1Ba0dBMVVFQmhNQ1ZWTXhGekFWQmdOVkJBZ1REazV2Y25Sb0lFTmhjbTlzYVc1aE1SUXdFZ1lEVlFRSwpFd3RJZVhCbGNteGxaR2RsY2pFUE1BMEdBMVVFQ3hNR1JtRmljbWxqTVJrd0Z3WURWUVFERXhCeVkyRXRjR0Z5CmRHNWxjbk10YjNKbk1CNFhEVEU0TVRBeE9ERXhNREV3TUZvWERUTXpNVEF4TkRFeE1ERXdNRm93YURFTE1Ba0cKQTFVRUJoTUNWVk14RnpBVkJnTlZCQWdURGs1dmNuUm9JRU5oY205c2FXNWhNUlF3RWdZRFZRUUtFd3RJZVhCbApjbXhsWkdkbGNqRVBNQTBHQTFVRUN4TUdSbUZpY21sak1Sa3dGd1lEVlFRREV4QnlZMkV0Y0dGeWRHNWxjbk10CmIzSm5NRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUVhMVl3UTVyc0k2MGpZL01yK3pqS2pKdjMKNEowNTd0UmJqc1FYbXd1OVdreCtBeDFWaTc4WERSTmNteGRUZjdBZVVrT1pXWVlNNHJ5VEVwaDFlVmc2ODZORgpNRU13RGdZRFZSMFBBUUgvQkFRREFnRUdNQklHQTFVZEV3RUIvd1FJTUFZQkFmOENBUUV3SFFZRFZSME9CQllFCkZBZjVYekVFLzNQcVNuUy9VSlhRalZLMkZDZGVNQW9HQ0NxR1NNNDlCQU1DQTBjQU1FUUNJQWRrSk9vQmFtTHAKSytRM0l0VU15KzAwS0sveDBoTHpMSDJFcHUvNnBFUG5BaUJuSVIzMDFCcHRPcXp6aVkzSjVoRU1ZYUVPbVVmMgo4UmlwQ3NVZ3ZYczlnZz09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K\"\n"
			+ "                    ]\n"
			+ "                  },\n"
			+ "                  \"type\":0\n"
			+ "                },\n"
			+ "                \"version\":\"0\"\n"
			+ "              }\n"
			+ "            },\n"
			+ "            \"version\":\"0\"\n"
			+ "          }";

}