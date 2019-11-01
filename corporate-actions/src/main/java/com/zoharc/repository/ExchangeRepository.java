package com.zoharc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.zoharc.persistence.Exchange;

/**
 * @author Zohar Cohen Date: 16-Aug-2019
 *
 */
public interface ExchangeRepository extends CrudRepository<Exchange,String> {

	@Query("Select e FROM Exchange e where e.exchangeName = :exchangeName")
	Optional<Exchange> findExchangeByName(@Param("exchangeName") String exchangeName);

	@Query("Select e FROM Exchange e where e.exchangeName LIKE CONCAT('%',:exchangeName,'%')")
	Optional<List<Exchange>> findExchangeContainsName(@Param("exchangeName") String exchangeName);

	/**
	 * The method returns a list of the available exchange that contains market code.
	 * The market code might not be available for particular exchange
	 * @return list that contains an array of market, exchange code (String[0] - market code, String[1] - exchange code.
	 */
	@Query("SELECT e.marketCode, e.exchangeCode FROM Exchange e WHERE e.marketCode IS NOT NULL")
	Optional<List<String[]>> findAllMarketCodes();

	@Query("SELECT e from Exchange e WHERE e.exchangeCode = 'ABCD'")
	public Exchange findDefaultExchange(); 

}
