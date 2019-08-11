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
public class JobLaunchingController {

	//private static final Logger logger = LoggerFactory.getLogger(JobLaunchingController.class);

	@Autowired
	private JobOperator jobOperator;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void launch(@RequestParam("name") String name) throws Exception {
		this.jobOperator.start("job", String.format("name=%s", name));
	}

}





