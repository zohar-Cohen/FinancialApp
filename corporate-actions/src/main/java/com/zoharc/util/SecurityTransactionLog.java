package com.zoharc.util;
import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.PostLoad;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zoharc.persistence.Security;

@Component
public class SecurityTransactionLog {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityTransactionLog.class);

	private static String ExternalKeyChange = "External key %d changed, from: %d to: %d";
	private static String PrimaryExchangeChange = "Primary exchange changed, from: %s to: %s";
	private static String SecurityTypeChange = "Security type changed, from: %s to: %s";
	private static String StatusChange = "Status change, from: %s, to: %s";

	@PreUpdate
	public void preUpdateListner(Security security) {

		LOG.debug("preUpdateListner(): There is an update for the security object with isin: {}",security.getIsin());
		findSecurityChanges(security, security.getPreviousState());

	}

	@PostLoad
	public void postSecurityLoad(Security security) {

		security.setPreviousState(security);
	}


	private static void findSecurityChanges(Security security, Security previousSecurity) {

		StringJoiner stringJoiner = new StringJoiner("#");
		if(security.getExternalKey1() != previousSecurity.getExternalKey1()) {
			LOG.info("findSecurityChanges(): The external key 1 was updated");
			stringJoiner.add(String.format(ExternalKeyChange,1, security.getExternalKey1(), previousSecurity.getExternalKey1()));
		}
		if(security.getExternalKey2() != previousSecurity.getExternalKey2()) {
			LOG.info("findSecurityChanges(): The external key 2 was updated");
			stringJoiner.add(String.format(ExternalKeyChange,2, security.getExternalKey2(), previousSecurity.getExternalKey2()));
		}
		if(!Objects.equals(security.getPrimaryExchange(), previousSecurity.getPrimaryExchange())) {
			LOG.info("The primary exchange was updated");
			stringJoiner.add(String.format(PrimaryExchangeChange, security.getPrimaryExchange(), previousSecurity.getPrimaryExchange()));
		}
		if(!Objects.equals(security.getSecurityType(), previousSecurity.getSecurityType())) {
			LOG.info("The security type was updated");
			stringJoiner.add(String.format(SecurityTypeChange, security.getSecurityType(), previousSecurity.getSecurityType()));
		}
		

	}

}

