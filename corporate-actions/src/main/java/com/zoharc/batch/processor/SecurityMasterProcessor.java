package com.zoharc.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.zoharc.batch.reader.SecurityMasterDTO;
import com.zoharc.persistence.Security;
import com.zoharc.persistence.Stock;
import com.zoharc.persistence.StockPK;
import com.zoharc.service.IExchangeService;
import com.zoharc.service.IIndustryService;
import com.zoharc.util.DateUtil;
import com.zoharc.util.EventMapper;

public class SecurityMasterProcessor implements ItemProcessor<SecurityMasterDTO, Security> {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityMasterProcessor.class);

	@Autowired
	private EventMapper eventMapper;
	@Autowired
	private IIndustryService industryService;
	@Autowired
	private IExchangeService exchangeService;

	@Override
	public Security process(SecurityMasterDTO item) throws Exception {

		LOG.info("process(): processing security record: isin-{}, raw symbol-{}", item.getISIN(), item.getLocalCode());

		// Build the security object from the reader input
		Security securityItem = Security.builder().isin(item.getISIN())
				                                  .uscode(item.getUSCode())
				                                  .status(item.getStatusFlag())
				                                  .securityType(item.getSectyCD())
				                                  .externalKey1(item.getSecID())
				                                  .externalKey2(item.getIssID())
				                                  .primaryExchange(item.getPrimaryExchgCD())
				                                  .industry(industryService.getIndustryById(item.getIndusID())).build();
		
		String mappedStockCode = eventMapper.mapStockSymbol(item.getLocalCode());
		String mappedExchangeCode = eventMapper.mapExchangeByExchangeKeys(item.getMic(), item.getMicseg(), item.getExchgCD());

		LOG.info("process(): Building stock object for the with the mapped stock code {} threaded under {} exchange", mappedStockCode, mappedExchangeCode);

		// Build the stock object from the reader input
		StockPK primaryKey = StockPK.builder().mappedStock(mappedStockCode).exchangeCode(mappedExchangeCode)
				.stockCurrency(item.getSedolCurrency()).securityId(item.getISIN()).build();

		Stock stockItem = Stock.builder().rawStock(item.getLocalCode()).stockName(item.getIssuerName())
				                          .listStatus(item.getListStatus()).exchange(exchangeService.getExchangeByidentifiers(mappedExchangeCode))
				.primaryExchange((mappedExchangeCode == item.getPrimaryExchgCD()) ? "Y" : "N")
				.externalKey1(item.getScexhID()).externalKey2(item.getSedolID()).sedol(item.getSedol()).id(primaryKey)
				.build();

		DateUtil.setStockListDates(stockItem, item.getListStatus(), item.getCreated(), item.getChanged());
		stockItem.setSecurity(securityItem);
		securityItem.getStocks().add(stockItem);

		return securityItem;
	}


}
