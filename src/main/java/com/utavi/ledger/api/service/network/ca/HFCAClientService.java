package com.utavi.ledger.api.service.network.ca;

import com.utavi.ledger.api.exceptions.HFClientException;
import com.utavi.ledger.api.model.CAServerInfo;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.service.client.CAServerInfoService;
import com.utavi.ledger.api.util.TlsUtils;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

@Service
@Transactional
public class HFCAClientService {

	private final static Logger LOGGER = LoggerFactory.getLogger(HFCAClientService.class.getName());

	private final CAServerInfoService caServerService;

	public HFCAClientService(final CAServerInfoService caServerService) {
		this.caServerService = caServerService;
	}

	@Cacheable(value = "caFabricClients", key = "{#orgType}")
	public HFCAClient createFabricCAClient(final OrgType orgType) {
		final CAServerInfo caServerInfo = this.caServerService.findOneByOrgType(orgType);
		final String caName = caServerInfo.getName();
		final String caLocation = caServerInfo.getLocation();
		LOGGER.info("Creating fabric CAClient = {}", caName);
		Properties caProperties = TlsUtils.makeCAServerTlsProperties(caServerInfo.getTlsCertificate().getBytes(), caName);
		try {
			final HFCAClient hfcaClient = HFCAClient.createNewInstance(caLocation, caProperties);
			hfcaClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
			LOGGER.info("Successfully created Fabric CAClient = {}", caName);
			return hfcaClient;
		} catch (final Exception e) {
			LOGGER.error("Failed to create Fabric CAClient = {}, Error:", caName, e);
			throw new HFClientException("Failed to create Fabric CAClient");
		}
	}

}
