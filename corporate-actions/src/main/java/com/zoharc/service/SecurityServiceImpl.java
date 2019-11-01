package com.zoharc.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoharc.persistence.Security;
import com.zoharc.repository.SecurityRepository;

@Service
public class SecurityServiceImpl implements SecurityService {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityServiceImpl.class);

	@Autowired
	private SecurityRepository securityRepository;
	
	 

	@Override
	@Transactional(readOnly = true)
	public Security getSecurityByidentifiers(String isin, String cusip, int externalId) {

		LOG.info("getSecurityByidentifiers(): Going to find security for the given identifies: isin-{}, uscode-{}, externalId-{}", isin, cusip, externalId);
		if(!StringUtils.isEmpty(isin)) {

			Optional<Security> optionalSecurity = securityRepository.findSecurityObjByIsin(isin);
			if(optionalSecurity.isPresent()) {
				return optionalSecurity.get();
			}
		}
		if(!StringUtils.isEmpty(cusip)) {
			Optional<Security> optionalSecurity = securityRepository.findSecurityObjByUsCode(cusip);
			if(optionalSecurity.isPresent()) {
				return optionalSecurity.get();
			}

		}
		if(externalId != 0) {
			Optional<Security> optionalSecurity = securityRepository.findSecurityObjByExternalId(externalId);
			if(optionalSecurity.isPresent()) {
				return optionalSecurity.get();
			}

		}
		return null;
	}

	
	public boolean upsertSecurityItem(Security security) {
		
		LOG.info("upsertSecurityItem(): Going to upsert security record, isin: {}", security.getIsin());
	
		return true;
		
		
	}


	@Override
	public void upsertSecurity(Security security) {
		// TODO Auto-generated method stub
		
	}

}




