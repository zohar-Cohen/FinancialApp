package com.zoharc.batch.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchIntegrationConfiguration.class);
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;


	@Bean
	@StepScope
	FlatFileItemReader<String> itemReader(@Value("#{jobParameters[file_path]}") String filePath) {
		
		LOGGER.info("Recived a message from Job Luncher, will process the file on {} location", filePath);
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