package com.zoharc.service;

import com.zoharc.persistence.Exchange;

public interface IExchangeService {
	
	public Exchange getExchangeByidentifiers(String rawExchangeCode);

}
