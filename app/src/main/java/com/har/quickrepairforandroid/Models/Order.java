package com.har.quickrepairforandroid.Models;

import java.util.Date;

public class Order {

	private long mId;
	private String mType;
	private String mDate;
	private String mDetail;

	public Order(long id, String date, String type, String detail) {
		mId = id;
		mType = type;
		mDate = date;
		mDetail = detail;
	}

	public long id() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

	public String type() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

	public String date() {
		return mDate;
	}

	public void setDate(String date) {
		mDate = date;
	}

	public String detail() {
		return mDetail;
	}

	public void setDetail(String detail) {
		mDetail = detail;
	}
}
