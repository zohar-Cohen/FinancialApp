package com.zoharc.enums;

import java.util.HashMap;
import java.util.Map;



/**
 * @author Zohar Cohen Date: 21-Sep-2019
 * The enum contains the available stock status, indicates 
 * whether a security is Listed on an Exchange or Unlisted Indicates Exchange Listing Status
 *
 * */
public enum ListStatus {
	D("Delisted"), 
	L("Listed"), 
	N("New Listing"),
	R("Resumed"), 
	S("Suspend"); 

	private String listStatus;

	private ListStatus(String listStatus) {

		this.listStatus = listStatus;
	}

	public String getListStatus() {

		return listStatus;
	}
	// ****** Reverse Lookup Implementation************//

	private static final Map<String, ListStatus> lookup = new HashMap<>();

	static {

		for (ListStatus listStatus : ListStatus.values()) {

			lookup.put(listStatus.getListStatus(), listStatus);
		}

	}

	public static ListStatus lookup(String listStatus) {

		return lookup.get(listStatus);
	}
}
