package com.coms309.efficiency_coms309_iastate;

import android.provider.BaseColumns;

/**
 * SQLiteContract
 * 
 * Purpose: provide template for local SQLite database
 * 
 * Version: 1
 * 
 * @author James Saylor
 *
 */

public class SQLiteContract {
	// Empty constructor to prevent accidental instantiation
	public SQLiteContract() {
	}

	// Class to define tables contents
	public static abstract class AppEntries implements BaseColumns {
		public static final String TABLE_NAME = "AppTable";
		public static final String COLUMN_APP_NAME = "appName";
		public static final String COLUMN_PRODUCTIVITY = "productivityRating";
		public static final String COLUMN_CATEGORY = "category";
		public static final String COLUMN_ICON = "icon";

		// String to create the local DB
		private static final String SQL_Creator = "CREATE TABLE "
				+ AppEntries.TABLE_NAME + " (" + AppEntries.COLUMN_APP_NAME
				+ " TEXT PRIMARY KEY," + AppEntries.COLUMN_PRODUCTIVITY
				+ " INTEGER," + AppEntries.COLUMN_CATEGORY + " TEXT,"
				+ AppEntries.COLUMN_ICON + " BLOB" + " )";

		// String to delete local DB
		private static final String SQL_Destroyer = "DROP TABLE IF EXISTS "
				+ AppEntries.TABLE_NAME;
	}
}
