package com.crtb.measure.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PointDao extends BaseDao {
	public static final String TABLE = "point";
	public static final String ID = "id";
	public static final String SECTION_ID = "section_id";
	public static final String INNER_CODE = "inner_code";
	public static final String POINT_TYPE = "point_type";
	public static final String MTIME = "mtime";
	public static final String MVALUES = "mvalues";
	public static final String XYZS = "xyzs";
	public static final String DESCRIPTION = "description";
	
    public synchronized static Cursor getPoint(String where) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.query(TABLE, null, where, null, null, null, null);
    }
}
