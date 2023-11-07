package com.utavi.ledger.api.service.network.channel;

import com.utavi.ledger.api.dto.AttachSiloDto;
import com.utavi.ledger.api.model.ChannelDetails;
import com.utavi.ledger.api.repository.ChannelDetailsRepository;
import com.utavi.ledger.api.service.client.ChaincodeInvokeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttachChannelToSiloService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AttachChannelToSiloService.class.getName());

    private final ChannelDetailsRepository channelDetailsRepository;
    private final ChaincodeInvokeService chaincodeInvokeService;

    public AttachChannelToSiloService(ChannelDetailsRepository channelDetailsRepository, ChaincodeInvokeService chaincodeInvokeService) {
        this.channelDetailsRepository = channelDetailsRepository;
        this.chaincodeInvokeService = chaincodeInvokeService;
    }

    @Transactional
    public void attach(final AttachSiloDto dto) {
        final String siloName = dto.getName();
        final ChannelDetails channelDetails = findNotAttached();
        LOGGER.info("Attaching silo {} to the channel {}", siloName, channelDetails.getName());
        channelDetails.setSiloName(siloName);
        this.channelDetailsRepository.save(channelDetails);
        final String apiKey = channelDetails.getApiKey();
        final String channelName = channelDetails.getName();
        this.chaincodeInvokeService.addApiKey(channelName, apiKey).join();
        LOGGER.info("Successfully attached silo {} to the channel {}", siloName, channelDetails.getName());
    }

    private ChannelDetails findNotAttached() {
        return this.channelDetailsRepository.findNotAttached().stream().findAny().orElseThrow(() -> new RuntimeException("There no channels available"));
    }
}
