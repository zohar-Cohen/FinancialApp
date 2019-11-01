package com.zoharc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.zoharc.persistence.Stock;
import com.zoharc.persistence.StockPK;

public interface StockRepository extends CrudRepository<Stock, StockPK> {

	/**
	 * The method select the stock based on the global external unique id's.
	 * @param externalKeyOne - global listing id.
	 * @param externalKeyTwo - external unique id
	 * @return Stock object
	 */
	@Query("SELECT s FROM Stock s WHERE s.externalKey1 = : externalKeyOne AND s.externalKey2 = :externalKeyTwo")
	Optional<Stock> findStockByExternalIds(@Param("") int externalKeyOne, @Param("") int externalKeyTwo);

	@Query("SELECT s FROM Stock s , Security i WHERE i.isin = :isin AND s.id.stockCurrency = :currencyCode AND s.id.exchangeCode = :exchangeCode AND s.stockSeqNum = i.securitySeqNum")
	Optional<Stock> findStockByUniqeKeys(@Param("isin") String isin, @Param("currencyCode") String currencyCode, @Param("exchangeCode") String exchangeCode);
}
