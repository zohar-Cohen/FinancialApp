package com.zoharc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoharc.persistence.Exchange;
import com.zoharc.repository.ExchangeRepository;

/**
 * @author Zohar Cohen Date: 16-Aug-2019
 *
 */
@RestController
@RequestMapping("/exchangeServices")
public class ExchangeGateWay {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeGateWay.class);

	@Autowired
	private ExchangeRepository exchangeRepository;

	/*--------------------------------------------------------GET EXCHANGE DETAILS----------------------------------------------------------------*/
	@GetMapping(path = "/allMarketCodes", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllExchangesWithMarketCode(){
		LOGGER.info("getAllExchangesWithMarketCode(): Going to provide the avilable market codes...");
		Optional<List<String[]>> marketCodes = exchangeRepository.findAllMarketCodes();
		if(!marketCodes.isPresent()) {
			String msg = "There is no exchanges with market code, please check the referance data implementation.";
			LOGGER.warn(msg);
			return new ResponseEntity<>(msg,HttpStatus.EXPECTATION_FAILED);
			
		}
		LOGGER.info("getAllExchangesWithMarketCode(): return {} exchanges details", marketCodes.get().size());
		return new ResponseEntity<Object>(marketCodes.get(), HttpStatus.OK);
		
	}

	@GetMapping(path = "/allexchanges", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllExchanges(){

		LOGGER.info("getAllExchanges(): Going to provide the avilable exchanges...");
		List<Exchange>  entities = new ArrayList<>();
		exchangeRepository.findAll().forEach(entities::add);
		if(entities.size() == 0) {

			LOGGER.warn("There is no exchange avilable in DB");
			return new ResponseEntity<Object>("There is no exchange data in the system", HttpStatus.EXPECTATION_FAILED);
		}
		LOGGER.info("getAllExchanges(): return {} exchanges details", entities.size());
		return new ResponseEntity<Object>(entities, HttpStatus.OK);
	}


	@GetMapping(path = "/getExchangeByID/{exchangeCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getExhangeDetailsByExcode(@PathVariable("exchangeCode") String exchangeCode){

		LOGGER.info("getExhangeDetailsByExcode(): Going to provide details for {} exchange code", exchangeCode);
		Optional<Exchange> exchangeDetails = exchangeRepository.findById(exchangeCode);
		if(!exchangeDetails.isPresent()) {
			LOGGER.info("getExhangeDetailsByExcode(): The exchange {} doesn't exsist in the system", exchangeCode);
			return new ResponseEntity<Object>("There is no data in the system for the given excode", HttpStatus.EXPECTATION_FAILED);
		}
		LOGGER.info("getExhangeDetailsByExcode(): The exchange has been found, response: {}", exchangeDetails);
		return new ResponseEntity<>(exchangeDetails, HttpStatus.OK);


	}
	
	@GetMapping(path = "/getExchangeLikeName/{exchangeName}")
	public ResponseEntity<Object> getExchangeDetailsByName(@PathVariable("exchangeName") String exchangeName){

		LOGGER.info("getExchangeDetailsByName(): Going to provide details for {} exchange name", exchangeName);
		Optional<List<Exchange>> exchangeEntities = exchangeRepository.findExchangeContainsName(exchangeName);

		if(!exchangeEntities.isPresent()) {
			LOGGER.info("getExchangeDetailsByName(): There is no exchange that contains the given exchange name: {}",exchangeName);
			return new ResponseEntity<Object>("There is no data in the system that contains the given exchange name", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<Object>(exchangeEntities, HttpStatus.OK);

	}
	@GetMapping(path = "/getExchangeByName/{exchangeName}")
	public ResponseEntity<Object> getExchangeDetailsByExactName(@PathVariable("exchangeName") String exchangeName){

		LOGGER.info("getExchangeDetailsByExactName(): Going to provide details for {} exchange name", exchangeName);
		Optional<Exchange> exchangeEntity = exchangeRepository.findExchangeByName(exchangeName);
		if(!exchangeEntity.isPresent()) {
			LOGGER.info("getExchangeDetailsByExactName(): There is no exchange with given exchange name: {}",exchangeName);
			return new ResponseEntity<Object>("There is no data in the system that contains the given exchange name", HttpStatus.EXPECTATION_FAILED);   
		}
		LOGGER.info("getExchangeDetailsByExactName(): Going to return the follwing exchnage: {}",exchangeEntity);
		return new ResponseEntity<Object>(exchangeEntity, HttpStatus.OK);
	}

	/*--------------------------------------------------------DELETE EXCHANGE DETAILS----------------------------------------------------------------*/


	@DeleteMapping(path = "/deleteExchangeByPK/{exchangeCode}")
	public void deleteExchangeByExchangeCode(@PathVariable("exchangeCode") String exchangeCode) {
		LOGGER.info("deleteExchangeByExchangeCode(): Going to delete recordfor {} exchange code", exchangeCode);
		exchangeRepository.deleteById(exchangeCode);
	}	


	/*--------------------------------------------------------UPDATE EXCHANGE DETAILS----------------------------------------------------------------*/

	/**
	 * The method updates the exchange table based on the given exchange code in the URL.
	 * The method expects for two arguments:
	 * 1) exchangeCode - for example http://localhost:8080/updateExchangeDetails/ILTSE
	 * 2) ExchangeTbl object - the API expects for a exchange object in the HTTP's body, for example:
	 *    **Make sure to specify that in the header that the format is JSON (POSTMAN drop down in the body radio button).
	 *    	{
	 *          "exchangeCode": "ILTSE",
	 *          "exchangeName": "Tel-Aviv Stock Exchange",
	 *          "exchangeCountry": "Israel"
	 *      }
	 * @param exchange - exchange object in the HTTP put's body.
	 * @param exchangeCode - exchange code
	 * @return HTTP status
	 */
	@PutMapping("/updateExchangeDetails/{exchangeCode}")
	public ResponseEntity<Object> updateExchangeDetails(@RequestBody Exchange exchange, @PathVariable("exchangeCode") String exchangeCode){
		LOGGER.info("updateExchangeDetails(): Going to update exchange code: {}, with the follwing info: {}",exchangeCode, exchange);
		Optional<Exchange> exchangeDetails = exchangeRepository.findById(exchangeCode);
		if(!exchangeDetails.isPresent()) {
			LOGGER.info("updateExchangeDetails(): The exchange doesn't exsit in the system, ignoring...");
			return ResponseEntity.notFound().build();
		}

		exchangeRepository.save(exchange);
		return ResponseEntity.noContent().build();
	}

	/*--------------------------------------------------------INSERT NEW EXCHANGE --------------------------------------------------------------------*/
	@PostMapping("/newExchange")
	public ResponseEntity<Object> insertNewExchange(@RequestBody Exchange exchange){
		LOGGER.info("insertNewExchange(): Going to insert new exchange exchange: {}", exchange);
        if(exchange == null) {
    		LOGGER.warn("insertNewExchange(): There is no data in the request body, ignore...");
            return new ResponseEntity<Object>("There is no exchange in the request body", HttpStatus.BAD_REQUEST);
        }
        exchangeRepository.save(exchange);
        String msg = String.format("New exchange has been stored in the system, exchange: %s", exchange);
        LOGGER.info(msg);
        return new ResponseEntity<Object>(msg, HttpStatus.OK);
	} 


}

