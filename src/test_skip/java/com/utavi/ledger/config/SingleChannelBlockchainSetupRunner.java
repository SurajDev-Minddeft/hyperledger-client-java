package com.utavi.ledger.config;

import com.utavi.ledger.api.AppDataManager;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class SingleChannelBlockchainSetupRunner /*implements ApplicationRunner*/ {

	private final AppDataManager appDataManager;

	public SingleChannelBlockchainSetupRunner(final AppDataManager appDataManager) {
		this.appDataManager = appDataManager;
	}

//	@Override
//	public void run(final ApplicationArguments args) {
//		this.networkConfigManager.writeNetworkConfiguration();
//		this.appDataManager.bootstrapOneChannel();
//	}
}
