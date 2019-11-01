package com.zoharc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


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

import com.zoharc.exception.IndustryNotFoundException;
import com.zoharc.persistence.Industry;
import com.zoharc.persistence.Security;
import com.zoharc.persistence.Stock;
import com.zoharc.persistence.StockPK;
import com.zoharc.repository.JdbcSecurityRepositoryImpl;
import com.zoharc.repository.IndustryRepository;
import com.zoharc.repository.SecurityRepository;
import com.zoharc.repository.StockRepository;
import com.zoharc.service.ExchangeService;
import com.zoharc.service.IIndustryService;

/**
 * @author Zohar Cohen Date: 16-Aug-2019
 *
 */
@RestController
@RequestMapping("/securityServices")
public class SecurityGateWay {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityGateWay.class);

	@Autowired
	private SecurityRepository securityRepository;
	@Autowired
	private ExchangeService exService;
	@Autowired
	private IIndustryService service;
	@Autowired
	private StockRepository r;
	@Autowired
	private JdbcSecurityRepositoryImpl se;

	@Autowired
	private IndustryRepository industryRepository;

	@GetMapping(path = "/allSecurities", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllSecurityes() {

		LOGGER.info("getAllSecurityes(): Going to provide the avilable securites...");
		List<Security>  entities = new ArrayList<>();
		securityRepository.findAll().forEach(entities::add);
		if(entities.size() == 0) {

			LOGGER.warn("There is no securities avilable in the system");
			return new ResponseEntity<Object>("There is no security data in the system", HttpStatus.EXPECTATION_FAILED);
		}
		LOGGER.info("getAllExchanges(): return {} securities details", entities.size());
		Set<Stock> s = entities.get(0).getStocks();
		s.forEach(System.out::println);
		return new ResponseEntity<Object>(entities, HttpStatus.OK);
	}
    //TODO:: change the exception to checked exception and catch it and return correct result;
	/*--------------------------------------------------------INSERT NEW EXCHANGE --------------------------------------------------------------------*/
	@PostMapping(path = "/newSecurityAllDetails/{industryCode}",  consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createNewSecurity(@RequestBody Security security, @PathVariable("industryCode") Integer industryCode ){

		LOGGER.info("Going to store new security in the DB");
		Optional<Industry> industry = industryRepository.findById(industryCode);
		if(!industry.isPresent()) {
			throw new IndustryNotFoundException(industryCode);
		}
		security.setIndustry(industry.get());
		securityRepository.save(security);
	}
	
	@PostMapping(path = "/test",  consumes = MediaType.APPLICATION_JSON_VALUE)
	public void test() {
		
		
		
		Security securityItem = Security.builder()
				.isin("TH0435054703")
				.uscode("")
				.status("A")
				.securityType("WAR")
				.externalKey1(5934516)
				.externalKey2(1234)
				.primaryExchange("THSET")
				.industry(service.getIndustryById(49))
				.build();
		//Build the stock object from the reader input
		String mappedStockCode = "ZZ1";
		String mappedExchangeCode = "THSET";
		
		Stock stockItem = Stock.builder().rawStock("ZZ1")
		                                 .stockName("TESTING")
		                                  .listStatus("L")
		                                  .exchange(exService.getExchangeByidentifiers(mappedExchangeCode))
		                                  .primaryExchange("Y")
		                                  .externalKey1(1234)
		                                  .externalKey2(6666)
		                                  .sedol("9809").id(StockPK.builder().mappedStock(mappedStockCode).exchangeCode(mappedExchangeCode)//.securityId(securityItem.getSecuritySeqNum())
		                		                                                      .stockCurrency("NIS").build()).build();
		//stockItem.setSecurity(securityItem);
		stockItem.getId().setSecurityId("TH0435054703");
		securityItem.getStocks().add(stockItem);
		//securityItem.getStocks().add(stockItem);

		securityRepository.save(securityItem);
		//r.save(stockItem);
	}
	
/*	
	@PostMapping(path = "/testJDBC",  consumes = MediaType.APPLICATION_JSON_VALUE)
	public void testingJDBC() {
		
		Security securityItem = Security.builder()
				.isin("TH0435054703")
				.uscode("")
				.status("A")
				.securityType("WAR")
				.externalKey1(5934516)
				.externalKey2(1234)
				.primaryExchange("THSET")
				.industryCode(149)
				.build();
		
		jdbcSec.upsertSecurity(securityItem);
		
	}
	
	*/
	

}
