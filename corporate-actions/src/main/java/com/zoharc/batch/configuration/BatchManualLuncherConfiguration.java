package com.zoharc.batch.configuration;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.persistence.EntityManagerFactory;

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
import org.springframework.batch.item.database.JpaItemWriter;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.zoharc.batch.processor.SecurityMasterJDBCProcessor;
import com.zoharc.batch.processor.SecurityMasterProcessor;
import com.zoharc.batch.reader.SecurityMasterDTO;
import com.zoharc.batch.reader.SecurityMasterLineMapper;
import com.zoharc.batch.writer.SecurityMasterJDBCWriter;
import com.zoharc.persistence.Security;

/**
 * @author Zohar Cohen
 */
@Configuration
public class BatchManualLuncherConfiguration implements ApplicationContextAware {
	
	private static final Logger LOG = LoggerFactory.getLogger(BatchManualLuncherConfiguration.class);
	
	private boolean multiThreadStep = false;

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
	
	@Autowired
	private EntityManagerFactory emf;

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
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setCorePoolSize(10);
		taskExecutor.setQueueCapacity(10);
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
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
	FlatFileItemReader<SecurityMasterDTO> manualItemReader(@Value("#{jobParameters[pathToFile]}") String filePath) {

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
		if(multiThreadStep) {
			itemReader.setSaveState(false);
		}
		return itemReader;
	}
	
	@Bean
	public SecurityMasterProcessor manualItemProcessor() {return new SecurityMasterProcessor();}
	
	@Bean
	public SecurityMasterJDBCProcessor jdbcItemProcessor() {return new SecurityMasterJDBCProcessor();}
	
    @Bean
    public SecurityMasterJDBCWriter jdbcItemWriter() {return new SecurityMasterJDBCWriter();}
	
	
	@Bean
	public JpaItemWriter<Security> manualItemWriter(){
		
		JpaItemWriter<Security> itemWriter = new JpaItemWriter<>();
		itemWriter.setEntityManagerFactory(emf);
		
		return itemWriter;
		
	}
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("byFileJob")
				.start(stepBuilderFactory.get("LOAD-SECURITY-MASTER")
						.<SecurityMasterDTO, Security>chunk(1000)
						.reader(manualItemReader(null))
						.processor(manualItemProcessor())
						.writer(manualItemWriter())
						.faultTolerant().skip(SQLIntegrityConstraintViolationException.class).skipLimit(10)
						//.writer(i -> i.stream().forEach(j -> System.out.println(j)))
						.build())
				.build();
	}
	
	
	@Bean
	public Job jdbcSecurityMasterJob() {
		this.multiThreadStep = true;
		return jobBuilderFactory.get("byFileJdbcJob")
				.start(stepBuilderFactory.get("LOAD-SECURITY-MASTER")
						.<SecurityMasterDTO, Security>chunk(1000)
						.reader(manualItemReader(null))
						.processor(jdbcItemProcessor())
						.writer(jdbcItemWriter())
						.taskExecutor(taskExecutor())
						.build())
				
				.build();
	}
	
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	
	
	
}