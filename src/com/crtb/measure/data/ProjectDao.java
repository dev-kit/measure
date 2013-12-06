package com.crtb.measure.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ProjectDao extends BaseDao {
	public static final String TABLE = "project";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String CREATE_TIME = "create_time";
	public static final String START_CHAINAGE = "start_chainage";
	public static final String END_CHAINAGE = "end_chainage";
	
	public synchronized void insert(String projectName) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, projectName);
		db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
}
