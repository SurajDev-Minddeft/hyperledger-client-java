package com.utavi.ledger.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestFlywayMigration {

	@Bean
	public FlywayMigrationStrategy clean() {
		return flyway -> {
			flyway.clean();
			flyway.migrate();
		};
	}

}
