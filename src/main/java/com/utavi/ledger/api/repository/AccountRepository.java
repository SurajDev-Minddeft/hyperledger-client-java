package com.utavi.ledger.api.repository;

import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.enums.AccountType;
import java.util.Optional;
import java.util.Set;

import com.utavi.ledger.api.model.enums.OrgType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends GenericRepository<Account> {

	@Query("SELECT account FROM Account account WHERE account.name=:name")
	Optional<Account> findByName(@Param("name") final String name);

	@Query("SELECT account FROM Account account WHERE account.accountType=:accountType")
	Set<Account> findByAccountType(@Param("accountType") final AccountType accountType);

	@Query("SELECT account FROM Account account WHERE account.accountType=:accountType AND account.orgInfo.orgType=:orgType")
	Set<Account> findByAccountTypeAndOrgType(@Param("accountType") final AccountType accountType, @Param("orgType") final OrgType orgType);

	@Query("DELETE FROM Account account WHERE account.name=:name")
	@Modifying
	void deleteByName(@Param("name") final String name);

	@Query("SELECT account FROM Account account WHERE account.orgInfo.orgType=:orgType")
	Set<Account> findAllByOrgType(@Param("orgType") final OrgType orgType);

	@Query("UPDATE Account account SET account.isActive = false WHERE account.name=:accountName")
	void updateAccountSetActiveFalse(@Param("accountName") String accountName);

}
