package com.zoharc.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoharc.persistence.Stock;
import com.zoharc.repository.StockRepository;

@Service
public class StockServiceImpl implements IStockService {

	private static final Logger LOG = LoggerFactory.getLogger(StockServiceImpl.class);
	@Autowired
	private StockRepository stockRepository;

	@Override
	@Transactional(readOnly = true)
	public Stock getStockByUniqeIdentifiers(String isin, String currencyCode, String exchangeCode) {

		LOG.info("getStockByUniqeIdentifiers(); Going to search  for a stock in the DataBase using stock's uniqe identifires:");
		LOG.info("Isin: {}, ExchangeCode: {}, CurrencyCode: {}", isin, exchangeCode, currencyCode);
		Optional<Stock> stock = stockRepository.findStockByUniqeKeys(isin, currencyCode, exchangeCode);
		if(!stock.isPresent()) {
			LOG.info("getStockByUniqeIdentifiers(): There is no stock in the system for the given keys...");
			throw new EntityNotFoundException();
		}
		return stock.get();
	}

}
