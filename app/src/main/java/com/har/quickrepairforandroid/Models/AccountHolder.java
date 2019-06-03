package com.har.quickrepairforandroid.Models;

public class AccountHolder {

	private String mAccount;
	private String mPassword;
	private boolean mCustomer = false;
	private boolean mLogin = false;

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
		return mCustomer;
	}

	public void setIsCustomer(boolean isCustomer) {
		mCustomer = isCustomer;
	}

	public boolean getIsLogin() {
		return mLogin;
	}

	public void setIsLogin(boolean login) {
		mLogin = login;
	}
}
