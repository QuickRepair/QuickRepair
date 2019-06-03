package com.har.quickrepairforandroid.Database;

public class Schema {
	public static final class AccountTable {
		public static final String TABLE_NAME = "account";
		public static final class Cols {
			public static final String ACCOUNT = "account";
			public static final String PASSWORD = "password";
			public static final String ACCOUNT_TYPE = "type";
		}
	}

	public static final class OrderTable {
		public static final String TABLE_NAME = "order";
		public static final class Cols {
			public static final String ID = "id";
			public static final String APPLIANCE_TYPE = "type";
			public static final String STATE = "state";
			public static final String CREATE_DATE = "create_date";
			public static final String RECEIVED_DATE = "received_date";
			public static final String END_REPAIR_DATE = "end_repair_date";
			public static final String FINISH_DATE = "finish_date";
			public static final String REJECT_DATE = "reject_date";
		}
	}
}
