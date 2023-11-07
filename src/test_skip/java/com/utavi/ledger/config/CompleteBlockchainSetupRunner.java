package com.utavi.ledger.config;

import com.utavi.ledger.api.AppDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;

//@TestConfiguration
public class CompleteBlockchainSetupRunner /*implements ApplicationRunner*/ {

	private final static Logger LOGGER = LoggerFactory.getLogger(CompleteBlockchainSetupRunner.class.getName());

	private final AppDataManager appDataManager;

	public CompleteBlockchainSetupRunner(final AppDataManager appDataManager) {
		this.appDataManager = appDataManager;
	}

//	@Override
//	public void run(final ApplicationArguments args) {
//		LOGGER.info("Running BlockchainRunner...");
//		this.networkConfigManager.writeNetworkConfiguration();
//		this.appDataManager.bootstrapChannelsWithSilos();
//		LOGGER.info("BlockchainRunner successfully finished.");
//	}
}