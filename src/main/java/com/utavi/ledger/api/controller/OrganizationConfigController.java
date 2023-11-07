package com.utavi.ledger.api.controller;

import com.utavi.ledger.api.dto.manage.SaveOrganizationConfigDto;
import com.utavi.ledger.api.service.manage.OrganizationConfigManagerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/manage/organization")
public class OrganizationConfigController {

	private final OrganizationConfigManagerService organizationConfigManagerService;

	public OrganizationConfigController(final OrganizationConfigManagerService organizationConfigManagerService) {
		this.organizationConfigManagerService = organizationConfigManagerService;
	}

	//todo wujek Krok 1 inicjalizacji blockchaina
	@PostMapping()
	public void saveOrganizationConfiguration(
			@Valid @RequestBody final SaveOrganizationConfigDto saveOrganizationConfigDto) {
		this.organizationConfigManagerService.writeOrgConfig(
			List.of(saveOrganizationConfigDto.getPeerOrganization(), saveOrganizationConfigDto.getOrdererOrganization())
		);
	}

}
