package com.zoharc.service;

import com.zoharc.persistence.Security;

public interface SecurityService {
	
	/**
	 * The method search for the security based using the uniqe identifiers
	 * @param isin - International Securities Identification Number
	 * @param cusip/us-code - Committee on Uniform Securities Identification (U.S and Canadian instruments)
	 * @param externalId
	 * @return
	 */
	public Security getSecurityByidentifiers(String isin, String cusip, int externalId);
	
	public void upsertSecurity(Security security);

}
