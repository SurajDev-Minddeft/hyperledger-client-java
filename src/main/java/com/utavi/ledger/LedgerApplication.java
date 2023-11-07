package com.utavi.ledger;

import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.ChaincodeUpgradeLog;
import com.utavi.ledger.api.model.OrdererInfo;
import com.utavi.ledger.api.model.OrgInfo;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Clock;
import java.util.concurrent.Executor;

@EnableCaching
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class LedgerApplication {

	public static void main(final String[] args) {
		SpringApplication.run(LedgerApplication.class, args);
	}

	@Bean
	@ConditionalOnProperty(value = "caching.enabled", havingValue = "true")
	public CacheManager cacheManager() {
		final ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager("caFabricClients", "accounts", "channels");
		concurrentMapCacheManager.setAllowNullValues(false);
		return new TransactionAwareCacheManagerProxy(concurrentMapCacheManager);
	}

	@Bean
	public Executor taskExecutor(
		@Value("${executor.pool.core-size}") final int coreSize,
		@Value("${executor.pool.max-size}") final int maxSize,
		@Value("${executor.pool.queue-capacity}") final int queueCapacity) {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(coreSize);
		executor.setMaxPoolSize(maxSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("TxThread~");
		executor.initialize();
		return executor;
	}

	@Bean
	@ConditionalOnProperty(value = "caching.enabled", havingValue = "false")
	public CacheManager noCacheManager() {
		return new NoOpCacheManager();
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

	@Bean
	public HttpClient httpClient() {
		return HttpClients.createDefault();
	}

	@Bean
	public ModelMapper modelMapper() {
		final ModelMapper modelMapper = new ModelMapper();
//		modelMapper.addMappings(PeerInfo.mappedFields);
		modelMapper.addMappings(Account.mappedFields);
//		modelMapper.addMappings(ChannelInfo.mappedFields);
		modelMapper.addMappings(OrdererInfo.mappedFields);
		modelMapper.addMappings(ChaincodeUpgradeLog.mappedFields);
		modelMapper.addMappings(OrgInfo.mappedFields);
		return modelMapper;
	}
}
