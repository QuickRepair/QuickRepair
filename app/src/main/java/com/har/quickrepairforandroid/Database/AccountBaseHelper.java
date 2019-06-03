package com.har.quickrepairforandroid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.har.quickrepairforandroid.Models.AccountHolder;

public class AccountBaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "account.db";

	public AccountBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + Schema.AccountTable.TABLE_NAME + "(" +
				Schema.AccountTable.Cols.ACCOUNT + ", " +
				Schema.AccountTable.Cols.PASSWORD + ", " +
				Schema.AccountTable.Cols.ACCOUNT_TYPE + ")"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public AccountCursorWrapper queryAccount() {
		String[] cols = new String[1];
		cols[0] = "*";
		Cursor cursor = getReadableDatabase().query(
				Schema.AccountTable.TABLE_NAME,
				cols, null, null, null, null, null
		);
		return new AccountCursorWrapper(cursor);
	}

	public void updateAccount() {
		ContentValues values = new ContentValues();
		values.put(Schema.AccountTable.Cols.ACCOUNT, AccountHolder.getInstance().getAccount());
		values.put(Schema.AccountTable.Cols.PASSWORD, AccountHolder.getInstance().getPassword());
		values.put(Schema.AccountTable.Cols.ACCOUNT_TYPE, AccountHolder.getInstance().getIsCustomer() ? "customer" : "merchant");

		SQLiteDatabase database = getWritableDatabase();
		if(hasAccountInDatabase(database))
			database.update(Schema.AccountTable.TABLE_NAME, values, null, null);
		else
			database.insert(Schema.AccountTable.TABLE_NAME, null, values);
	}

	private boolean hasAccountInDatabase(SQLiteDatabase database) {
		String[] cols = new String[1];
		cols[0] = "*";
		Cursor cursor = database.query(
				Schema.AccountTable.TABLE_NAME,
				cols, null, null, null, null, null
		);
		boolean has = !cursor.isAfterLast();
		cursor.close();
		return has;
	}
}
