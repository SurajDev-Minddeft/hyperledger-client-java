package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.ChaincodeInfo;
import com.utavi.ledger.api.model.chaincode.ExternalChaincodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExternalChaincodeInfoRepository extends JpaRepository<ExternalChaincodeInfo, Long> {

//	@Query("SELECT info FROM ChaincodeInfo info WHERE info.version.current=true")
//	Optional<ChaincodeInfo> findCurrent();

	@Query("SELECT info FROM ChaincodeInfo info WHERE info.name=:name AND info.version=:version")
	Optional<ChaincodeInfo> findOne(@Param("name") final String name, @Param("version") final String version);

	@Query("SELECT eci FROM ExternalChaincodeInfo eci WHERE eci.chaincodeInfo.name=:name AND eci.chaincodeInfo.version=:version")
	Optional<ExternalChaincodeInfo> findExternalChaincodeInfoByChaincodeNameAndVersion(@Param("name") final String chaincodeName, @Param("version") final String version);

	@Query("SELECT info FROM ChaincodeInfo info WHERE info.name=:name")
	Optional<ChaincodeInfo> findByName(@Param("name") final String name);

	@Query("SELECT eci FROM ExternalChaincodeInfo eci WHERE eci.packageId = :packageIds")
	Optional<ExternalChaincodeInfo> findByPackageId(@Param("packageIds") final String packageId);

}
