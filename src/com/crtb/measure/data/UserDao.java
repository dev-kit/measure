package com.crtb.measure.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UserDao extends BaseDao {
	public static final String TABLE = "user";
	public static final String ID = "id";
	public static final String SERVER = "server";
	public static final String PORT = "port";
	public static final String API_TYPE = "api_type";
	public static final String API_PATH = "api_path";
	public static final String USER_NAME = "user_name";
	public static final String DESCRIPTION = "description";
	public static final String LOGIN_TIME = "login_time";
	public static final String ZONE_CODE = "zone_code";
	public static final String SITE_CODE = "site_code";
	
	public UserDao(Context context) {
		super(context);
	}

	public void createUser() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(USER_NAME, "timlian");
		db.insert(TABLE, null, values);
	}
}
