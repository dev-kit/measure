
package com.crtb.measure.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class PointDao extends BaseDao {
    public static final String TABLE = "point";

    public static final String ID = "_id";

    public static final String SECTION_ID = "section_id";

    public static final String INNER_CODE = "inner_code";

    public static final String POINT_TYPE = "point_type";

    public static final String MTIME = "mtime";

    public static final String MVALUES = "mvalues";

    public static final String XYZS = "xyzs";

    public static final String DESCRIPTION = "description";

    public synchronized static Cursor query(String where) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.query(TABLE, null, where, null, null, null, null);
    }

    public synchronized static Cursor getPointsBySection(String section) {
        String where = " where s." + SectionDao.SECTION_CODE + " = '" + section + "'";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(
                "SELECT p.* FROM section AS s JOIN point AS p ON s._id = p.section_id " + where,
                null);
        return c;
    }

    public synchronized static int update(ContentValues values, String whereClause,
            String[] whereArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(TABLE, values, whereClause, whereArgs);
    }
}
