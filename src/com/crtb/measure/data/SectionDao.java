package com.crtb.measure.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SectionDao extends BaseDao {
	public static final String TABLE = "section";
	public static final String ID = "id";
	public static final String SECTION_CODE = "section_code";
	public static final String FACE_KILO = "face_kilo";
	public static final String BUILD_STEP = "build_step";
	public static final String INSTRUMENT = "instrument";
	public static final String SURVEYOR_NAME = "surveyor_name";
	public static final String SURVEYOR_ID = "surveyor_id";
	public static final String DESCRIPTION = "description";
	public static final String UPLOAD = "upload";
	
    public synchronized static Cursor getSection(String where) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.query(TABLE, null, where, null, null, null, null);
    }
}
