package com.har.quickrepairforandroid.Models;

public class Merchant {

	private String mName;

	public Merchant(String name) {
		mName = name;
	}

	public String name() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}
}
