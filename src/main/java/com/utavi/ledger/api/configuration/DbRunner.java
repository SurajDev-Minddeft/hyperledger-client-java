package com.utavi.ledger.api.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbRunner /*implements ApplicationRunner*/ {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DbRunner.class.getName());

//	private final NetworkConfigManager networkConfigManager;
//
//	public DbRunner(final NetworkConfigManager networkConfigManager) {
//		this.networkConfigManager = networkConfigManager;
//	}

//	@Override
//	public void run(final ApplicationArguments args) {
//		LOGGER.info("Running DbRunner...");
//		this.networkConfigManager.writeNetworkConfiguration();
//		LOGGER.info("DbRunner successfully finished.");
//	}
}
