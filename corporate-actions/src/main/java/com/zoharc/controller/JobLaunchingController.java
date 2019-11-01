package com.zoharc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobOperator;
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

	private static final Logger LOG = LoggerFactory.getLogger(JobLaunchingController.class);

	@Autowired
	private JobOperator jobOperator;

	@RequestMapping(value = "/byFile", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void launch(@RequestParam("pathToFile") String filePath) throws Exception {

		LOG.info("Rest Controller, recived a call for a manual request to process the follwing file: {}", filePath);
		this.jobOperator.start("byFileJob", String.format("pathToFile=%s", filePath));
	}


	@RequestMapping(value = "/byFileJdbc", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void lunchJdbcJob(@RequestParam("pathToFile") String filePath) throws Exception{

		LOG.info("Rest Controller, recived a call for a manual request to process the follwing file: {}", filePath);
		this.jobOperator.start("byFileJdbcJob", String.format("pathToFile=%s", filePath));
	}

}









