package com.utavi.ledger.api.controller;

import com.utavi.ledger.api.controller.util.MapperUtil;
import com.utavi.ledger.api.dto.OrgInfoDto;
import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.service.manage.OrganizationConfigManagerService;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/org")
public class OrgInfoController {

	private final OrganizationConfigManagerService organizationConfigManagerService;
	private final MapperUtil mapperUtil;
	public OrgInfoController(final OrganizationConfigManagerService organizationConfigManagerService, final MapperUtil mapperUtil) {
		this.organizationConfigManagerService = organizationConfigManagerService;
		this.mapperUtil = mapperUtil;
	}

	@GetMapping
	public ResponseEntity<Set<OrgInfoDto>> find() {
		return ResponseEntity.ok(this.organizationConfigManagerService.findAll().stream().map(this::doMap).collect(Collectors.toSet()));
	}

	private OrgInfoDto doMap(final OrgInfo orgInfo) {
		return this.mapperUtil.mapToType(orgInfo, OrgInfoDto.class);
	}

}
