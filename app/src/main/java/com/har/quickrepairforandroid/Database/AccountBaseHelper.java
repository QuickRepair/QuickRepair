package com.har.quickrepairforandroid.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountBaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "account.db";

	public AccountBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + Schema.Table.TABLE_NAME + "(" +
				Schema.Table.Cols.ACCOUNT + ", " +
				Schema.Table.Cols.PASSWORD + ", " +
				Schema.Table.Cols.ACCOUNT_TYPE + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
