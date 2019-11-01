package com.zoharc.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoharc.persistence.Industry;
import com.zoharc.repository.IndustryRepository;

@Service
public class IndustryServiceImpl implements IIndustryService {

	private static final Logger LOG = LoggerFactory.getLogger(IndustryServiceImpl.class);

	@Autowired
	private IndustryRepository IndustryRepository;
	@Value("${corp.actions.update.reference.data:false}")
	private boolean updateRefDate;


	@Override
	@Transactional
	public Industry getIndustryById(int industryCode) {

		LOG.info("getIndustryById(): Going to find industry object in the DataBase for in industryCode: {}", industryCode);
		Optional<Industry> industryObject = IndustryRepository.findById(industryCode);
		if(!industryObject.isPresent()) {

			LOG.warn("getIndustryById(): There is no reference data in the DataBase from the given industry code: {}", industryCode);
			if(updateRefDate) {
				LOG.info("getIndustryById(): Update reference property set to true");
				return IndustryRepository.save(Industry.builder().industryCode(industryCode).industryDesc("N/A - Missing in the implementation").build());
			}else {
				LOG.warn("getIndustryById(): Update reference property is false, getting default industry object (1234)");
				return IndustryRepository.findDefaultIndustry();

			}
		}
		return industryObject.get();

	}

}
