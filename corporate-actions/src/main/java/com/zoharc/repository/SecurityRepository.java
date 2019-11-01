package com.zoharc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.zoharc.persistence.Security;

public interface SecurityRepository extends CrudRepository<Security, Long> {

	@Query("SELECT s FROM Security s WHERE s.externalKey1 = : externalKeyOne")
	Optional<Security> findSecurityObjByExternalId(@Param("externalKeyOne") int externalKeyOne);
	
	@Query("SELECT s.securitySeqNum FROM Security s WHERE s.isin = :isin")
	Optional<Integer> findSecurityIdByIsin(@Param("isin") String isin);
	
	@Query("SELECT s FROM Security s WHERE s.uscode = :uscode")
	Optional<Security> findSecurityObjByUsCode(@Param("uscode") String uscode);
	
	@Query("SELECT s FROM Security s WHERE s.isin = :isin")
	Optional<Security> findSecurityObjByIsin(@Param("isin") String isin);
}
