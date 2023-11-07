package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.ChaincodeInfo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChaincodeInfoRepository extends JpaRepository<ChaincodeInfo, Long> {

//	@Query("SELECT info FROM ChaincodeInfo info WHERE info.version.current=true")
//	Optional<ChaincodeInfo> findCurrent();

	@Query("SELECT info FROM ChaincodeInfo info WHERE info.name=:name AND info.version=:version")
	Optional<ChaincodeInfo> findOne(@Param("name") final String name, @Param("version") final String version);

	@Query
	Optional<ChaincodeInfo> findChaincodeInfoByNameAndVersion(@Param("name") final String name, @Param("version") final String version);

	@Query("SELECT info FROM ChaincodeInfo info WHERE info.name=:name")
	Optional<ChaincodeInfo> findByName(@Param("name") final String name);

}
