package com.zoharc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zohar Cohen Date : 08-Aug-2018
 * The class provides an option to invoke a batch job using a rest controller invocation.
 *
 */

@RestController
@RequestMapping("/lunch")
public class JobLaunchingController {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobLaunchingController.class);

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired //@Qualifier(value = )
	private Job manualJobRunner;

	@RequestMapping(value = "/manual", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void lunchLocalInput(@RequestParam("filePath") String filePath) {

		LOGGER.info("lunchLocalInput: Going to start a batch job, to process the file: "+filePath);
		JobParameters jobParameters = new JobParametersBuilder().addString("filePath", filePath).toJobParameters();
		try {
			this.jobLauncher.run(manualJobRunner, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException| JobParametersInvalidException e) {
			LOGGER.error("lunchLocalInput method: Unable to invoke the job due to the follwing exception: {}",e);
		}


	}

}





