package com.zoharc.configuration.integration;

import java.io.File;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.messaging.Message;

public class FileMessageToJobLaunchRequest {

	private Job corpActionsJob;

	private String fileParameterName = "input.file.name";

	public void setFileParameterName(String fileParameterName) {
		this.fileParameterName = fileParameterName;
	}

	public void setJob(Job corpActionsJob) {
		this.corpActionsJob = corpActionsJob;
	}

	public JobLaunchRequest toRequest(Message<File> message) {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

		jobParametersBuilder.addString(fileParameterName, message.getPayload().getAbsolutePath());
		// We add a dummy value to make job params unique, or else spring batch
		// will only run it the first time
		jobParametersBuilder.addDate("dummy", new Date());
		return new JobLaunchRequest(corpActionsJob, jobParametersBuilder.toJobParameters());
	}

}
