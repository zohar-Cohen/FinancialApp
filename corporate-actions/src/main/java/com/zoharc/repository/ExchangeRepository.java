package com.zoharc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.zoharc.persistence.ExchangeTbl;

/**
 * @author Zohar Cohen Date: 16-Aug-2019
 *
 */
public interface ExchangeRepository extends CrudRepository<ExchangeTbl,String> {

	@Query("Select e FROM ExchangeTbl e where e.exchangeName = :exchangeName")
	Optional<ExchangeTbl> findExchangeByName(@Param("exchangeName") String exchangeName);
	
	@Query("Select e FROM ExchangeTbl e where e.exchangeName LIKE CONCAT('%',:exchangeName,'%')")
	Optional<List<ExchangeTbl>> findExchangeContainsName(@Param("exchangeName") String exchangeName);
	
	
}
