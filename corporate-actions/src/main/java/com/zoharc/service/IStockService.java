package com.zoharc.service;

import com.zoharc.persistence.Stock;

public interface IStockService {

	/**
	 * The method provides the Stock object using unique keys, each security can has only one stock with the isin exchange code and currency combination.
	 * @param isin - International Securities Identification Number, uniquely identifies a specific securities issue.
	 * @param currencyCode - stock currency.
	 * @param exchangeCode - exchange identification code.
	 * @return unique stock object
	 */
	public Stock getStockByUniqeIdentifiers(String isin, String currencyCode, String exchangeCode);
	
}
