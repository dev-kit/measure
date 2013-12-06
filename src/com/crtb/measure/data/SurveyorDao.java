package com.crtb.measure.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SurveyorDao extends BaseDao {
	public static final String TABLE = "surveyor";
	public static final String ID = "id";
	public static final String USER_ID = "user_id";
	public static final String SURVEYOR_NAME = "surveyor_name";
	public static final String SURVEYOR_ID = "surveyor_id";
	public static final String DESCRIPTION = "description";
	
	public synchronized void insert(String name, String id) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SURVEYOR_NAME, name);
		values.put(SURVEYOR_ID, id);
		db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
}
