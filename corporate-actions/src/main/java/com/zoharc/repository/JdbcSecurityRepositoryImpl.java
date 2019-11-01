package com.zoharc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zoharc.persistence.Security;

/**
 * The repository provides CRUD services to Security table, using JDBC Template/PreparedStatement queries
 * The repository used mostly in the massive update/migration as the JPA ItemWriter is slow.
 * 
 * @author Zohar Cohen
 * @version 1.0
 * @since 2019-09-22
 *
 */
@Repository
public class JdbcSecurityRepositoryImpl extends AbstractJdbcRepository<Security>{
	
	private static Logger LOG = LoggerFactory.getLogger(JdbcSecurityRepositoryImpl.class);

	private static final String sqlInsert = "INSERT INTO security (sys_creation_date, external_key1, external_key2, industry_code, primary_exchange,"
			                                                    +" security_type, status, uscode, isin) ";	
	private static final String sqlValuesInsert = "VALUES (now(), ?, ?, ?, ?, ?, ?, ?, ?) ";
	private static final String sqlUpdate = "UPDATE security set sys_update_date = now(), external_key1 = ?, external_key2 = ?, industry_code = ?, primary_exchange = ?, "
			                                     + " security_type = ?, status = ? , uscode = ? WHERE isin = ?";
	
	private static final String sqlSelect = "SELECT * FROM security WHERE isin = ?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	protected void insert(Security security) throws SQLIntegrityConstraintViolationException {
		LOG.info("Insert new security with isin: {}", security.getIsin());
		jdbcTemplate.update(sqlInsert + sqlValuesInsert, getJdbcQueryArgs(security), getJdbcQueryTypes());
		
	}
	
	@Override
	protected void update(Security security) {
		LOG.info("Update existing security with isin: {}", security.getIsin());		
		jdbcTemplate.update(sqlUpdate, getJdbcQueryArgs(security), getJdbcQueryTypes());
	}
	
	public Security selectByIsin(String isin) {
		LOG.info("Select security by isin: {}", isin);
		return jdbcTemplate.query(sqlSelect, new Object[] {isin}, this::mappEntity).get(0);
		
	}
	
	@Override
	protected Object[] getJdbcQueryArgs(Security security) {
		return new Object[] {security.getExternalKey1(),
				             security.getExternalKey2(),
				             security.getIndustryCode(),
				             security.getPrimaryExchange(),
				             security.getSecurityType(), 
				             security.getStatus(),
				             security.getUscode(),
				             security.getIsin()};
	}
	
	
	@Override
	protected int[] getJdbcQueryTypes() {
		
		return new int[] {Types.INTEGER, 
				          Types.INTEGER,
				          Types.INTEGER,
				          Types.VARCHAR,
				          Types.VARCHAR,
				          Types.VARCHAR,
				          Types.VARBINARY,
				          Types.VARCHAR};
	}
	@Override
	protected Security mappEntity(ResultSet rs, int rowNum) throws SQLException {
		return Security.builder().securitySeqNum(rs.getInt("security_seq_num")).isin(rs.getString("isin")).uscode(rs.getString("uscode"))
				                 .status(rs.getString("status")).securityType(rs.getString("security_type")).industryCode(rs.getInt("INDUSTRY_CODE"))
				                  .externalKey1(rs.getInt("external_key1")).externalKey2(rs.getInt("external_key2")).primaryExchange(rs.getString("primary_exchange")).build();
				
	}
	


 
}
