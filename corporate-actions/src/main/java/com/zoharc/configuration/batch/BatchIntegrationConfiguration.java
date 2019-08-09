package com.zoharc.configuration.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class BatchIntegrationConfiguration {

	//private final static int LinesToSkip = 2;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	/*
	@Bean
	public FlatFileItemReader<CorporateActions> itemReader(){

		FlatFileItemReader<CorporateActions> itemReader = new FlatFileItemReader<>();
		itemReader.setLinesToSkip(LinesToSkip);
		itemReader.setComments(new String[] {"EDI_ENDOFFILE"});
		itemReader.setResource(new ClassPathResource("/data/20190503_690.txt"));

		DefaultLineMapper<CorporateActions> corporateActionsLineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
		tokenizer.setNames(CorporateActions.getFileHeader());

		corporateActionsLineMapper.setLineTokenizer(tokenizer);
		corporateActionsLineMapper.setFieldSetMapper(new CorporateActionsLineMapper());
		corporateActionsLineMapper.afterPropertiesSet();

		itemReader.setLineMapper(corporateActionsLineMapper);
		return itemReader;

	}


	@Bean
	public ValidatingItemProcessor<CorporateActions> itemValidator(){

		ValidatingItemProcessor<CorporateActions> corporateActionValidator = new ValidatingItemProcessor<>(new CorporateActionValidator(supportedEvents));
		corporateActionValidator.setFilter(true);
		return corporateActionValidator;
	}

	@Bean
	public CorporateActionProcessor itemProcessor() {return new CorporateActionProcessor();}

	@Bean
	public ItemWriter<CorporateActions> ItemWriter() {
		return items -> {
			for (CorporateActions item : items) {
				System.out.println(item.toString());
			}
		};
	}
	 */

	@Bean
	@StepScope
	FlatFileItemReader<String> itemReader(@Value("#{jobParameters[file_path]}") String filePath) {
		System.out.println("Item Reader: "+filePath);
		FlatFileItemReader<String> reader = new FlatFileItemReader<>();
		FileSystemResource fileResource = new FileSystemResource(filePath);


		reader.setResource(fileResource);
		reader.setLineMapper(new PassThroughLineMapper());
		try {
			reader.afterPropertiesSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reader;
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<String, String>chunk(10)
				.reader(itemReader(null))
				.writer(i -> i.stream().forEach(j -> System.out.println(j)))
				.build();
	}	


	@Bean(name = "corpActionsJob")
	public Job corpActionsJob() {
		return jobBuilderFactory.get("corpActionsJob")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}

}