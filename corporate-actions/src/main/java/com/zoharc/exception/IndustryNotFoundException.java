package com.zoharc.exception;

/**
* @author Zohar Cohen Date: 27-Aug-2019
 *
 */
public class IndustryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String errorMsg = "The industry is not presented in the DB: ";


 public IndustryNotFoundException(int industryCode) {
		super(errorMsg+industryCode);
	}
}
