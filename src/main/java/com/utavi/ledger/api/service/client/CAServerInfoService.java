package com.utavi.ledger.api.service.client;

import com.utavi.ledger.api.model.CAServerInfo;
import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.repository.CAServerInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CAServerInfoService {

	private final static Logger LOGGER = LoggerFactory.getLogger(CAServerInfoService.class.getName());

	private final CAServerInfoRepository caServerInfoRepository;

	public CAServerInfoService(final CAServerInfoRepository caServerInfoRepository) {
		this.caServerInfoRepository = caServerInfoRepository;
	}

	public List<CAServerInfo> findCAServers() {
		return this.caServerInfoRepository.findAll();
	}

	public Set<CAServerInfo> findByOrgType(final OrgType orgType) {
		return this.caServerInfoRepository.findByOrgType(orgType);
	}

	public CAServerInfo findOneByOrgType(final OrgType orgType) {
		return findByOrgType(orgType).stream().findAny().orElseThrow(() -> new IllegalStateException("CA server not found"));
	}

	public CAServerInfo createServerInfo(OrgInfo orgInfo, String name, String location, String tlsCertificate) {
		return new CAServerInfo(name, location, orgInfo, tlsCertificate);
	}

	private int getAvailableCAsNum(final OrgType orgType) {
		return findByOrgType(orgType).size();
	}

	public void save(final Set<CAServerInfo> servers) {
		this.caServerInfoRepository.saveAll(servers);
	}
}
