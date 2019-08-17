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

import com.zoharc.persistence.IndustryTbl;
import com.zoharc.repository.IndustryRepository;


/**
* @author Zohar Cohen Date: 16-Aug-2019
 *
 */
@RestController
@RequestMapping("/industryServices")
public class IndustryGateWay {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndustryGateWay.class);

	@Autowired
	private IndustryRepository industryRepository;

	/*--------------------------------------------------------GET INDUSTRY DETAILS----------------------------------------------------------------*/


	@GetMapping(path = "/allIndustries", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllIndustries(){

		LOGGER.info("getAllIndustries(): Going to provide the avilable industries...");
		List<IndustryTbl>  entities = new ArrayList<>();
		industryRepository.findAll().forEach(entities::add);
		if(entities.size() == 0) {

			LOGGER.warn("There is no exchange avilable in DB");
			return new ResponseEntity<Object>("There is no exchange data in the system", HttpStatus.EXPECTATION_FAILED);
		}
		LOGGER.info("getAllIndustries(): return {} industries details", entities.size());
		return new ResponseEntity<Object>(entities, HttpStatus.OK);
	}


	@GetMapping(path = "/getIndustryByID/{industryCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getIndustryDetailsByExcode(@PathVariable("industryCode") long industryCode){

		LOGGER.info("getIndustryDetailsByExcode(): Going to provide details for {} industry code", industryCode);
		Optional<IndustryTbl> industryDetails = industryRepository.findById(industryCode);
		if(!industryDetails.isPresent()) {
			LOGGER.info("getIndustryDetailsByExcode(): The industry {} doesn't exsist in the system", industryCode);
			return new ResponseEntity<Object>("There is no data in the system for the given excode", HttpStatus.EXPECTATION_FAILED);
		}
		LOGGER.info("getIndustryDetailsByExcode(): The industry has been found, response: {}", industryDetails);
		return new ResponseEntity<>(industryDetails, HttpStatus.OK);

	}

	/*--------------------------------------------------------DELETE INDUSTRY DETAILS----------------------------------------------------------------*/


	@DeleteMapping(path = "/deleteIndustryByPK/{industryCode}")
	public void deleteIndustryByExchangeCode(@PathVariable("industryCode") long industryCode) {
		LOGGER.info("updateIndustryDetails(): Going to delete record of {} industry code", industryCode);
		industryRepository.deleteById(industryCode);
	}	


	/*--------------------------------------------------------UPDATE INDUSTRY DETAILS----------------------------------------------------------------*/


	@PutMapping("/updateIndustryDetails/{industryCode}")
	public ResponseEntity<Object> updateIndustryDetails(@RequestBody IndustryTbl industry, @PathVariable("industryCode") long industryCode){
		LOGGER.info("updateIndustryDetails(): Going to update industry code: {}, with the follwing info: {}",industryCode, industry);
		Optional<IndustryTbl> industryDetails = industryRepository.findById(industryCode);
		if(!industryDetails.isPresent()) {
			LOGGER.info("updateIndustryDetails(): The exchange doesn't exsit in the system, ignoring...");
			return ResponseEntity.notFound().build();
		}
		industryRepository.save(industry);
		return ResponseEntity.noContent().build();
	}

	/*--------------------------------------------------------INSERT NEW INDUSTRY --------------------------------------------------------------------*/
	@PostMapping("/newIndustry")
	public ResponseEntity<Object> insertNewIndustry(@RequestBody IndustryTbl industry){
		LOGGER.info("insertNewExchange(): Going to insert new industry record: {}", industry);
		if(industry == null) {
			LOGGER.warn("insertNewExchange(): There is no data in the request body, ignore...");
			return new ResponseEntity<Object>("There is no industry information in the request body", HttpStatus.BAD_REQUEST);
		}
		industryRepository.save(industry);
		String msg = String.format("New industry has been stored in the system, industry: %s", industry);
		LOGGER.info(msg);
		return new ResponseEntity<Object>(msg, HttpStatus.OK);
	} 



}
