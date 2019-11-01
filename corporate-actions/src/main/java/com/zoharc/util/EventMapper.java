package com.zoharc.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zoharc.repository.ExchangeRepository;

@Component
public class EventMapper {

	private static final Logger LOG = LoggerFactory.getLogger(EventMapper.class);
	private Map<String,String> marketExchangeMapper;

	@Autowired
	private ExchangeRepository exchangeRepository;

	/**
	 * The method initialize the exchange market code map.
	 */
	@PostConstruct
	public void initialize() {

		LOG.info("Initializinh market code exchane mapper");
		Map<String,String> map = new HashMap<>();
		Optional<List<String[]>> marketCodes = exchangeRepository.findAllMarketCodes();
		if(!marketCodes.isPresent()) {
			LOG.warn("There are no exchanges with market code, please check the referance data implementation.");
			throw new EntityNotFoundException();
		}
		LOG.info("There are {} exchanges with market codes", marketCodes.get().size());
		marketCodes.get().forEach(elementArr -> {
			map.put(elementArr[0], elementArr[1]);
		});
		marketExchangeMapper = Collections.unmodifiableMap(map);
	}


	/**
	 * @deprecated use {@link #mapExchangeByExchangeKeys()} instead. 
	 * The method return exchange code for given market code, the method will throw exception once there is no exchange 
	 * @param marketCode - standard Market Identification Code	
	 * @return exchange code
	 */
	@Deprecated
	public String mapExchangeByMarketCode(String marketCode) {
		Objects.requireNonNull(marketCode, "The given market code is null");
		LOG.info("mapExchangeByMarketCode(): Searching for exchange code for the given market code: {}", marketCode);
		return marketExchangeMapper.computeIfAbsent(marketCode, v -> {throw new IllegalArgumentException("Unsupported market code");});
	}


	/**
	 * The method apply internal symbology pattern.
	 * TODO - This method should be enhance to add exchange suffix, in order to have unique stock symbol. 
	 * @param rawSymbol - raw stock symbol/ticker
	 * @return - internal stock symbol
	 */
	public String mapStockSymbol(String rawSymbol) {

		LOG.info("mapStockSymbol(): Going to apply symbology on the ray symbol: {}", rawSymbol);
		return rawSymbol.replace(' ', '.');
	}

	/**
	 * The method search for the application internal exchange code.
	 * First it tries to map using the market code -> market group code -> raw exchange code
	 * @param marketCode - standard Market Identification Code
	 * @param marketGroupCode - standard Market group Identification Code
	 * @param rawExchangeCode - raw exchange code, without system symbology pattern.
	 * @return - internal exchange code
	 */
	public String mapExchangeByExchangeKeys(String marketCode, String marketGroupCode, String rawExchangeCode) {

		LOG.info("mapExchangeByExchangeKeys(): Going to map exchange for the given raw exchange identifiers, marketCode: {}, marketGroupCode: {}, rawExchangeCode: {}");
		String exchangeCode = null;
		if(!StringUtils.isEmpty(marketCode)) {
			LOG.info("mapExchangeByExchangeKeys(): Mapping the exchange code using market code");
			exchangeCode = marketExchangeMapper.get(marketCode);
		}else if(exchangeCode == null && !StringUtils.isEmpty(marketGroupCode)) {
			LOG.info("mapExchangeByExchangeKeys(): Mapping the exchange code using market group code");
			exchangeCode = marketExchangeMapper.get(marketGroupCode);
		}else if(exchangeCode == null && !StringUtils.isEmpty(rawExchangeCode)) {
			LOG.info("mapExchangeByExchangeKeys(): Mapping the exchange code using exchange raw code");
			exchangeCode = rawExchangeCode;
		}
		return exchangeCode;

	}

}
