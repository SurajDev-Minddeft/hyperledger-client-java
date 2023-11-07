package com.utavi.ledger.api.controller;

import com.utavi.ledger.api.controller.util.MapperUtil;
import com.utavi.ledger.api.dto.CAServerInfoDto;
import com.utavi.ledger.api.model.CAServerInfo;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.service.client.CAServerInfoService;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/ca")
public class CAController {
	private final CAServerInfoService caServerService;
	private final MapperUtil mapperUtil;

	public CAController(final CAServerInfoService caServerService, final MapperUtil mapperUtil) {
		this.caServerService = caServerService;
		this.mapperUtil = mapperUtil;
	}

	@GetMapping
	public ResponseEntity<Set<CAServerInfoDto>> find() {
		return ResponseEntity.ok(this.caServerService.findCAServers().stream().map(this::doMap).collect(Collectors.toSet()));
	}

	@GetMapping("/{orgType}")
	public ResponseEntity<Set<CAServerInfoDto>> find(@PathVariable final OrgType orgType) {
		return ResponseEntity.ok(this.caServerService.findByOrgType(orgType).stream().map(this::doMap).collect(Collectors.toSet()));
	}

	private CAServerInfoDto doMap(final CAServerInfo caServerInfo) {
		return this.mapperUtil.mapToType(caServerInfo, CAServerInfoDto.class);
	}

}
