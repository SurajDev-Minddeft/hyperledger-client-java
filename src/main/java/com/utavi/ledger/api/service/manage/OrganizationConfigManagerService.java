package com.utavi.ledger.api.service.manage;

import com.utavi.ledger.api.model.*;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.repository.OrgInfoRepository;
import com.utavi.ledger.api.service.client.AccountService;
import com.utavi.ledger.api.service.client.CAServerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.utavi.ledger.api.util.UtilFunctions.notFoundBuilder;

@Service
public class OrganizationConfigManagerService {

	private final static Logger LOGGER = LoggerFactory.getLogger(OrganizationConfigManagerService.class.getName());

	private final OrgInfoRepository orgInfoRepository;
	private final CAServerInfoService caServerInfoService;
	private final Map<OrgType, NetworkNodeConfigManager<? extends BaseOrganizationComponent>> orgTypeToManager;
	private final AccountService accountService;
	public OrganizationConfigManagerService(final OrgInfoRepository orgInfoRepository,
											@Qualifier("peerConfigManager") final NetworkNodeConfigManager<PeerInfo> peerConfigManager,
											@Qualifier("ordererConfigManager") final NetworkNodeConfigManager<OrdererInfo> ordererConfigManager,
											final CAServerInfoService caServerInfoService,
											final AccountService accountService) {
		this.orgInfoRepository = orgInfoRepository;
		this.orgTypeToManager = new HashMap<>();
		this.orgTypeToManager.put(OrgType.PEER, peerConfigManager);
		this.orgTypeToManager.put(OrgType.ORDERER, ordererConfigManager);
		this.caServerInfoService = caServerInfoService;
		this.accountService = accountService;
	}

	@Transactional
	public void writeOrgConfig(List<com.utavi.ledger.config.network.OrganizationConfigDto> organizationConfigDtos) {
		organizationConfigDtos.forEach(organizationConfigDto -> {
			final String orgName = organizationConfigDto.getName();
			if (checkIfOrgWithGivenNameExists(orgName)) {
				throw new RuntimeException(String.format("Organization with name %s already exist.", orgName));
			}
			saveOrgInfo(organizationConfigDto.getOrgType(), organizationConfigDto);
			OrgInfo savedOrgInfo = this.orgInfoRepository.findByType(organizationConfigDto.getOrgType()).get();
			saveNodes(organizationConfigDto.getOrgType(), organizationConfigDto, savedOrgInfo);
			saveCaNodes(organizationConfigDto, savedOrgInfo);
			this.accountService.registerAdmin(
				organizationConfigDto.getOrgType(),
				organizationConfigDto.getBootstrapUser().getName(),
				organizationConfigDto.getMspId(),
				organizationConfigDto.getBootstrapUser().getPrivateKey(),
				organizationConfigDto.getBootstrapUser().getPemCertificate());
			LOGGER.info("Added organization information, org name is {}, orgType={}", savedOrgInfo.getName(), organizationConfigDto.getOrgType());
		});
	}

	private void saveCaNodes(com.utavi.ledger.config.network.OrganizationConfigDto organizationConfigDto, OrgInfo savedOrgInfo) {
		Set<CAServerInfo> caServerInfos = organizationConfigDto
			.getCaNodes()
			.stream()
			.map(caNode -> this.caServerInfoService.createServerInfo(
				savedOrgInfo,
				caNode.getName(),
				caNode.getLocation(),
				organizationConfigDto.getCaTlsPemCertificate()))
			.collect(Collectors.toSet());
		this.caServerInfoService.save(caServerInfos);
	}

	private void saveNodes(OrgType orgType, com.utavi.ledger.config.network.OrganizationConfigDto organizationConfigDto, OrgInfo savedOrgInfo) {
		NetworkNodeConfigManager<? extends BaseOrganizationComponent> networkNodeConfigManager = this.orgTypeToManager.get(orgType);
		Set<NetworkNodeConfigManager.NodeConfigWrapper> nodeConfigWrappers = networkNodeConfigManager.mapNetworkNodeConfiguration(
			savedOrgInfo, organizationConfigDto.getNodes(), organizationConfigDto.getTlsPemCertificate());
		networkNodeConfigManager.save(nodeConfigWrappers);
	}

	private void saveOrgInfo(OrgType orgType, com.utavi.ledger.config.network.OrganizationConfigDto organizationConfigDto) {
		final OrgInfo orgInfo = new OrgInfo(
			organizationConfigDto.getName(),
			organizationConfigDto.getMspId(),
			"",
			organizationConfigDto.getCaNodes().size(),
			organizationConfigDto.getNodes().size(),
			true,
			orgType);
		this.orgInfoRepository.saveAndFlush(orgInfo);
	}

	private boolean checkIfOrgWithGivenNameExists(final String orgName) {
		return this.orgInfoRepository.findByName(orgName).isPresent();
	}

	public List<OrgInfo> findAll() {
		return this.orgInfoRepository.findAll();
	}

	public OrgInfo find(final String name) {
		return this.orgInfoRepository.findByName(name).orElseThrow(notFoundBuilder.apply("OrgInfo", "name", name));
	}

	public OrgInfo find(final OrgType orgType) {
		return this.orgInfoRepository.findByType(orgType).orElseThrow(notFoundBuilder.apply("OrgInfo", "orgType", orgType));
	}

	public OrgInfo findPeerOrg() {
		return find(OrgType.PEER);
	}

	public OrgInfo findOrdererOrg() {
		return find(OrgType.ORDERER);
	}
}
