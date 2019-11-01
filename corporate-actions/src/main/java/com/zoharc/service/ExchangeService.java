package com.zoharc.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoharc.persistence.Exchange;
import com.zoharc.repository.ExchangeRepository;

@Service
public class ExchangeService implements IExchangeService {

	private static final Logger LOG = LoggerFactory.getLogger(ExchangeService.class);
	@Autowired
	private ExchangeRepository exchangeRepository;
	@Value("${corp.actions.update.reference.data:false}")
	private boolean updateRefDate;


	@Override
	@Transactional(readOnly = true)
	public Exchange getExchangeByidentifiers(String rawExchangeCode) {

		LOG.info("getExchangeByExchangeCode(): Going to map exchange for the given raw exchange code: {}", rawExchangeCode);
		Optional<Exchange> exchangeObject = exchangeRepository.findById(rawExchangeCode);
		if(!exchangeObject.isPresent()) {
			LOG.warn("getExchangeByExchangeCode(): There is no reference data in the DataBase from the given exchange code: {}", rawExchangeCode);
			if(updateRefDate) {
				LOG.info("getExchangeByExchangeCode(): Update reference property set to true");
				return exchangeRepository.save(Exchange.builder().exchangeCode(rawExchangeCode).exchangeName("N/A - missing in reference data").build());
			}else {
				LOG.warn("getExchangeByExchangeCode(): Update reference property is false, getting default Exchange object (ABCD)");
				return exchangeRepository.findDefaultExchange();
			}
			
		}
		return  exchangeObject.get();

	}

}



