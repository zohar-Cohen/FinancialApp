package com.zoharc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.zoharc.persistence.Security;
import com.zoharc.repository.JdbcSecurityRepositoryImpl;
import com.zoharc.repository.JdbcStockRepositoryImpl;


/**
 * @author Zohar Cohen
 * @version 1.0
 * @since 2019-10-25
 */
@Service
public class InstrumentFacade {

	private static final Logger LOG = LoggerFactory.getLogger(InstrumentFacade.class);
	
	@Autowired
	private JdbcSecurityRepositoryImpl securityRepositoryJdbc;
	
	@Autowired
	private JdbcStockRepositoryImpl stockRepositoryJdbc;
	
	
	//@Transactional
	@Retryable(maxAttempts = 4, backoff = @Backoff(delay = 500))
	public void upsertInstrumentJdbc(Security security) {
		
		LOG.info("Going to upsert the instrument information (security and stock");
		securityRepositoryJdbc.upsert(security);
		security.getStocks().forEach(stockRepositoryJdbc::upsert);
		
		LOG.info("Security object processed sucsessfully, isin: {}", security.getIsin());
		LOG.info("Stock object processed sucsessfully, size: {}",security.getStocks().size() );
		
	}

	
	

}
