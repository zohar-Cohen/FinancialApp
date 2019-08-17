package com.zoharc.repository;

import org.springframework.data.repository.CrudRepository;

import com.zoharc.persistence.StockTbl;
import com.zoharc.persistence.identity.StockPK;

public interface StockRepository extends CrudRepository<StockTbl, StockPK> {

}
