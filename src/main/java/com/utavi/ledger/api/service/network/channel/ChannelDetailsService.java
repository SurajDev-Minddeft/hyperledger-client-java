package com.utavi.ledger.api.service.network.channel;

import com.google.common.collect.Sets;
import com.utavi.ledger.api.dto.chaincode.SaveChaincodeDeploymentInfoDto;
import com.utavi.ledger.api.model.*;
import com.utavi.ledger.api.model.chaincode.ExternalChaincodeInfo;
import com.utavi.ledger.api.model.enums.ChaincodeUpgradeStatus;
import com.utavi.ledger.api.model.enums.DeployType;
import com.utavi.ledger.api.repository.ChaincodeInfoRepository;
import com.utavi.ledger.api.repository.ChaincodeUpgradeInfoRepository;
import com.utavi.ledger.api.repository.ChannelDetailsRepository;
import com.utavi.ledger.api.repository.ExternalChaincodeInfoRepository;
import com.utavi.ledger.api.service.manage.OrdererConfigManager;
import com.utavi.ledger.api.service.manage.PeerConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.utavi.ledger.api.util.UtilFunctions.notFoundBuilder;

@Service
public class ChannelDetailsService {

	private final static Logger LOGGER = LoggerFactory.getLogger(ChannelDetailsService.class.getName());

	private final ChannelDetailsRepository channelRepository;
	private final ChaincodeInfoRepository chaincodeInfoRepository;
	private final ExternalChaincodeInfoRepository externalChaincodeInfoRepository;
	private final PeerConfigManager peerConfigManager;
	private final OrdererConfigManager ordererConfigManager;
	private final ChaincodeUpgradeInfoRepository chaincodeUpgradeInfoRepository;
	private final HFInitializeChannelService HFInitializeChannelService;
	public ChannelDetailsService(final ChannelDetailsRepository channelRepository,
								 final PeerConfigManager peerConfigManager,
								 final OrdererConfigManager ordererConfigManager,
								 final ChaincodeUpgradeInfoRepository chaincodeUpgradeInfoRepository,
								 final ChaincodeInfoRepository chaincodeInfoRepository,
								 final ExternalChaincodeInfoRepository externalChaincodeInfoRepository,
								 final HFInitializeChannelService HFInitializeChannelService) {
		this.channelRepository = channelRepository;
		this.peerConfigManager = peerConfigManager;
		this.ordererConfigManager = ordererConfigManager;
		this.chaincodeUpgradeInfoRepository = chaincodeUpgradeInfoRepository;
		this.chaincodeInfoRepository = chaincodeInfoRepository;
		this.externalChaincodeInfoRepository = externalChaincodeInfoRepository;
		this.HFInitializeChannelService = HFInitializeChannelService;
	}

	@Transactional(readOnly = true)
	public ChannelDetails findByName(final String name) {
		return this.channelRepository.findByName(name).orElseThrow(notFoundBuilder.apply("ChannelDetails", "name", name));
	}

	@Transactional(readOnly = true)
	public Optional<ChannelDetails> findBySiloName(final String siloName) {
		return this.channelRepository.findBySiloName(siloName);
	}

	@Transactional(readOnly = true)
	public List<ChannelDetails> findAll() {
		return this.channelRepository.findAll();
	}

