package com.zoharc.repository;

import org.springframework.data.repository.CrudRepository;

import com.zoharc.persistence.Stock;
import com.zoharc.persistence.StockPK;

public interface StockRepository extends CrudRepository<Stock, StockPK> {

}
