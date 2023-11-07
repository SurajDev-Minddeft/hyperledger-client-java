package com.utavi.ledger.api.service.network.channel;

import com.utavi.ledger.api.model.ChannelDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StartupChannelInitializerService implements ApplicationRunner {

	private final static Logger LOGGER = LoggerFactory.getLogger(StartupChannelInitializerService.class.getName());

	private final ChannelDetailsService channelDetailsService;
	private final HFInitializeChannelService HFInitializeChannelService;

	public StartupChannelInitializerService(final ChannelDetailsService channelDetailsService, final HFInitializeChannelService HFInitializeChannelService) {
		this.channelDetailsService = channelDetailsService;
		this.HFInitializeChannelService = HFInitializeChannelService;
	}

	//todo wujek tutaj startuje inicjalizacja polaczenia z fabric
	@Override
	@Transactional
	public void run(final ApplicationArguments args) {
		final List<ChannelDetails> channelInfos = this.channelDetailsService.findAll();
		if (channelInfos.isEmpty()) {
			LOGGER.info("No channels found.");
		} else {
			LOGGER.info("Found {} channels. Starting initialization process...", channelInfos.size());
			channelInfos.forEach(this::initializeChannel);
		}
	}

	public void initializeChannel(final ChannelDetails channelDetails) {
		try {
			this.HFInitializeChannelService.initializeChannel(channelDetails);
			LOGGER.info("Successfully initialized channel={}", channelDetails.getName());
		} catch (final RuntimeException e) {
			LOGGER.warn("Failed to initialize channel={}, cause: {}", channelDetails.getName(), e);
		}
	}

}
