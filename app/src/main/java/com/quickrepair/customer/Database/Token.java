package com.quickrepair.customer.Database;

public class Token {
	private String mTokenId;

	public Token() {
		mTokenId = "-1";
	}

	public Token(String tokenId) {
		mTokenId = tokenId;
	}

	public String getTokenId() {
		return mTokenId;
	}

	public void setTokenId(String tokenId) {
		mTokenId = tokenId;
	}
}
