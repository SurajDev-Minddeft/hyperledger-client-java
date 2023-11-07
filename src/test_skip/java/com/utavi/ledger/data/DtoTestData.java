package com.utavi.ledger.data;

import static com.utavi.ledger.data.AccountTestData.ACCOUNT_NAME;
import static com.utavi.ledger.data.OrgInfoTestData.ORGANIZATION_NAME;
import static com.utavi.ledger.data.OrgInfoTestData.ROOT_AFFILIATION;
import static com.utavi.ledger.data.PeerTestData.PEER_NAME;

import com.utavi.ledger.api.dto.CreateChannelDto;
import com.utavi.ledger.api.dto.CreateOrgInfoDto;
import com.utavi.ledger.api.dto.IdentityRegistrationRequest;
import com.utavi.ledger.api.dto.IdentityRegistrationRequest.Builder;
import com.utavi.ledger.api.dto.SetChannelPeersDto;
import com.utavi.ledger.api.dto.JoinSiloDto;
import com.utavi.ledger.api.dto.ResponseOrganizationDto;
import com.utavi.ledger.api.dto.UpdateCAAttributesRequest;
import com.utavi.ledger.api.dto.chaincode.ChaincodeDeployRequest;
import com.utavi.ledger.api.dto.chaincode.ChaincodeInstallRequest;
import com.utavi.ledger.api.dto.chaincode.ChaincodeInstantiateRequest;
import com.utavi.ledger.api.dto.chaincode.ChaincodePrivateCollectionConfig;
import com.utavi.ledger.api.dto.request.balance.get.GetWalletEntryRequest;
import com.utavi.ledger.api.dto.request.balance.get.GetWalletEntryRequestPayload;
import com.utavi.ledger.api.dto.request.balance.getMany.GetWalletsEntriesRequest;
import com.utavi.ledger.api.dto.request.balance.getMany.GetWalletsEntriesRequestPayload;
import com.utavi.ledger.api.dto.request.balance.move.MoveBalancePayload;
import com.utavi.ledger.api.dto.request.balance.move.MoveBalanceRequest;
import com.utavi.ledger.api.dto.request.balance.move.MoveBalanceRequest.TxType;
import com.utavi.ledger.api.dto.request.credits.CreateCreditsPayload;
import com.utavi.ledger.api.dto.request.credits.CreateCreditsRequest;
import com.utavi.ledger.api.dto.request.silo.LeaveSiloDto;
import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.model.enums.DeployType;
import com.utavi.ledger.api.model.enums.IdentityType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

public final class DtoTestData {

	private static final String WALLET_ENTRY_ID = "WalletEntryId";
	private static final String CREDIT_ID = "CreditId";
	private static final BigDecimal AMOUNT = new BigDecimal(1);
	public static final String API_KEY = "apiKey";
	public static final String CHAINCODE_NAME = "ChaincodeName";
	public static final String VERSION = "Version";

	public static JoinSiloDto makeJoinSiloRequestDto(final Account actualUser) {
		final JoinSiloDto joinSiloDto = new JoinSiloDto();
		joinSiloDto.setSiloName("SILO_NAME");
		joinSiloDto.setAccountId(actualUser.getName());
		joinSiloDto.setAccountType(actualUser.getAccountType());
		return joinSiloDto;
	}

	public static LeaveSiloDto makeLeaveSiloRequestDto() {
		final LeaveSiloDto leaveSiloDto = new LeaveSiloDto();
		leaveSiloDto.setSiloName("SILO_NAME");
		leaveSiloDto.setAccountId(ACCOUNT_NAME);
		return leaveSiloDto;
	}

	public static CreateOrgInfoDto makeOrganizationDto() {
		final CreateOrgInfoDto createOrgInfoDto = new CreateOrgInfoDto();
		createOrgInfoDto.setName(ORGANIZATION_NAME);
		createOrgInfoDto.setMspId("main-utaviMSP");
		createOrgInfoDto.setAffiliation(ROOT_AFFILIATION);
		return createOrgInfoDto;
	}

	public static UpdateCAAttributesRequest makeUserLeaveSiloUpdateCAIdentityRequest(final Account user, final String value) {
		return new UpdateCAAttributesRequest(user.getName(), "member.of", value);
	}

	public static IdentityRegistrationRequest makeUserIdentityRegistrationRequest(final Account user) {
		return new Builder()
				.setName(user.getName())
				.setSecret(user.getEnrollmentSecret())
				.setAffiliation(user.getAffiliation())
				.setIdentityType(IdentityType.USER)
				.setAccountType(user.getAccountType())
				.build();
	}

	public static RegistrationRequest makeClientRegistrationRequest(final IdentityRegistrationRequest userRegistrationRequest) throws Exception {
		final RegistrationRequest registrationRequest = new RegistrationRequest(userRegistrationRequest.getName(),
				userRegistrationRequest.getAffiliation());
		registrationRequest.setSecret(userRegistrationRequest.getSecret());
		registrationRequest.setType(HFCAClient.HFCA_TYPE_USER);
		registrationRequest.addAttribute(new Attribute("account.type", userRegistrationRequest.getAccountType().name(), Boolean.TRUE));
		registrationRequest.addAttribute(new Attribute("member.of", userRegistrationRequest.getMembershipInfo(), Boolean.TRUE));
		registrationRequest.addAttribute(new Attribute("membership.status", "", Boolean.TRUE));
		return registrationRequest;
	}

