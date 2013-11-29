package com.crtb.measure.data;

public abstract class BaseDao {
	protected static DbHelper mDbHelper;
	
	public static void setDbHelper(DbHelper dbHelper) {
		mDbHelper = dbHelper;
	}
	
}
