package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.ChannelDetails;
import com.utavi.ledger.api.repository.GenericRepository;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelDetailsRepository extends GenericRepository<ChannelDetails> {

	@Query("SELECT channelDetails FROM ChannelDetails channelDetails WHERE channelDetails.name=:name")
	Optional<ChannelDetails> findByName(@Param("name") final String name);

	@Query("SELECT channelDetails FROM ChannelDetails channelDetails WHERE channelDetails.siloName=:siloName")
	Optional<ChannelDetails> findBySiloName(@Param("siloName") final String siloName);

	@Query("SELECT channelDetails FROM ChannelDetails channelDetails WHERE channelDetails.siloName IS NULL")
	Set<ChannelDetails> findNotAttached();

}
