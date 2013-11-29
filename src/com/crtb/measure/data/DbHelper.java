package com.crtb.measure.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "crtb.db";
	private static final int DB_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + UserDao.TABLE + " (" +
                UserDao.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserDao.SERVER + " TEXT, " +
                UserDao.PORT + " INTEGER, " +
                UserDao.API_TYPE + " INTEGER, " +
                UserDao.API_PATH + " TEXT, " +
                UserDao.USER_NAME + " TEXT, " +
                UserDao.DESCRIPTION + " TEXT, " +
                UserDao.LOGIN_TIME + " TEXT, " +
                UserDao.ZONE_CODE + " TEXT, " +
                UserDao.SITE_CODE + " TEXT" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("DROP TABLE IF EXISTS " + UserDao.TABLE + ";");
		 onCreate(db);
	}

}
