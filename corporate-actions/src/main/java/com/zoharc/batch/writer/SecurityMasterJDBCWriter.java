package com.zoharc.batch.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.zoharc.persistence.Security;
import com.zoharc.service.InstrumentFacade;

/**
 * @author Zohar Cohen
 * @version 1.0
 * @since 2019-09-22
 *
 */
public class SecurityMasterJDBCWriter implements ItemWriter<Security> {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityMasterJDBCWriter.class);
	
	@Autowired
	private InstrumentFacade instrumentFacade;
	
	@Override
	public void write(List<? extends Security> items) throws Exception {
		
		LOG.debug("write(): {} items in the write bucket, looping", items.size());
		items.forEach(instrumentFacade::upsertInstrumentJdbc);
	}
}
