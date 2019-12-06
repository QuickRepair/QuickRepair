package com.quickrepair.customer.NetworkConnection;

public enum StatuesCode {
	OK(200);

	private final int code;

	StatuesCode(int value) {
		code = value;
	}

	public int getCode() {
		return code;
	}
}
