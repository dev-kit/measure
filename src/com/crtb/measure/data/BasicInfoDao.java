package com.crtb.measure.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BasicInfoDao extends BaseDao {
	public static final String TABLE = "basicinfo";
	public static final String ID = "_id";
	public static final String ZONE_CODE = "zone_code";
	public static final String SITE_COTE = "site_code";
	public static final String SECTION_NAME = "section_name";
	public static final String SECTION_CODE = "section_code";
	public static final String INNER_CODES = "inner_codes";
	public static final String UPLOAD = "upload";

    public synchronized static Cursor queryAllBasicInfo(String where) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.query(TABLE, null, where, null, null, null, null);
    }
}
