package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.OrdererInfo;
import com.utavi.ledger.api.repository.GenericRepository;
import com.utavi.ledger.api.repository.NodeGenericRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdererDetailsRepository extends NodeGenericRepository<OrdererInfo> {

	@Query("SELECT ordererInfo FROM OrdererInfo ordererInfo WHERE ordererInfo.name=:name")
	Optional<OrdererInfo> findByName(@Param("name") final String name);
}