	@Transactional
	public void createAndSave(final SaveChaincodeDeploymentInfoDto saveChaincodeDeploymentInfoDto) {
		List<ChaincodeUpgradeLog> existingChaincodeUpgradeLogs = this.chaincodeUpgradeInfoRepository.findByNameAndDeployVersionAndChannelNameInStatusComplete(
			saveChaincodeDeploymentInfoDto.getChaincodeMetaInfoDto().getName(),
			saveChaincodeDeploymentInfoDto.getChaincodeMetaInfoDto().getVersion(),
			saveChaincodeDeploymentInfoDto.getChannelName());
		LOGGER.info("Received a request to write channel details {}", saveChaincodeDeploymentInfoDto.getChannelName());
		ChannelDetails channelDetails = this.channelRepository
			.findByName(saveChaincodeDeploymentInfoDto.getChannelName())
			.orElse(buildNewChannelDetails(saveChaincodeDeploymentInfoDto));


		Set<PeerInfo> peerInfos = peerConfigManager.findAllByName(new HashSet<>(saveChaincodeDeploymentInfoDto.getPeerNames()));
		channelDetails.addPeerDetails(peerInfos);

		Set<OrdererInfo> ordererInfos = ordererConfigManager.findAllByName(Sets.newHashSet(saveChaincodeDeploymentInfoDto.getChannelOrdererNames()));
		channelDetails.addOrdererDetails(ordererInfos);

		ChaincodeInfo chaincodeInfo = chaincodeInfoRepository.findChaincodeInfoByNameAndVersion(
				saveChaincodeDeploymentInfoDto.getChaincodeMetaInfoDto().getName(),
				saveChaincodeDeploymentInfoDto.getChaincodeMetaInfoDto().getVersion())
			.orElse(new ChaincodeInfo(saveChaincodeDeploymentInfoDto.getChaincodeMetaInfoDto()));

		ExternalChaincodeInfo externalChaincodeInfo = this.externalChaincodeInfoRepository.findByPackageId(
			saveChaincodeDeploymentInfoDto.getPackageId())
		.orElse(new ExternalChaincodeInfo(
			chaincodeInfo,
			saveChaincodeDeploymentInfoDto.getPackageId(),
			saveChaincodeDeploymentInfoDto.getExternalChaincodeName()
		));


		List<ChaincodeUpgradeLog> chaincodeUpgradeLogs =
			peerInfos
			.stream()
			.map(peerInfo -> {
				final ChaincodeUpgradeLog newChaincodeUpgradeLog = new ChaincodeUpgradeLog();
				newChaincodeUpgradeLog.setExternalChaincodeInfo(externalChaincodeInfo);
				newChaincodeUpgradeLog.setDeployType(DeployType.INSTANTIATE);
				newChaincodeUpgradeLog.setDeployVersion(saveChaincodeDeploymentInfoDto.getChaincodeMetaInfoDto().getVersion());
				newChaincodeUpgradeLog.setUpgradeStatus(ChaincodeUpgradeStatus.COMPLETED);
				newChaincodeUpgradeLog.setChannelDetails(channelDetails);
				newChaincodeUpgradeLog.setPeerInfo(peerInfo);

				return existingChaincodeUpgradeLogs
					.stream()
					.filter(chaincodeUpgradeLog -> chaincodeUpgradeLog.getPeerInfo().getName().equals(peerInfo.getName()))
					.findAny()
					.map(chaincodeUpgradeLog -> {
						chaincodeUpgradeLog.setExternalChaincodeInfo(externalChaincodeInfo);
						return chaincodeUpgradeLog;
					})
					.orElse(newChaincodeUpgradeLog);
			})
			.collect(Collectors.toList());

		this.chaincodeInfoRepository.save(chaincodeInfo);
		this.channelRepository.save(channelDetails);
		this.externalChaincodeInfoRepository.save(externalChaincodeInfo);
		this.chaincodeUpgradeInfoRepository.saveAll(chaincodeUpgradeLogs);
		this.HFInitializeChannelService.initializeChannel(channelDetails);
	}

	private ChannelDetails buildNewChannelDetails(final SaveChaincodeDeploymentInfoDto saveChaincodeDeploymentInfoDto) {
		final ChannelDetails channelDetails = new ChannelDetails();
		channelDetails.setName(saveChaincodeDeploymentInfoDto.getChannelName());
		channelDetails.setApiKey(UUID.randomUUID().toString());
		final Set<PeerInfo> peerDetails = this.peerConfigManager.findAll();
		final Set<OrdererInfo> orderers = this.ordererConfigManager.findAll();
		channelDetails.addPeerDetails(peerDetails);
		channelDetails.addOrdererDetails(orderers);
		return channelDetails;
	}

}
