package com.har.quickrepairforandroid.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.har.quickrepairforandroid.Database.AccountCursorWrapper;
import com.har.quickrepairforandroid.Database.Schema;

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

	public void updateAccount(SQLiteDatabase database) {
		ContentValues values = new ContentValues();
		values.put(Schema.Table.Cols.ACCOUNT, getAccount());
		values.put(Schema.Table.Cols.PASSWORD, getPassword());
		values.put(Schema.Table.Cols.ACCOUNT_TYPE, getIsCustomer() ? "customer" : "merchant");

		if(hasAccountInDatabase(database))
			database.update(Schema.Table.TABLE_NAME, values, null, null);
		else
			database.insert(Schema.Table.TABLE_NAME, null, values);
	}

	public AccountCursorWrapper queryAccount(SQLiteDatabase database) {
		String[] cols = new String[1];
		cols[0] = "*";
		Cursor cursor = database.query(
				Schema.Table.TABLE_NAME,
				cols, null, null, null, null, null
		);
		return new AccountCursorWrapper(cursor);
	}

	private boolean hasAccountInDatabase(SQLiteDatabase database) {
		String[] cols = new String[1];
		cols[0] = "*";
		Cursor cursor = database.query(
				Schema.Table.TABLE_NAME,
				cols, null, null, null, null, null
		);
		boolean has = !cursor.isAfterLast();
		cursor.close();
		return has;
	}
}
