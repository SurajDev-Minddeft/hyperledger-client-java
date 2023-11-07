package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrgInfoRepository extends GenericRepository<OrgInfo> {

	@Query("SELECT orgInfo FROM OrgInfo orgInfo WHERE orgInfo.name=:name")
	Optional<OrgInfo> findByName(@Param("name") final String name);

	@Query("SELECT orgInfo FROM OrgInfo orgInfo WHERE orgInfo.orgType=:orgType")
	Optional<OrgInfo> findByType(@Param("orgType") final OrgType orgType);
}
