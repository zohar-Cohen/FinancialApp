package com.zoharc.configuration.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.zoharc.reader.SecurityMasterDTO;
import com.zoharc.reader.SecurityMasterLineMapper;

/**
 * @author Zohar Cohen
 */
@Configuration
public class BatchManualLuncherConfiguration implements ApplicationContextAware {
	
	private static final Logger LOG = LoggerFactory.getLogger(BatchManualLuncherConfiguration.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public JobExplorer jobExplorer;

	@Autowired
	public JobRepository jobRepository;

	@Autowired
	public JobRegistry jobRegistry;

	@Autowired
	public JobLauncher jobLauncher;

	private ApplicationContext applicationContext;

	@Bean
	public JobRegistryBeanPostProcessor jobRegistrar() throws Exception {
		JobRegistryBeanPostProcessor registrar = new JobRegistryBeanPostProcessor();

		registrar.setJobRegistry(this.jobRegistry);
		registrar.setBeanFactory(this.applicationContext.getAutowireCapableBeanFactory());
		registrar.afterPropertiesSet();

		return registrar;
	}

	@Bean
	public JobOperator jobOperator() throws Exception {
		SimpleJobOperator simpleJobOperator = new SimpleJobOperator();

		simpleJobOperator.setJobLauncher(this.jobLauncher);
		simpleJobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
		simpleJobOperator.setJobRepository(this.jobRepository);
		simpleJobOperator.setJobExplorer(this.jobExplorer);
		simpleJobOperator.setJobRegistry(this.jobRegistry);

		simpleJobOperator.afterPropertiesSet();

		return simpleJobOperator;
	}


	@Bean
	@StepScope
	FlatFileItemReader<SecurityMasterDTO> itemReader(@Value("#{jobParameters[pathToFile]}") String filePath) {

		LOG.info("ItemReader:: Going to process the follwing file: "+filePath);

		FlatFileItemReader<SecurityMasterDTO> itemReader = new FlatFileItemReader<>();
		FileSystemResource fileResource = new FileSystemResource(filePath);
		itemReader.setResource(fileResource);
		itemReader.setLinesToSkip(2);
		itemReader.setComments(new String[] {"EDI_ENDOFFILE"});

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
		tokenizer.setNames(SecurityMasterDTO.getFileHeader());

		DefaultLineMapper<SecurityMasterDTO> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(new SecurityMasterLineMapper());
		lineMapper.afterPropertiesSet();

		itemReader.setLineMapper(lineMapper);

		return itemReader;
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("byFileJob")
				.start(stepBuilderFactory.get("step1")
						.<SecurityMasterDTO, SecurityMasterDTO>chunk(10)
						.reader(itemReader(null))
						.writer(i -> i.stream().forEach(j -> System.out.println(j)))
						.build())
				.build();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}