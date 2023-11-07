package com.utavi.ledger.api.controller;

import com.utavi.ledger.api.dto.chaincode.ChaincodeMetaInfDto;
import com.utavi.ledger.api.dto.chaincode.ExternalChaincodeInfoDto;
import com.utavi.ledger.api.service.network.chaincode.management.ChaincodeInfoService;
import com.utavi.ledger.api.service.network.channel.StartupChannelInitializerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/chaincode")
public class ChaincodeController {
	private final static Logger LOGGER = LoggerFactory.getLogger(StartupChannelInitializerService.class.getName());

	private final ChaincodeInfoService chaincodeInfoService;

	public ChaincodeController(final ChaincodeInfoService chaincodeInfoService) {
		this.chaincodeInfoService = chaincodeInfoService;
	}

	@GetMapping("/{chaincodeName}/{version}")
	public ResponseEntity<ExternalChaincodeInfoDto> getExternalChaincodeInfo(
			@PathVariable("chaincodeName") final String chaincodeName,
			@PathVariable("version") final String version) {
		LOGGER.info("Getting external chaincode infos for chaincode {} in version {}", chaincodeName, version);
		return this.chaincodeInfoService.getExternalChaincodeInfo(chaincodeName, version)
			.map(externalChaincodeInfoDto -> ResponseEntity.status(HttpStatus.OK).body(externalChaincodeInfoDto))
			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping("/info")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void accept(@Valid @RequestBody final ChaincodeMetaInfDto chaincodeMetaInfDto) {
		this.chaincodeInfoService.save(chaincodeMetaInfDto);
	}

}
