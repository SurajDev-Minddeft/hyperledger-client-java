package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.BaseEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

	Optional<T> findByName(final String name);

}
