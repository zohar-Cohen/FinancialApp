package com.zoharc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zoharc.persistence.Stock;

/**
 * @author Zohar Cohen
 * @version 1.0
 * @since 2019-09-23
 *
 */
@Repository
public class JdbcStockRepositoryImpl extends AbstractJdbcRepository<Stock>{
	
	private static Logger LOG = LoggerFactory.getLogger(JdbcStockRepositoryImpl.class);
	
	private static final String sqlInsert = "INSERT INTO stock (sys_creation_date, raw_stock, stock_name, list_status, listed_date, expire_date, external_key1, "
			                                                  + "external_key2, sedol, primary_exchange, mapped_stock, exchange_code, stock_currency, security_id) ";
	private static final String sqlValuesInsert = "VALUES (now(),?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String sqlUpdate = "UPDATE stock SET sys_update_date = now(), raw_stock =?, stock_name = ? , list_status = ?, listed_date = ?, expire_date = ? , external_key1 = ?, "
			                                              + " external_key2 = ?, sedol = ?, primary_exchange = ? WHERE mapped_stock = ? AND exchange_code = ? AND stock_currency = ? "
			                                              + "AND security_id =? ";
	
	private static final String sqlSelectByPK = "SELECT * FROM stock WHERE mapped_stock = ? AND exchange_code = ? AND stock_currency = ? AND security_id =?";
	
	private static final String sqlSelectBySecurityId = "SELECT * FROM stock WHERE security_id =?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	protected void insert(Stock stock) throws SQLIntegrityConstraintViolationException {
		LOG.info("Insert new stock with PK, mappedStock:{}, exchangeCode:{}, stockCurrency:{}, securityId/isin:{}",
				                                           stock.getMappedStock(), stock.getExchangeCode(), stock.getStockCurrency(), stock.getSecurityId());
		jdbcTemplate.update(sqlInsert + sqlValuesInsert, getJdbcQueryArgs(stock), getJdbcQueryTypes());

	}

	@Override
	protected void update(Stock stock) {
		LOG.info("Update existing stock with PK, mappedStock:{}, exchangeCode:{}, stockCurrency:{}, securityId/isin:{}",
				stock.getMappedStock(), stock.getExchangeCode(), stock.getStockCurrency(), stock.getSecurityId());
		jdbcTemplate.update(sqlUpdate, getJdbcQueryArgs(stock), getJdbcQueryTypes());
	}
	
	public Stock selectByPK(String mappedStock, String exchangeCode, String stockCurrency, String securityId) {
		LOG.info("Select a stock by PK, mappedStock:{}, exchangeCode:{}, stockCurrency:{}, securityId/isin:{}",
				mappedStock, exchangeCode, stockCurrency, securityId);

		return jdbcTemplate.queryForObject(sqlSelectByPK,
				new Object[] { mappedStock, exchangeCode, stockCurrency, securityId }, this::mappEntity); 
	}
	
	public List<Stock> selectBySecurityId(String securityId){
		
		return jdbcTemplate.query(sqlSelectBySecurityId, new Object[] {securityId}, this::mappEntity);
		
	}
	

	@Override
	protected Object[] getJdbcQueryArgs(Stock stock) {
		return new Object[] { stock.getRawStock(),
				              stock.getStockName(), 
				              stock.getListStatus(), 
				              stock.getListedDate(),
				              stock.getExpireDate(), 
				              stock.getExternalKey1(), 
				              stock.getExternalKey2(), 
				              stock.getSedol(),
				              stock.getPrimaryExchange(), 
				              stock.getMappedStock(), 
				              stock.getExchangeCode(), 
				              stock.getStockCurrency(),
				              stock.getSecurityId() };
	}

	@Override
	protected int[] getJdbcQueryTypes() {

		return new int[] { Types.VARCHAR, 
				           Types.VARCHAR, 
				           Types.CHAR, 
				           Types.DATE, 
				           Types.DATE, 
				           Types.INTEGER,
				           Types.INTEGER, 
				           Types.VARCHAR, 
				           Types.CHAR, 
				           Types.VARCHAR, 
				           Types.VARCHAR, 
				           Types.VARCHAR,
				           Types.VARCHAR}
		;
	}

	@Override
	protected Stock mappEntity(ResultSet rs, int rowNum) throws SQLException {
		
		return Stock.builder().stockSeqNum(rs.getInt("stock_seq_num")).mappedStock(rs.getString("mapped_stock"))
				.rawStock(rs.getString("raw_stock")).exchangeCode(rs.getString("exchange_code"))
				.stockName(rs.getString("stock_name")).stockCurrency(rs.getString("stock_currency"))
				.listStatus(rs.getString("list_status")).listedDate(rs.getDate("listed_date"))
				.expireDate(rs.getDate("expire_date")).externalKey1(rs.getInt("external_key1"))
				.externalKey2(rs.getInt("external_key2")).securityId(rs.getString("security_id"))
				.sedol(rs.getString("sedol")).primaryExchange(rs.getString("primary_exchange")).build();
	}

	
}
