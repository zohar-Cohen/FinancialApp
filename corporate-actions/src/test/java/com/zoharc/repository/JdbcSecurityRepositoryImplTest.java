package com.zoharc.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLIntegrityConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.zoharc.persistence.Security;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfiguration.class)
@ActiveProfiles("test")
public class JdbcSecurityRepositoryImplTest {

	@Autowired
	private JdbcSecurityRepositoryImpl jdbcSecurityRepositoryImplTest;

	private static final Security security  = Security.builder().isin("US02079K1079")
			//.uscode("02079K107") -- removed due to exception in H2 
			.status("A")
			.securityType("ETF")
			.industryCode(163)
			.externalKey1(1)
			.externalKey2(2)
			.primaryExchange("NZBND").build();
	@Test
	@Sql(scripts = "/scripts/create-security-table-h2.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void upsertTest() {

		jdbcSecurityRepositoryImplTest.upsert(security);
		Security dbSecurity = jdbcSecurityRepositoryImplTest.selectByIsin(security.getIsin());

		assertNotNull(dbSecurity.getSecuritySeqNum());
		assertEquals(security.getIsin(), dbSecurity.getIsin());
		assertEquals(security.getUscode(), dbSecurity.getUscode());
		assertEquals(security.getStatus(), dbSecurity.getStatus());
		assertEquals(security.getSecurityType(), dbSecurity.getSecurityType());
		assertEquals(security.getIndustryCode(), dbSecurity.getIndustryCode());
		assertEquals(security.getExternalKey1(), dbSecurity.getExternalKey1());
		assertEquals(security.getExternalKey2(), dbSecurity.getExternalKey2());
		assertEquals(security.getPrimaryExchange(), dbSecurity.getPrimaryExchange());

	}
	
	@Test
	public void updateTest() {
		
		String securityType = "NTS";
		security.setSecurityType(securityType);
		jdbcSecurityRepositoryImplTest.update(security);
		
		assertEquals(securityType, jdbcSecurityRepositoryImplTest.selectByIsin(security.getIsin()).getSecurityType());
	}
	
	
	/** 
	 * Test - insert a record that already exists in the H2 DB
	 * @results - the method throws DuplicateKeyException as the stock entity already exists
	 * @throws SQLIntegrityConstraintViolationException
	 */
	@Test(expected = DuplicateKeyException.class)
	public void insertTest() throws SQLIntegrityConstraintViolationException {
		jdbcSecurityRepositoryImplTest.insert(security);
		
	}
	
}

