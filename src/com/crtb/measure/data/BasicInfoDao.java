
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

    public synchronized void insert(String zoneCode, String siteCode, String sectionCode,
            String innerCodes) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ZONE_CODE, zoneCode);
        values.put(SITE_COTE, siteCode);
        values.put(SECTION_CODE, sectionCode);
        values.put(INNER_CODES, innerCodes);
        db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public synchronized static Cursor query(String where) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.query(TABLE, null, where, null, null, null, null);
    }

    public synchronized static void submit(String where) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UPLOAD, 1);
        db.update(TABLE, values, where, null);
    }
}
