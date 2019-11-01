package com.zoharc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoharc.persistence.Stock;
import com.zoharc.repository.StockRepository;
import com.zoharc.service.IStockService;

@RestController
@RequestMapping("/stockServices")
public class StockGateWay {

	public static Logger LOG = LoggerFactory.getLogger(StockGateWay.class);
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private IStockService stockService;



	@GetMapping(path = "/allStocks", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> findAllStocks() {

		LOG.info("findAllEntities(): Going to provide the avilable stoks");
		List<Stock> entities = new ArrayList<>();
		stockRepository.findAll().forEach(entities::add);;
		if(entities.size() == 0) {
			String warnMessege = "There are no Stocks in the DataBase...";
			LOG.warn("findAllEntities(): {}", warnMessege);
			return new ResponseEntity<>(warnMessege,HttpStatus.EXPECTATION_FAILED);

		}
		LOG.info("getAllIndustries(): return {} stocks details", entities.size());
		return new ResponseEntity<Object>(entities, HttpStatus.OK);
	}

	/*
	 * @GetMapping(path = "/getExchangeByName/{exchangeName}")
	public ResponseEntity<Object> getExchangeDetailsByExactName(@PathVariable("exchangeName") String exchangeName){

	 * 	public Stock getStockByUniqeIdentifiers(String isin, String currencyCode, String exchangeCode);

	 */
	@GetMapping(path = "/byUniqeKeys/{isin}/{currencyCode}/{exchangeCode}")	
	public ResponseEntity<Object> findStockByUniqeKeys(@PathVariable("isin") String isin, @PathVariable("currencyCode") String currencyCode, @PathVariable("exchangeCode") String exchangeCode){

		Stock stock = null;
		try {
			stock = stockService.getStockByUniqeIdentifiers(isin, currencyCode, exchangeCode);
		}
		catch(EntityNotFoundException e) {
			return new ResponseEntity<>("Entity Not Found", HttpStatus.EXPECTATION_FAILED);

		}
		return new ResponseEntity<>(stock, HttpStatus.OK);
	}




	@PostMapping(path = "/newStock")
	public ResponseEntity<Object> insertNewStock(@RequestBody Stock stock){
		LOG.info("insertNewExchange(): Going to insert new industry record: {}, {}", stock.getId().getMappedStock(), stock.getId().getExchangeCode());
		if(Objects.isNull(stock)) {
			String warnMessege = "There is no data in the requst body, ignore...";
			LOG.warn(warnMessege);
			return new ResponseEntity<>(warnMessege, HttpStatus.BAD_REQUEST);
		}
		String infoMessege = "New stock has been stored in the DataBase";
		stockRepository.save(stock);
		return new ResponseEntity<>(infoMessege, HttpStatus.OK);
	}










}
