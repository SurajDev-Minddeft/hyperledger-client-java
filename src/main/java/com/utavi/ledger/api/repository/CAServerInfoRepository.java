package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.CAServerInfo;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface CAServerInfoRepository extends GenericRepository<CAServerInfo> {

	@Query("SELECT server FROM CAServerInfo server WHERE server.orgType=:orgType")
	Set<CAServerInfo> findByOrgType(@Param("orgType") final OrgType orgType);

	@Query("SELECT caServerInfo FROM CAServerInfo caServerInfo WHERE caServerInfo.name=:name")
	Optional<CAServerInfo> findByName(@Param("name") final String name);
}
