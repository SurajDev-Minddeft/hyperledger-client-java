package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.BaseOrganizationComponent;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface NodeGenericRepository<T extends BaseOrganizationComponent> extends GenericRepository<T> {

	@Query("SELECT node FROM #{#entityName} node WHERE node.name IN :names")
	Set<T> findAllByName(@Param("names") final Set<String> names);

}
