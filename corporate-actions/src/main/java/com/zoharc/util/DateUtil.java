package com.zoharc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoharc.enums.ListStatus;
import com.zoharc.persistence.Stock;

/**
 * @author Zohar Cohen Date: 21-Sep-2019 Date Utility to convert date string to
 *         Date object
 *
 */
public class DateUtil {

	private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);
	private static final String DateTimeFormat = "yyyy/MM/dd hh:mm:ss";
	private static final String DateFormat = "yyyy/MM/dd";

	/**
	 * The method convert date string to date object, in case the input contains
	 * unexpected format/empty/null the method will return null;
	 * 
	 * @param dateStr - Date String
	 * @return - Date object
	 */
	public static Date getDateByString(String dateStr) {

		SimpleDateFormat dateFormat = null;
		Date date = null;

		try {
			if (dateStr.length() == DateTimeFormat.length()) {
				dateFormat = new SimpleDateFormat(DateTimeFormat);
			} else {
				dateFormat = new SimpleDateFormat(DateFormat);
			}
			Objects.requireNonNull(dateStr);
			date = dateFormat.parse(dateStr);
		} catch (Exception e) {
			if (e instanceof ParseException) {
				LOG.warn("getDateByString(): Unparseble date String");
			} else {
				LOG.warn("getDateByString(): The data String input is null, skip...");
			}
		}
		return date;
	}

	public static void setStockListDates(Stock stockItem, String listStatusStr, String createdDate, String changedDate) {

		try {
			ListStatus listStatus = ListStatus.valueOf(listStatusStr);
			LOG.info("Stock's list status is: {}", listStatus.getListStatus());
			switch (listStatus) {
			case D:
				stockItem.setExpireDate(DateUtil.getDateByString(changedDate));
				stockItem.setListedDate(DateUtil.getDateByString(createdDate));
				break;
			case N:
				stockItem.setListedDate(DateUtil.getDateByString(changedDate));
				break;
			case R:
			case S:
			case L:
				stockItem.setListedDate(DateUtil.getDateByString(createdDate));
				break;
			}

		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				LOG.warn("The provided list status is no suported, setting it as null");
			} else {
				LOG.warn("The provided list status is null");
			}
		}

	}

}
