package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.ChaincodeUpgradeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChaincodeUpgradeInfoRepository extends JpaRepository<ChaincodeUpgradeLog, Long> {

	@Query("SELECT info FROM ChaincodeUpgradeLog info WHERE info.externalChaincodeInfo.chaincodeInfo.name=:chaincodeName AND info.deployVersion=:deployVersion AND info.channelDetails.name=:channelName AND info.upgradeStatus=com.utavi.ledger.api.model.enums.ChaincodeUpgradeStatus.COMPLETED")
	List<ChaincodeUpgradeLog> findByNameAndDeployVersionAndChannelNameInStatusComplete(@Param("chaincodeName") final String chaincodeName, @Param("deployVersion") final String deployVersion, @Param("channelName") final String channelName);

}
