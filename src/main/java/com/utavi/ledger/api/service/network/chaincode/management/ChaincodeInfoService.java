package com.utavi.ledger.api.service.network.chaincode.management;

import com.utavi.ledger.api.dto.chaincode.ChaincodeMetaInfDto;
import com.utavi.ledger.api.dto.chaincode.ExternalChaincodeInfoDto;
import com.utavi.ledger.api.model.ChaincodeInfo;
import com.utavi.ledger.api.repository.ChaincodeInfoRepository;
import com.utavi.ledger.api.repository.ExternalChaincodeInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ChaincodeInfoService {

	private final static Logger LOGGER = LoggerFactory.getLogger(ChaincodeInfoService.class.getName());

	private final ChaincodeInfoRepository chaincodeInfoRepository;
	private final ExternalChaincodeInfoRepository externalChaincodeInfoRepository;

	public ChaincodeInfoService(final ChaincodeInfoRepository chaincodeInfoRepository,
								final ExternalChaincodeInfoRepository externalChaincodeInfoRepository) {
		this.chaincodeInfoRepository = chaincodeInfoRepository;
		this.externalChaincodeInfoRepository = externalChaincodeInfoRepository;
	}

	public Optional<ExternalChaincodeInfoDto> getExternalChaincodeInfo(String chaincodeName, String version) {
		return this.externalChaincodeInfoRepository.findExternalChaincodeInfoByChaincodeNameAndVersion(chaincodeName, version)
			.map(externalChaincodeInfo -> new ExternalChaincodeInfoDto(
				externalChaincodeInfo.getName(),
				externalChaincodeInfo.getChaincodeInfo().getVersion(),
				externalChaincodeInfo.getPackageId()
			));
	}

	@Transactional
	public void save(final ChaincodeMetaInfDto metaInf) {
		final String chaincodeInfoName = metaInf.getName();
		final Optional<ChaincodeInfo> optionalInfo = this.chaincodeInfoRepository.findByName(chaincodeInfoName);
		if (optionalInfo.isPresent()) {
			final ChaincodeInfo info = optionalInfo.get();
			info.setVersion(metaInf.getVersion());
			LOGGER.info("Updated version of the chaincode {} to {}", chaincodeInfoName, info.getVersion());
			this.chaincodeInfoRepository.save(info);
		} else {
			final ChaincodeInfo chaincodeInfo = new ChaincodeInfo(metaInf);
			LOGGER.info("Info for the chaincode={} not found. Writing the info to the database, version={}", chaincodeInfoName,
					chaincodeInfo.getVersion());
			this.chaincodeInfoRepository.save(chaincodeInfo);
		}
	}

	@Transactional(readOnly = true)
	public Set<ChaincodeInfo> find() {
		return new HashSet<>(this.chaincodeInfoRepository.findAll());
	}

}
