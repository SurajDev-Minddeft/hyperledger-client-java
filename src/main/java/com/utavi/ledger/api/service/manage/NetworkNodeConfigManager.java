package com.utavi.ledger.api.service.manage;

import com.utavi.ledger.api.dto.manage.OrgNodeDto;
import com.utavi.ledger.api.model.BaseOrganizationComponent;
import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.repository.NodeGenericRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public abstract class NetworkNodeConfigManager<T extends BaseOrganizationComponent> {

	protected final NodeGenericRepository<T> repository;

	protected NetworkNodeConfigManager(final NodeGenericRepository<T> repository) {
		this.repository = repository;
	}

	public abstract void save(Set<NodeConfigWrapper> nodeConfigWrappers);

	public Set<NodeConfigWrapper> mapNetworkNodeConfiguration(OrgInfo orgInfo, List<OrgNodeDto> orgNodeDtos, String pemTlsCertificate) {
		return orgNodeDtos
				   .stream()
				   .map(orgNodeDto -> new NodeConfigWrapper(
					   orgNodeDto.getName(),
					   orgNodeDto.getLocation(),
					   orgInfo,
					   pemTlsCertificate))
				   .collect(toSet());
	}

	public Set<T> findAll() {
		return new HashSet<>(this.repository.findAll());
	}

	public Set<T> findAllByName(final Set<String> names) {
		return this.repository.findAllByName(names);
	}

	public static class NodeConfigWrapper {

		private final String name;
		private final String location;
		private final OrgInfo orgInfo;
		private final String tlsCertificate;

		NodeConfigWrapper(final String name, final String location, final OrgInfo orgInfo, final String tlsCertificate) {
			this.name = name;
			this.location = location;
			this.orgInfo = orgInfo;
			this.tlsCertificate = tlsCertificate;
		}

		public String getName() {
			return this.name;
		}

		public String getLocation() {
			return this.location;
		}

		public OrgInfo getOrgInfo() {
			return this.orgInfo;
		}

		public String getTlsCertificate() {
			return this.tlsCertificate;
		}
	}

}