	private static MoveBalancePayload makeMoveBalanceExternalPayload() {
		final MoveBalancePayload payload = new MoveBalancePayload();
		payload.setFromAccountId(ACCOUNT_NAME);
		payload.setToAccountId(ACCOUNT_NAME);
		payload.setAmount(AMOUNT);
		payload.setCreditId(CREDIT_ID);
		payload.setFromWalletEntry(WALLET_ENTRY_ID);
		payload.setToWalletEntry(WALLET_ENTRY_ID);
		return payload;
	}

	private static CreateCreditsPayload makeCreateCreditsExternalPayload() {
		final CreateCreditsPayload updateCreateCreditsPayload = new CreateCreditsPayload();
		updateCreateCreditsPayload.setAccountId(ACCOUNT_NAME);
		updateCreateCreditsPayload.setAmount(AMOUNT);
		updateCreateCreditsPayload.setWalletEntryId(WALLET_ENTRY_ID);
		updateCreateCreditsPayload.setCreditId(CREDIT_ID);
		return updateCreateCreditsPayload;
	}

	public static CreateCreditsRequest makeCreateCreditsRequest() {
		final CreateCreditsRequest createCreditsRequest = new CreateCreditsRequest();
		createCreditsRequest.setAccountId(ACCOUNT_NAME);
		createCreditsRequest.setSiloName("SILO_NAME");
		createCreditsRequest.setPayload(makeCreateCreditsExternalPayload());
		return createCreditsRequest;
	}

	public static MoveBalanceRequest makeMoveBalanceRequest(final TxType txType) {
		final List<MoveBalancePayload> moveBalanceExternalPayloads = new ArrayList<>();
		moveBalanceExternalPayloads.add(makeMoveBalanceExternalPayload());
		final MoveBalanceRequest moveBalanceRequest = new MoveBalanceRequest();
		moveBalanceRequest.setAccountId(ACCOUNT_NAME);
		moveBalanceRequest.setSiloName("SILO_NAME");
		moveBalanceRequest.setTxType(txType);
		moveBalanceRequest.setPayload(moveBalanceExternalPayloads);
		return moveBalanceRequest;
	}

	public static GetWalletsEntriesRequest makeGetWalletsEntriesRequest() {
		final GetWalletsEntriesRequest getWalletsEntriesRequest = new GetWalletsEntriesRequest();
		final GetWalletsEntriesRequestPayload getWalletsEntriesRequestPayload = makeGetWalletsEntriesRequestPayload();
		getWalletsEntriesRequest.setAccountId(ACCOUNT_NAME);
		getWalletsEntriesRequest.setSiloName("SILO_NAME");
		getWalletsEntriesRequest.setPayload(getWalletsEntriesRequestPayload);
		return getWalletsEntriesRequest;
	}

	private static GetWalletsEntriesRequestPayload makeGetWalletsEntriesRequestPayload() {
		final GetWalletsEntriesRequestPayload getWalletsEntriesRequestPayload = new GetWalletsEntriesRequestPayload();
		final Map<String, List<String>> wallets = new HashMap<>();
		wallets.put(CREDIT_ID, Collections.singletonList(WALLET_ENTRY_ID));
		getWalletsEntriesRequestPayload.setWallets(wallets);
		return getWalletsEntriesRequestPayload;
	}

	public static GetWalletEntryRequest makeGetWalletEntryRequest() {
		final GetWalletEntryRequest getWalletEntryRequest = new GetWalletEntryRequest();
		getWalletEntryRequest.setAccountId(ACCOUNT_NAME);
		getWalletEntryRequest.setSiloName("SILO_NAME");
		getWalletEntryRequest.setPayload(makeGetWalletEntryRequestExternalPayload());
		return getWalletEntryRequest;
	}

	private static GetWalletEntryRequestPayload makeGetWalletEntryRequestExternalPayload() {
		final GetWalletEntryRequestPayload payload = new GetWalletEntryRequestPayload();
		payload.setAccountId(ACCOUNT_NAME);
		payload.setCreditId(CREDIT_ID);
		payload.setWalletEntryId(WALLET_ENTRY_ID);
		return payload;
	}

	public static ChaincodeInstallRequest makeChaincodeInstallRequest() {
		final ChaincodeInstallRequest installRequest = new ChaincodeInstallRequest();
		installRequest.setChaincodeVersion(VERSION);
		installRequest.setChaincodeName(CHAINCODE_NAME);
		final Set<String> peerNames = new HashSet<>();
		peerNames.add("peer");
		installRequest.setPeerNames(peerNames);
		return installRequest;
	}

	public static ChaincodeInstantiateRequest makeChaincodeInstantiateRequest() {
		final ChaincodeInstantiateRequest request = new ChaincodeInstantiateRequest();
		request.setChaincodeName(CHAINCODE_NAME);
		request.setChaincodeVersion(VERSION);
		request.setChannelName("CHANNEL_NAME");
		return request;
	}

