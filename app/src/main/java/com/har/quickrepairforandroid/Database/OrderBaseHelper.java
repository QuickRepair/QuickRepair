package com.har.quickrepairforandroid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.har.quickrepairforandroid.Models.Order;

public class OrderBaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "order.db";

	public OrderBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + Schema.OrderTable.TABLE_NAME + "(" +
				Schema.OrderTable.Cols.ID + ", " +
				Schema.OrderTable.Cols.APPLIANCE_TYPE + ", " +
				Schema.OrderTable.Cols.STATE + ", " +
				Schema.OrderTable.Cols.CREATE_DATE + ", " +
				Schema.OrderTable.Cols.RECEIVED_DATE + ", " +
				Schema.OrderTable.Cols.END_REPAIR_DATE + ", " +
				Schema.OrderTable.Cols.FINISH_DATE + ", " +
				Schema.OrderTable.Cols.REJECT_DATE + ")"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void updateOrder(Order order) {
		ContentValues values = new ContentValues();
		String state = "";
		switch(order.orderState()) {
			case unreceived:    state = "unreceived";   break;
			case received:      state = "received";     break;
			case repairing:     state = "repairing";    break;
			case paying:        state = "paying";       break;
			case finished:      state = "finished";     break;
			case reject:        state = "reject";       break;
		}
		values.put(Schema.OrderTable.Cols.STATE, state);
		values.put(Schema.OrderTable.Cols.CREATE_DATE, order.createDate());
		values.put(Schema.OrderTable.Cols.REJECT_DATE, order.rejectDate());
		values.put(Schema.OrderTable.Cols.RECEIVED_DATE, order.receivedDate());
		values.put(Schema.OrderTable.Cols.END_REPAIR_DATE, order.endRepairDate());
		values.put(Schema.OrderTable.Cols.FINISH_DATE, order.finishDate());

		SQLiteDatabase database = getWritableDatabase();
		database.update(Schema.OrderTable.TABLE_NAME,
				values,
				Schema.OrderTable.Cols.ID + "=",
				new String[]{ String.valueOf(order.id()) }
		);
	}

	public void insertOrder(Order order) {
		ContentValues values = new ContentValues();
		String state = "";
		switch(order.orderState()) {
			case unreceived:    state = "unreceived";   break;
			case received:      state = "received";     break;
			case repairing:     state = "repairing";    break;
			case paying:        state = "paying";       break;
			case finished:      state = "finished";     break;
			case reject:        state = "reject";       break;
		}
		values.put(Schema.OrderTable.Cols.STATE, state);
		values.put(Schema.OrderTable.Cols.ID, String.valueOf(order.id()));
		values.put(Schema.OrderTable.Cols.APPLIANCE_TYPE, order.applianceType());
		values.put(Schema.OrderTable.Cols.CREATE_DATE, order.createDate());
		values.put(Schema.OrderTable.Cols.REJECT_DATE, order.rejectDate());
		values.put(Schema.OrderTable.Cols.RECEIVED_DATE, order.receivedDate());
		values.put(Schema.OrderTable.Cols.END_REPAIR_DATE, order.endRepairDate());
		values.put(Schema.OrderTable.Cols.FINISH_DATE, order.finishDate());

		SQLiteDatabase database = getWritableDatabase();
		database.insert(Schema.OrderTable.TABLE_NAME,
				null,
				values
		);
	}
}
