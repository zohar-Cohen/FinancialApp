package com.zoharc.repository;

import static org.junit.Assert.assertEquals;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.zoharc.persistence.Stock;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfiguration.class)
@ActiveProfiles("test")
public class JdbcStockRepositoryImplTest {

	@Autowired
	private JdbcStockRepositoryImpl jdbcStockRepositoryImplTest;
	
	private static Stock stock = Stock.builder().mappedStock("TestStock")
                                                .rawStock("Test.Stock")
                                                .exchangeCode("ILTSE")
                                                .stockName("Test Stock Name")
                                                .stockCurrency("NIS")
                                                .externalKey1(123)
                                                .externalKey2(456)
                                                .securityId("IL0026400684")
                                                .listStatus("L")
                                                .sedol("ABC")
                                                .primaryExchange("Y").build();

	/**
	 * Test - create new stock entity in the H2 database and select it using the PK
	 * @results - The stock entity exists 
	 */
	@Test
	@Sql(scripts = "/scripts/create-stock-table-h2.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void upsertTest() {
		
		jdbcStockRepositoryImplTest.upsert(stock);
		Stock dbStock = jdbcStockRepositoryImplTest.selectByPK(stock.getMappedStock(), stock.getExchangeCode(), stock.getStockCurrency(), stock.getSecurityId());
		
		assertEquals(stock.getMappedStock(), dbStock.getMappedStock());
		assertEquals(stock.getRawStock(), dbStock.getRawStock());
		assertEquals(stock.getExchangeCode(), dbStock.getExchangeCode());
		assertEquals(stock.getStockName(), dbStock.getStockName());
		assertEquals(stock.getStockCurrency(), dbStock.getStockCurrency());
		assertEquals(stock.getExternalKey1(), dbStock.getExternalKey1());
		assertEquals(stock.getExternalKey2(), dbStock.getExternalKey2());
		assertEquals(stock.getSecurityId(), dbStock.getSecurityId());
		assertEquals(stock.getListStatus(), dbStock.getListStatus());
		assertEquals(stock.getSedol(), dbStock.getSedol());
		assertEquals(stock.getPrimaryExchange(), dbStock.getPrimaryExchange());

	}
	
	/**
	 *  Test - update the current stock name with a new name
	 *  @results - the stock name is updated successfully.
	 */
	@Test
	public void updateTest() {
		
		String newStockName = "Test Stock Name for update";
		stock.setStockName(newStockName);
		jdbcStockRepositoryImplTest.update(stock);
		
		assertEquals(newStockName, jdbcStockRepositoryImplTest.selectByPK(stock.getMappedStock(), stock.getExchangeCode(), stock.getStockCurrency(), stock.getSecurityId()).getStockName());
	}
	
	
	/** 
	 * Test - insert a record that already exists in the H2 DB
	 * @results - the method throws DuplicateKeyException as the stock entity already exists
	 * @throws SQLIntegrityConstraintViolationException
	 */
	@Test(expected = DuplicateKeyException.class)
	public void insertTest() throws SQLIntegrityConstraintViolationException {
		jdbcStockRepositoryImplTest.insert(stock);
		
	}
	
	/**
	 *  Test - upsert a new record in the H2 DB, and return a list of items with the same security id.
	 *  @result - the method returns a list of two stocks with the same security id 
	 */
	@Test
	public void selectBySecurityIdTest() {
		
		stock.setExchangeCode("IQISX");
		stock.setMappedStock("TestStock2");
		jdbcStockRepositoryImplTest.upsert(stock);
		
		List<Stock> stockList = jdbcStockRepositoryImplTest.selectBySecurityId(stock.getSecurityId());
		
        assertEquals(2, stockList.size());		
		
	}
	
	
	
}
