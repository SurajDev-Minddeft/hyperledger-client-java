package com.utavi.ledger.api.controller;

import com.utavi.ledger.api.dto.AttachSiloDto;
import com.utavi.ledger.api.dto.ChannelInfoDto;
import com.utavi.ledger.api.dto.chaincode.SaveChaincodeDeploymentInfoDto;
import com.utavi.ledger.api.model.ChannelDetails;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.utavi.ledger.api.service.network.channel.AttachChannelToSiloService;
import com.utavi.ledger.api.service.network.channel.ChannelDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/channel")
public class ChannelDetailsController {

	private final ChannelDetailsService channelDetailsService;
	private final AttachChannelToSiloService attachChannelToSiloService;

	public ChannelDetailsController(final ChannelDetailsService channelDetailsService, final AttachChannelToSiloService attachChannelToSiloService) {
		this.channelDetailsService = channelDetailsService;
		this.attachChannelToSiloService = attachChannelToSiloService;
	}

	//todo tylko baze uaktualnia
	@PutMapping("/chaincode-deployment")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody final SaveChaincodeDeploymentInfoDto saveChaincodeDeploymentInfoDto) {
		this.channelDetailsService.createAndSave(saveChaincodeDeploymentInfoDto);
	}


	//todo wujek uzywane prze utavi-user-service. to znajduje w bazie nowy kanal bez silosa (kazdy), zapisuje w bazie polaczenie tego kanalu z silosem i wywoluje 1 transakcje w chaincode, zapisujac api key
	@PostMapping("/attach")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void attach(@Valid @RequestBody final AttachSiloDto attachSiloDto) {
		this.attachChannelToSiloService.attach(attachSiloDto);
	}

	@GetMapping
	public ResponseEntity<Set<ChannelInfoDto>> find() {
		return ResponseEntity.ok(this.channelDetailsService.findAll().stream().map(ChannelDetails::toDto).collect(Collectors.toSet()));
	}

}
