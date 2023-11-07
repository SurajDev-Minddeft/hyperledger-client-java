package com.utavi.ledger;

import static com.utavi.ledger.Utils.createJoinSiloDto;
import static com.utavi.ledger.api.util.Utils.createAccountName;
import static com.utavi.ledger.api.util.Utils.fromJson;
import static com.utavi.ledger.api.util.Utils.toJson;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.utavi.ledger.api.OrganizationProperties;
import com.utavi.ledger.api.OrganizationProperties.TestTarget;
import com.utavi.ledger.config.CompleteBlockchainSetupRunner;
import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.dto.request.balance.get.GetWalletEntryRequest;
import com.utavi.ledger.api.dto.request.balance.get.GetWalletEntryRequestPayload;
import com.utavi.ledger.api.dto.request.balance.getMany.GetWalletsEntriesRequest;
import com.utavi.ledger.api.dto.request.balance.getMany.GetWalletsEntriesRequestPayload;
import com.utavi.ledger.api.dto.request.balance.move.MoveBalancePayload;
import com.utavi.ledger.api.dto.request.balance.move.MoveBalanceRequest;
import com.utavi.ledger.api.dto.request.balance.move.MoveBalanceRequest.TxType;
import com.utavi.ledger.api.dto.request.credits.CreateCreditsPayload;
import com.utavi.ledger.api.dto.request.credits.CreateCreditsRequest;
import com.utavi.ledger.api.dto.response.GetWalletEntryResponsePayload;
import com.utavi.ledger.api.dto.response.GetWalletsEntriesResponsePayload;
import com.utavi.ledger.api.dto.response.WalletResponseDto;
import com.utavi.ledger.api.exceptions.ProposalExceptionInternalServerError;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.service.client.AccountService;
import com.utavi.ledger.api.service.network.ResourceServerService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets/ledger")
@Import({TestFlywayMigration.class, CompleteBlockchainSetupRunner.class})
public class LedgerIT {

//	@Autowired
//	MockMvc mockMvc;
//
//	@Autowired
//	ResourceServerService resourceServerService;
//
//	@Autowired
//	AccountService accountService;
//
//	@Autowired
//	OrganizationProperties organizationProperties;
//
//	@Autowired
//	ChannelInfoManager channelInfoManager;
//
//	@Test
//	public void testQueryAsRevokedUser() throws Exception {
//		final String accountName = createAccountName();
//		final String siloName = getSiloName();
//		createJoinAccount(accountName, AccountType.BUSINESS, siloName);
//		createCredit(accountName, "wENTRY_1", "FFF", siloName, BigDecimal.ONE);
//		validateGetEntry(accountName, "FFF", "wENTRY_1", siloName, BigDecimal.ONE);
//		revokeAccountRequest(accountName);
//
//		final GetWalletEntryRequestPayload payload = new GetWalletEntryRequestPayload();
//		payload.setAccountId(accountName);
//		payload.setCreditId("FFF");
//		payload.setWalletEntryId("wENTRY_1");
//
//		final GetWalletEntryRequest request = new GetWalletEntryRequest();
//		request.setPayload(payload);
//		request.setSiloName(siloName);
//		request.setAccountId(accountName);
//		final Exception resolvedException = this.mockMvc.perform(post("/v1/ledger/query/entry")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(request))).andReturn().getResolvedException();
//		assertTrue(resolvedException instanceof ProposalExceptionInternalServerError);
//		assertTrue(resolvedException.getMessage().contains("access denied"));
//	}
//
//	private void revokeAccountRequest(final String enrollmentId) throws Exception {
//		this.mockMvc.perform(put("/v1/account/revoke/{enrollmentId}", enrollmentId)
//				.param("orgType", OrgType.PEER.name())
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isAccepted());
//	}
//
//	@Test
//	public void testCreateCredits() throws Exception {
//		final BigDecimal amount = BigDecimal.valueOf(1000000);
//		final String accountName = createAccountName();
//		final String siloName = getSiloName();
//		final String creditId = "CREDIT" + System.currentTimeMillis();
//		final String entryId = "wENTRY_0";
//		createJoinAccount(accountName, AccountType.BUSINESS, siloName);
//		createCredit(accountName, entryId, creditId, siloName, amount).andDo(document("create-credits", preprocessRequest(prettyPrint())));
//		validateGetEntry(accountName, creditId, entryId, siloName, amount).andDo(document("get-entry", preprocessRequest(prettyPrint())));
//	}
//
//	@Test
//	public void testMoveBalance() throws Exception {
//		final String siloName = getSiloName();
//		final String creditId = "CMD" + System.currentTimeMillis();
//		final String firstEntry = "w" + UUID.randomUUID().toString();
//		final String secondEntry = "w" + UUID.randomUUID().toString();
//		final BigDecimal moveAmount = BigDecimal.valueOf(333);
//		final BigDecimal sourceAccountAmount = BigDecimal.valueOf(1333);
//
//		final String firstAccount = createAccountName();
//		createJoinAccount(firstAccount, AccountType.BUSINESS, siloName);
//		createCredit(firstAccount, firstEntry, creditId, siloName, sourceAccountAmount);
//		final String secondAccount = createAccountName();
//		createJoinAccount(secondAccount, AccountType.INVESTOR, siloName);
//
//		validateGetEntry(firstAccount, creditId, firstEntry, siloName, sourceAccountAmount);
//
//		moveBalance(firstAccount, secondAccount, firstEntry, secondEntry, creditId, siloName, moveAmount, TxType.TRANSFER)
//				.andDo(document("move-balance", preprocessRequest(prettyPrint())));
//
//		validateGetEntry(firstAccount, creditId, firstEntry, siloName, sourceAccountAmount.subtract(moveAmount));
//		validateGetEntry(secondAccount, creditId, secondEntry, siloName, moveAmount);
//	}
//
//	@Test
//	public void testGetWalletsEntries() throws Exception {
//		final BigDecimal moveAmount = BigDecimal.valueOf(333);
//		final String siloName = getSiloName();
//
//		final String firstAccount = createAccountName();
//		final String firstAccountCredit = "CMD" + System.currentTimeMillis();
//		final String firstAccountEntry = "w" + UUID.randomUUID().toString();
//		final BigDecimal firstAccountCreditAmount = BigDecimal.valueOf(1333);
//		createJoinAccount(firstAccount, AccountType.BUSINESS, siloName);
//		createCredit(firstAccount, firstAccountEntry, firstAccountCredit, siloName, firstAccountCreditAmount);
//
//		final String secondAccount = createAccountName();
//		createJoinAccount(secondAccount, AccountType.INVESTOR, siloName);
//
//		final String thirdAccount = createAccountName();
//		final String thirdAccountCredit = "JJJ" + System.currentTimeMillis();
//		final String thirdAccountEntry = "w" + UUID.randomUUID().toString();
//		final BigDecimal thirdAccountCreditAmount = firstAccountCreditAmount.add(BigDecimal.valueOf(667));
//		createJoinAccount(thirdAccount, AccountType.BUSINESS, siloName);
//		createCredit(thirdAccount, thirdAccountEntry, thirdAccountCredit, siloName, thirdAccountCreditAmount);
//
//		validateGetEntry(firstAccount, firstAccountCredit, firstAccountEntry, siloName, firstAccountCreditAmount);
//		final String secondAccountCMDEntry = "w" + UUID.randomUUID().toString();
//		moveBalance(firstAccount, secondAccount, firstAccountEntry, secondAccountCMDEntry, firstAccountCredit, siloName, moveAmount,
//				TxType.TRANSFER);
//
//		final BigDecimal firstCreditEntry2Amount = moveAmount.subtract(BigDecimal.valueOf(100));
//
//		final String secondAccountCMDEntry2 = "w" + UUID.randomUUID().toString();
//		moveBalance(firstAccount, secondAccount, firstAccountEntry, secondAccountCMDEntry2, firstAccountCredit, siloName, firstCreditEntry2Amount,
//				TxType.TRANSFER);
//
//		final BigDecimal secondCreditEntry3Amount = BigDecimal.valueOf(67);
//		final String secondAccountJJJCredit = "w" + UUID.randomUUID().toString();
//		moveBalance(thirdAccount, secondAccount, thirdAccountEntry, secondAccountJJJCredit, thirdAccountCredit, siloName, secondCreditEntry3Amount,
//				TxType.TRANSFER);
//
//		final GetWalletsEntriesRequestPayload payload = new GetWalletsEntriesRequestPayload();
//		payload.setAccountId(secondAccount);
//		final Map<String, List<String>> wallets = new HashMap<>();
//		wallets.put(firstAccountCredit, Arrays.asList(secondAccountCMDEntry2, secondAccountCMDEntry));
//		wallets.put(thirdAccountCredit, Collections.singletonList(secondAccountJJJCredit));
//		payload.setWallets(wallets);
//		final GetWalletsEntriesRequest request = new GetWalletsEntriesRequest();
//		request.setSiloName(siloName);
//		request.setAccountId(secondAccount);
//		request.setPayload(payload);
//
//		final ResultActions getEntriesActions = this.mockMvc.perform(post("/v1/ledger/query/entries")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(request)))
//				.andExpect(status().isOk());
//		getEntriesActions.andDo(document("get-wallets-entries", preprocessRequest(prettyPrint())));
//		final GetWalletsEntriesResponsePayload response = fromJson(getEntriesActions
//				.andReturn().getResponse().getContentAsString(), GetWalletsEntriesResponsePayload.class);
//
//		assertEquals(secondAccount, response.getAccountId());
//		final Map<String, List<WalletResponseDto>> balances = response.getBalances();
//		assertTrue(balances.containsKey(firstAccountCredit));
//		final List<WalletResponseDto> firstCreditWallets = balances.get(firstAccountCredit);
//		assertEquals(2, firstCreditWallets.size());
//		assertTrue(verifyWalletBalance(firstCreditWallets, secondAccountCMDEntry, moveAmount));
//		assertTrue(verifyWalletBalance(firstCreditWallets, secondAccountCMDEntry2, firstCreditEntry2Amount));
//
//		assertTrue(balances.containsKey(thirdAccountCredit));
//		final List<WalletResponseDto> secondCreditWallets = balances.get(thirdAccountCredit);
//		assertEquals(1, secondCreditWallets.size());
//		assertTrue(verifyWalletBalance(secondCreditWallets, secondAccountJJJCredit, secondCreditEntry3Amount));
//	}
//
//	private boolean verifyWalletBalance(final List<WalletResponseDto> wallets, final String entry, final BigDecimal amount) {
//		return wallets.stream().filter(wallet -> wallet.getWalletEntryId().equals(entry))
//				.anyMatch(wallet -> wallet.getAmount().equals(amount));
//	}
//
//	private void createJoinAccount(final String name, final AccountType type, final String siloId) throws Exception {
//		this.mockMvc.perform(post("/v1/account/join")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(createJoinSiloDto(siloId, name, type))))
//				.andExpect(status().isAccepted())
//				.andReturn();
//	}
//
//	private ResultActions validateGetEntry(final String accountName, final String creditId, final String walletEntryId,
//			final String siloName, final BigDecimal expectedAmount) throws Exception {
//		final GetWalletEntryRequestPayload payload = new GetWalletEntryRequestPayload();
//		payload.setAccountId(accountName);
//		payload.setCreditId(creditId);
//		payload.setWalletEntryId(walletEntryId);
//
//		final GetWalletEntryRequest request = new GetWalletEntryRequest();
//		request.setPayload(payload);
//		request.setSiloName(siloName);
//		request.setAccountId(accountName);
//
//		final ResultActions mvcResult = this.mockMvc.perform(post("/v1/ledger/query/entry")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(request)))
//				.andExpect(status().isOk());
//		final GetWalletEntryResponsePayload response = fromJson(
//				mvcResult.andReturn().getResponse().getContentAsString(), GetWalletEntryResponsePayload.class);
//
//		assertEquals(0, response.getAmount().compareTo(expectedAmount));
//		assertEquals(walletEntryId, response.getWalletEntryId());
//		assertEquals(accountName, response.getAccountId());
//		assertEquals(creditId, response.getCreditId());
//		return mvcResult;
//	}
//
//	private ResultActions createCredit(final String account, final String entryId, final String creditId, final String siloName,
//			final BigDecimal amount)
//			throws Exception {
//		final CreateCreditsPayload payload = new CreateCreditsPayload();
//		payload.setAccountId(account);
//		payload.setAmount(amount);
//		payload.setCreditId(creditId);
//		payload.setWalletEntryId(entryId);
//
//		final CreateCreditsRequest request = new CreateCreditsRequest();
//		request.setPayload(payload);
//		request.setAccountId(account);
//		request.setSiloName(siloName);
//
//		final MvcResult mvcResult = this.mockMvc.perform(post("/v1/ledger/invoke/create-credits")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(request))).andReturn();
//
//		final ResultActions createCreditsActions = this.mockMvc.perform(asyncDispatch(mvcResult));
//		validateTransactionResponse(createCreditsActions);
//		return createCreditsActions;
//	}
//
//	private ResultActions moveBalance(final String fromAcc, final String toAcc, final String fromEntry, final String toEntry,
//			final String creditId, final String siloName, final BigDecimal amount, final TxType txType) throws Exception {
//		final MoveBalancePayload moveBalancePayload = new MoveBalancePayload();
//		moveBalancePayload.setAmount(amount);
//		moveBalancePayload.setCreditId(creditId);
//		moveBalancePayload.setFromAccountId(fromAcc);
//		moveBalancePayload.setToAccountId(toAcc);
//		moveBalancePayload.setFromWalletEntry(fromEntry);
//		moveBalancePayload.setToWalletEntry(toEntry);
//
//		final MoveBalanceRequest moveBalanceRequest = new MoveBalanceRequest();
//		moveBalanceRequest.setPayload(Collections.singletonList(moveBalancePayload));
//		moveBalanceRequest.setAccountId(fromAcc);
//		moveBalanceRequest.setSiloName(siloName);
//		moveBalanceRequest.setTxType(txType);
//
//		final MvcResult mvcResult = this.mockMvc.perform(post("/v1/ledger/invoke/move-balance")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(moveBalanceRequest))).andReturn();
//
//		final ResultActions moveActions = this.mockMvc.perform(asyncDispatch(mvcResult));
//
//		validateTransactionResponse(moveActions);
//		return moveActions;
//	}
//
//	private String getSiloName() {
//		final String channel = this.organizationProperties.getTargetTestChannel(TestTarget.BLOCKCHAIN);
//		return this.channelInfoManager.find(channel).getSiloName();
//	}
//
//	private void validateTransactionResponse(final ResultActions resultActions) throws Exception {
//		resultActions
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.blockNumber", not(0)))
//				.andExpect(jsonPath("$.valid", is(Boolean.TRUE)))
//				.andExpect(jsonPath("$.transactionId", notNullValue()))
//				.andExpect(jsonPath("$.timestamp", notNullValue()))
//				.andDo(print());
//	}

}
