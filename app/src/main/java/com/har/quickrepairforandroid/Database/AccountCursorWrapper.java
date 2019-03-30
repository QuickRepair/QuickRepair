package com.har.quickrepairforandroid.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

public class AccountCursorWrapper extends CursorWrapper {
	public AccountCursorWrapper(Cursor cursor) {
		super(cursor);
	}

	public String getAccount() {
		return getString(getColumnIndex(Schema.Table.Cols.ACCOUNT));
	}

	public String getPassword() {
		return getString(getColumnIndex(Schema.Table.Cols.PASSWORD));
	}

	public boolean getIsCustomer() {
		return getString(getColumnIndex(Schema.Table.Cols.ACCOUNT_TYPE)).equalsIgnoreCase("customer");
	}
}
