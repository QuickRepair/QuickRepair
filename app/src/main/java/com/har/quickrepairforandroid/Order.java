package com.har.quickrepairforandroid;

import java.util.Date;

public class Order {

	private String mTitle;
	private Date mDate;

	public Order() {
		mTitle = "order_title";
		mDate = new Date();
	}

	public String title() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String date() {
		return mDate.toString();
	}

	public void setDate(Date date) {
		mDate = date;
	}
}
