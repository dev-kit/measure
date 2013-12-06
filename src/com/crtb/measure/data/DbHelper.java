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
		//create project table
		db.execSQL("CREATE TABLE " + ProjectDao.TABLE + " (" +
				ProjectDao.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ProjectDao.NAME + " TEXT, " +
				ProjectDao.CREATE_TIME + " TEXT, " +
				ProjectDao.START_CHAINAGE + " TEXT, " +
				ProjectDao.END_CHAINAGE + " TEXT, " + 
				"UNIQUE (" + ProjectDao.NAME + ")" + " )");
		
		//create section basic info table
		db.execSQL("CREATE TABLE " + BasicInfoDao.TABLE + " (" +
				BasicInfoDao.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				BasicInfoDao.ZONE_CODE + " TEXT, " +
				BasicInfoDao.SITE_COTE + " TEXT, " +
				BasicInfoDao.SECTION_NAME + " TEXT, " +
				BasicInfoDao.SECTION_CODE + " TEXT, " +
				BasicInfoDao.INNER_CODES + " TEXT, " +
				BasicInfoDao.UPLOAD + " INTEGER DEFAULT 0, " +
				"UNIQUE (" + BasicInfoDao.SECTION_CODE + ")"+ " )");
		
		//create section table
		db.execSQL("CREATE TABLE " + SectionDao.TABLE + " (" +
				SectionDao.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				SectionDao.SECTION_CODE + " TEXT, " +
				SectionDao.FACE_KILO + " TEXT, " +
				SectionDao.BUILD_STEP + " TEXT, " +
				SectionDao.INSTRUMENT + " TEXT, " +
				SectionDao.SURVEYOR_NAME + " TEXT, " +
				SectionDao.SURVEYOR_ID + " TEXT, " +
				SectionDao.DESCRIPTION + " TEXT, " +
				SectionDao.UPLOAD + " INTEGER" + ");");
		
		//create point table
		db.execSQL("CREATE TABLE " + PointDao.TABLE + " (" +
				PointDao.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PointDao.SECTION_ID + " INTEGER, " +
				PointDao.INNER_CODE + " TEXT, " +
				PointDao.POINT_TYPE + " TEXT, " +
				PointDao.MTIME + " TEXT, " +
				PointDao.MVALUES + " TEXT, " +
				PointDao.XYZS + " TEXT, " +
				PointDao.DESCRIPTION + " TEXT" + ");");
		
		//create user table
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
                UserDao.SITE_CODE + " TEXT, " + 
                "UNIQUE (" + UserDao.USER_NAME + ")"+ " )");
		
		//create surveyor table
		db.execSQL("CREATE TABLE " + SurveyorDao.TABLE + " (" +
				SurveyorDao.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				SurveyorDao.USER_ID + " INTEGER, " +
				SurveyorDao.SURVEYOR_NAME + " TEXT, " +
				SurveyorDao.SURVEYOR_ID + " TEXT, " +
				SurveyorDao.DESCRIPTION + " TEXT, " + 
				"UNIQUE (" + SurveyorDao.SURVEYOR_ID + ")"+ " )");
}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("DROP TABLE IF EXISTS " + ProjectDao.TABLE + ";");
		 db.execSQL("DROP TABLE IF EXISTS " + BasicInfoDao.TABLE + ";");
		 db.execSQL("DROP TABLE IF EXISTS " + SectionDao.TABLE + ";");
		 db.execSQL("DROP TABLE IF EXISTS " + PointDao.TABLE + ";");
		 db.execSQL("DROP TABLE IF EXISTS " + UserDao.TABLE + ";");
		 db.execSQL("DROP TABLE IF EXISTS " + SurveyorDao.TABLE + ";");
		 onCreate(db);
	}

}
