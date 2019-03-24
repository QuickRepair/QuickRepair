package com.har.quickrepairforandroid.Models;

public class ApplianceType {

	private String mName;

	public ApplianceType(String name) {
		mName = name;
	}

	public String name() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}
}
