
package com.crtb.measure.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class SectionDao extends BaseDao {
    public static final String TABLE = "section";

    public static final String ID = "_id";

    public static final String SECTION_CODE = "section_code";

    public static final String FACE_KILO = "face_kilo";

    public static final String BUILD_STEP = "build_step";

    public static final String INSTRUMENT = "instrument";

    public static final String SURVEYOR_NAME = "surveyor_name";

    public static final String SURVEYOR_ID = "surveyor_id";

    public static final String DESCRIPTION = "description";

    public static final String UPLOAD = "upload";

    public synchronized static Cursor query(String where) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.query(TABLE, null, where, null, null, null, null);
    }

    public synchronized static int update(ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(TABLE, values, where, whereArgs);
    }

    public synchronized static void syncBasicInfo() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor c = db
                .rawQuery(
                        "SELECT b.*, s._id as sid FROM basicinfo AS b LEFT JOIN section AS s ON b.section_code = s.section_code where sid IS NULL",
                        null);
        if (c == null) {
            return;
        }

        try {
            while (c.moveToNext()) {
                String[] pointsList = null;
                String sectionCode;
                String innerCodes = c.getString(c.getColumnIndex(BasicInfoDao.INNER_CODES));
                if (!TextUtils.isEmpty(innerCodes)) {
                    pointsList = innerCodes.split("/|#");
                }
                sectionCode = c.getString(c.getColumnIndex(BasicInfoDao.SECTION_CODE));

                if (!TextUtils.isEmpty(sectionCode)) {
                    ContentValues values = new ContentValues();
                    values.put(SECTION_CODE, sectionCode);
                    long Id = db.insert(TABLE, null, values);

                    if (pointsList == null) {
                        return;
                    }

                    for (String point : pointsList) {
                        if (!TextUtils.isEmpty(point)) {
                            ContentValues values2 = new ContentValues();
                            values2.put(PointDao.SECTION_ID, Id);
                            values2.put(PointDao.INNER_CODE, point);
                            db.insert(PointDao.TABLE, null, values2);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            c.close();
        }
    }

    public synchronized static void submit(String where) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UPLOAD, 1);
        db.update(TABLE, values, where, null);
    }
}
