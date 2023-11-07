package com.utavi.ledger.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

@Configuration
@Component
@EnableConfigurationProperties
public class OrganizationProperties {

	private final static Logger LOGGER = LoggerFactory.getLogger(OrganizationProperties.class.getName());

	public enum TestTarget {
		BLOCKCHAIN, CHANNEL
	}

//	private final int channelNum;
//	private final Set<String> channels;
	private final Map<TestTarget, String> testTargetToChannel = new HashMap<>();

//	public OrganizationProperties(@Value("${organization.peer.channel-num}") final int channelNum, final Environment environment) {
//		this.channelNum = channelNum;
//		final IntStream intStream = IntStream.range(0, channelNum);
//		if (environment.acceptsProfiles(Profiles.of("dev", "test"))) {
//			LOGGER.info("Mapping channel names for a non-production environment");
//			this.channels = intStream.mapToObj(num -> "channel" + System.currentTimeMillis() + num).collect(Collectors.toSet());
//		} else if (environment.acceptsProfiles(Profiles.of("docker"))){
//			LOGGER.info("Mapping channel names for a production environment");
//			this.channels = intStream.mapToObj(num -> "channel" + num).collect(Collectors.toSet());
//		} else {
//			throw new IllegalArgumentException("Unknown profile(s): " + Arrays.toString(environment.getActiveProfiles()));
//		}
//		if (environment.acceptsProfiles(Profiles.of("test", "dev"))) {
//			if (this.channels.size() < 2) {
//				throw new IllegalArgumentException("channelNum property should be >= 2");
//			}
//			final List<String> stringList = this.channels.stream().limit(2).collect(Collectors.toList());
//			this.testTargetToChannel.put(TestTarget.BLOCKCHAIN, stringList.get(0));
//			this.testTargetToChannel.put(TestTarget.CHANNEL, stringList.get(1));
//		}
//	}

//	public Set<String> getChannels() {
//		return Collections.unmodifiableSet(this.channels);
//	}
//
//	public int getChannelNum() {
//		return this.channelNum;
//	}

	public String getTargetTestChannel(final TestTarget testTarget) {
		return this.testTargetToChannel.get(testTarget);
	}

}