	public static ChaincodeDeployRequest makeChaincodeDeployRequest(final String orgName, final DeployType deployType) {
		final ChaincodeDeployRequest chaincodeDeployRequest = new ChaincodeDeployRequest();
		chaincodeDeployRequest.setChannelName("CHANNEL_NAME");
		final ChaincodePrivateCollectionConfig chaincodePrivateCollectionConfig = makeChaincodePrivateCollectionConfig(orgName);
		final List<ChaincodePrivateCollectionConfig> chaincodePrivateCollectionConfigs = new ArrayList<>();
		chaincodePrivateCollectionConfigs.add(chaincodePrivateCollectionConfig);
		chaincodeDeployRequest.setPrivateCollectionConfig(chaincodePrivateCollectionConfigs);
		chaincodeDeployRequest.setDeployType(deployType);
		chaincodeDeployRequest.setChaincodeName(CHAINCODE_NAME);
		chaincodeDeployRequest.setChaincodeVersion(VERSION);
		return chaincodeDeployRequest;
	}

	private static ChaincodePrivateCollectionConfig makeChaincodePrivateCollectionConfig(final String orgName) {
		final ChaincodePrivateCollectionConfig chaincodePrivateCollectionConfig = new ChaincodePrivateCollectionConfig();
		chaincodePrivateCollectionConfig.setBlockToLive(0);
		chaincodePrivateCollectionConfig.setMaximumPeerCount(3);
		chaincodePrivateCollectionConfig.setName("apiKeys");
		chaincodePrivateCollectionConfig.setPolicy("OR('" + orgName + "MSP.member')");
		chaincodePrivateCollectionConfig.setRequiredPeerCount(0);
		chaincodePrivateCollectionConfig.setSignaturePolicyEnvelope(makeSignaturePolicyEnvelope());
		return chaincodePrivateCollectionConfig;
	}

	private static ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope makeSignaturePolicyEnvelope() {
		final ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope signaturePolicyEnvelope = new ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope();
		final ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.PolicyValue policyValue = makePolicyValue();
		final List<ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.PolicyValue> policyValues = new ArrayList<>();
		policyValues.add(policyValue);
		final Map<String, List<ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.PolicyValue>> mapPolicyValue = new HashMap<>();
		mapPolicyValue.put("policyValues", policyValues);
		signaturePolicyEnvelope.setPolicy(mapPolicyValue);
		final ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.Role role = makeRole();
		final Map<String, ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.Role> mapRole = new HashMap<>();
		mapRole.put("role", role);
		final List<Map<String, ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.Role>> roles = new ArrayList<>();
		roles.add(mapRole);
		signaturePolicyEnvelope.setIdentities(roles);
		return signaturePolicyEnvelope;
	}

	private static ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.Role makeRole() {
		final ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.Role role = new ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.Role();
		role.setRole(makeRoleValue());
		return role;
	}

	private static ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.Role.RoleValue makeRoleValue() {
		final ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.Role.RoleValue roleValue = new ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.Role.RoleValue();
		roleValue.setMspId("main-utaviMSP");
		roleValue.setName("name");
		return roleValue;
	}

	private static ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.PolicyValue makePolicyValue() {
		final ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.PolicyValue policyValue = new ChaincodePrivateCollectionConfig.SignaturePolicyEnvelope.PolicyValue();
		policyValue.setSignedBy("SignedBy");
		return policyValue;
	}

	public static CreateChannelDto makeCreateChannelDto() {
		final CreateChannelDto createChannelDto = new CreateChannelDto();
		createChannelDto.setChannelName("CHANNEL_NAME");
		final Set<String> peersNames = new HashSet<>();
		peersNames.add(PEER_NAME);
//		createChannelDto.setPeers(peersNames);
		createChannelDto.setOrgName(ORGANIZATION_NAME);
		createChannelDto.setOrgType(OrgType.PEER);
		return createChannelDto;
	}

	public static SetChannelPeersDto makeJoinChannelDto() {
		final SetChannelPeersDto setChannelPeersDto = new SetChannelPeersDto();
		setChannelPeersDto.setChannelName("CHANNEL_NAME");
		final Set<String> peersNames = new HashSet<>();
		peersNames.add(PEER_NAME);
		setChannelPeersDto.setPeers(peersNames);
		return setChannelPeersDto;
	}

	public static ResponseOrganizationDto makeResponseOrganizationDto() {
		final ResponseOrganizationDto responseOrganizationDto = new ResponseOrganizationDto();
		responseOrganizationDto.setName(ORGANIZATION_NAME);
		responseOrganizationDto.setCreateDate(Instant.now());
		responseOrganizationDto.setUpdateDate(Instant.now());
		responseOrganizationDto.setMspId(ORGANIZATION_NAME + "MSP");
		responseOrganizationDto.setAffiliation(ROOT_AFFILIATION);
		responseOrganizationDto.setOrgType(OrgType.PEER.name());
		return responseOrganizationDto;
	}

}
