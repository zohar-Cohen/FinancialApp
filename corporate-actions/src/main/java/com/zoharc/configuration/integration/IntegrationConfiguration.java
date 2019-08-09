package com.zoharc.configuration.integration;


import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.ftp.filters.FtpRegexPatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizingMessageSource;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.handler.LoggingHandler;


/**
 * The class define the configuration for spring FTP integration.
 * 
 * @author Zohar Cohen Date : 08-Aug-2018
 *
 */
@Configuration
public class IntegrationConfiguration {


	@Autowired @Qualifier("corpActionsJob")
	private Job corpActionsJob;

	@Autowired
	private JobLaunchingGateway jobLaunchingGateway;

	@Autowired
	private IntegrationProperties properties;

	@Bean
	public JobLaunchingGateway launchingGateway(JobLauncher jobLauncher) {
		return new JobLaunchingGateway(jobLauncher);
	}

	@Bean
	public DefaultFtpSessionFactory ftpSessionFactory() {
		DefaultFtpSessionFactory defaultFtpSessionFactory = new DefaultFtpSessionFactory();
		defaultFtpSessionFactory.setHost(properties.getHost());
		defaultFtpSessionFactory.setPort(properties.getPort());
		defaultFtpSessionFactory.setUsername(properties.getUserName());
		defaultFtpSessionFactory.setPassword(properties.getPassword());
		defaultFtpSessionFactory.setControlEncoding("UTF-8");
		defaultFtpSessionFactory.setClientMode(FTPClient.ACTIVE_LOCAL_DATA_CONNECTION_MODE);

		return defaultFtpSessionFactory;

	}

	@Bean
	public FtpInboundFileSynchronizingMessageSource ftpMessageSource() {

		final Pattern pattern = Pattern.compile(properties.getFilter());
		FtpRegexPatternFileListFilter ftpRegexPatternFileListFilter = new FtpRegexPatternFileListFilter(pattern);

		FtpInboundFileSynchronizer fileSynchronizer = new FtpInboundFileSynchronizer(ftpSessionFactory());
		fileSynchronizer.setDeleteRemoteFiles(false);
		fileSynchronizer.setRemoteDirectory(properties.getRemoteDir());
		fileSynchronizer.setFilter(ftpRegexPatternFileListFilter);

		FtpInboundFileSynchronizingMessageSource ftpMessageSource = new FtpInboundFileSynchronizingMessageSource(fileSynchronizer);
		ftpMessageSource.setLocalDirectory(new File(properties.getLocalDir()));

		return ftpMessageSource;
	}

	@Bean
	public FileMessageToJobLaunchRequest fileMessageToJobLaunchRequest() {
		FileMessageToJobLaunchRequest transformer = new FileMessageToJobLaunchRequest();
		transformer.setJob(this.corpActionsJob);
		transformer.setFileParameterName("file_path");

		return transformer;
	}

	@Bean
	public IntegrationFlow triggerCorpActionFlow() {
		return IntegrationFlows.from(ftpMessageSource(),
				c -> c.poller(Pollers.fixedRate(5000, 2000)))
				.transform(fileMessageToJobLaunchRequest())
				.handle(jobLaunchingGateway)
				.handle(new LoggingHandler("INFO"))
				.get();
	}

}
