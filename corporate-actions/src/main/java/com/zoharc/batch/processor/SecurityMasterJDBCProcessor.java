package com.zoharc.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.zoharc.batch.reader.SecurityMasterDTO;
import com.zoharc.persistence.Security;
import com.zoharc.persistence.Stock;
import com.zoharc.util.DateUtil;
import com.zoharc.util.EventMapper;

/**
 * @author Zohar Cohen
 * @version 1.0
 * @since 2019-09-22
 *
 */
public class SecurityMasterJDBCProcessor implements ItemProcessor<SecurityMasterDTO, Security> {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityMasterJDBCProcessor.class);
	
	@Autowired
	private EventMapper eventMapper;
	@Override
	public Security process(SecurityMasterDTO item) throws Exception {
		
		LOG.info("process(): processing security record: isin-{}, raw symbol-{}", item.getISIN(), item.getLocalCode());
		
		//Build Security object;
		Security securityItem = Security.builder().isin(item.getISIN())
				                                  .uscode(item.getUSCode())
				                                  .status(item.getStatusFlag())
				                                  .securityType(item.getSectyCD())
				                                  .industryCode(item.getIndusID())
				                                  .externalKey1(item.getSecID())
				                                  .externalKey2(item.getIssID())
				                                  .primaryExchange(item.getPrimaryExchgCD())
				                                  .build();
		
		String mappedStockCode = eventMapper.mapStockSymbol(item.getLocalCode());
		String mappedExchangeCode = eventMapper.mapExchangeByExchangeKeys(item.getMic(), item.getMicseg(), item.getExchgCD());

		
		Stock stockItem = Stock.builder().mappedStock(mappedStockCode)
				                         .rawStock(item.getLocalCode())
				                         .exchangeCode(mappedExchangeCode)
				                         .stockName(item.getIssuerName())
				                         .stockCurrency(item.getSedolCurrency())
				                         .listStatus(item.getListStatus())
				                         .externalKey1(item.getScexhID())
				                         .externalKey2(item.getSedolID())
				                         .sedol(item.getSedol())
				                         .securityId(item.getISIN())
				                         .build();
		DateUtil.setStockListDates(stockItem, item.getListStatus(), item.getCreated(), item.getChanged());
		
		securityItem.addStock(stockItem);
		
		return securityItem;
	}

}
