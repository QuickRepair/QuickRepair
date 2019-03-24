package com.har.quickrepairforandroid.Models;

public class AccountHolder {

	private String mAccount;
	private String mPassword;
	private boolean mIsCustomer = false;
	private boolean mIsLogin = false;

	private static final AccountHolder ourInstance = new AccountHolder();

	public static AccountHolder getInstance() {
		return ourInstance;
	}

	private AccountHolder() {
	}

	public String getAccount() {
		return mAccount;
	}

	public void setAccount(String account) {
		mAccount = account;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	public boolean getIsCustomer() {
		return mIsCustomer;
	}

	public void setIsCustomer(boolean isCustomer) {
		mIsCustomer = isCustomer;
	}

	public boolean getIsLogin() {
		return mIsLogin;
	}

	public void setIsLogin(boolean login) {
		mIsLogin = login;
	}
}
