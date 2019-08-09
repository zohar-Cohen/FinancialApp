package com.zoharc.configuration.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * The class loads the Integration FTP consts
 * 
 * @author Zohar Cohen Date : 08-Aug-2018
 */

@Component
public class IntegrationProperties {

	private final String host;
	private final int port;
	private final String userName;
	private final String password;
	private final String localDir;
	private final String remoteDir;
	private final String filter;


	/**
	 * C'tor, loades the properties from the application.properties file.
	 * The data is used to initialize the inBound Channel adapter.
	 * 
	 * @param host - FTP sever host name.
	 * @param port - FTP server port, default port is 21 (ftp).
	 * @param userName - FTP server user name
	 * @param password - FTP server password
	 * @param localDir - local directory where the ftpInbound channel will store the files locally.
	 * @param remoteDir - FTP remote directory where the integration poller will look for a new files.
	 * @param filter - regular expression pattern to validate the FTP files names.
	 */
	public IntegrationProperties(@Value("${corp.actions.host}") String host,
			                     @Value("${corp.actions.por:21}") int port,
			                     @Value("${corp.actions.username}") String userName,
			                     @Value("${corp.actions.password}") String password,
			                     @Value("${corp.actions.localdir}") String localDir,
			                     @Value("${corp.actions.remotefolder}") String remoteDir,
			                     @Value("${corp.actions.filter}") String filter) {

		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.localDir = localDir;
		this.remoteDir = remoteDir;
		this.filter = filter;
	}


	public String getHost() {
		return host;
	}


	public int getPort() {
		return port;
	}


	public String getUserName() {
		return userName;
	}


	public String getPassword() {
		return password;
	}


	public String getLocalDir() {
		return localDir;
	}


	public String getRemoteDir() {
		return remoteDir;
	}


	public String getFilter() {
		return filter;
	}
	
}



