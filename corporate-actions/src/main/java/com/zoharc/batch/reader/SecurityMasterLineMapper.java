package com.zoharc.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class SecurityMasterLineMapper implements FieldSetMapper<SecurityMasterDTO>{

	private static final Logger LOG = LoggerFactory.getLogger(SecurityMasterLineMapper.class);

	protected final static String dateFormat = "yyyy/MM/dd";
	protected final static String dateTimeFormat = "yyyy/MM/dd HH:mm:ss";
	private final static int DEFAULT_INTEGER = 1234;

	@Override
	public SecurityMasterDTO mapFieldSet(FieldSet fieldSet) throws BindException {

		LOG.info("Reading a new line, line size {}", fieldSet.getFieldCount());
		int fieldSetCounter = 0;

		return SecurityMasterDTO.builder().scexhID(fieldSet.readInt(fieldSetCounter++))
				                          .sedolID(fieldSet.readInt(fieldSetCounter++))
				                          .actflag(fieldSet.readString(fieldSetCounter++))
				                          .changed(fieldSet.readString(fieldSetCounter++))
				                          .created(fieldSet.readString(fieldSetCounter++))
				                          .secID(fieldSet.readInt(fieldSetCounter++))
				                          .issID(fieldSet.readInt(fieldSetCounter++))
				                          .iSIN(fieldSet.readString(fieldSetCounter++))
				                          .uSCode(fieldSet.readString(fieldSetCounter++))
				                          .issuerName(fieldSet.readString(fieldSetCounter++))
				                          .cntryofIncorp(fieldSet.readString(fieldSetCounter++))
				                          .sIC(fieldSet.readString(fieldSetCounter++))
				                          .cIK(fieldSet.readString(fieldSetCounter++))
				           
				                          .indusID(getInteger(fieldSet,fieldSetCounter++))
				                          
				                          .sectyCD(fieldSet.readString(fieldSetCounter++))
				                          .securityDesc(fieldSet.readString(fieldSetCounter++))
				                          .parValue(fieldSet.readString(fieldSetCounter++))
				                          .pVCurrency(fieldSet.readString(fieldSetCounter++))
				                          .statusFlag(fieldSet.readString(fieldSetCounter++))
				                          .primaryExchgCD(fieldSet.readString(fieldSetCounter++))
				                          .sedol(fieldSet.readString(fieldSetCounter++))
				                          .sedolCurrency(fieldSet.readString(fieldSetCounter++))
				                          .defunct(fieldSet.readString(fieldSetCounter++))
				                          .sedolRegCntry(fieldSet.readString(fieldSetCounter++))
				                          .structCD(fieldSet.readString(fieldSetCounter++))
				                          .exchgCntry(fieldSet.readString(fieldSetCounter++))
				                          .exchgCD(fieldSet.readString(fieldSetCounter++))
				                          .mic(fieldSet.readString(fieldSetCounter++))
				                          .micseg(fieldSet.readString(fieldSetCounter++))
				                          .localCode(fieldSet.readString(fieldSetCounter++))
				                          .listStatus(fieldSet.readString(fieldSetCounter++))
				                          .build();

	}
	
/** we can filter it with Optional .filter(StringUtils::isNotEmpty)
	 * The method wrap spring FieldSet.readInt as in case the field is 
	 * 
	 * @param fs - FieldSet represents the line field set.
	 * @param key -The position of the requested field
	 * @return The requested field, in case the field is blank, the method will return default value
	 */
	public int getInteger(FieldSet fs, int key) {
		try {
			return fs.readInt(key);
			
		}catch(NumberFormatException e) {
			return DEFAULT_INTEGER;
		}
	}
	
	
}
