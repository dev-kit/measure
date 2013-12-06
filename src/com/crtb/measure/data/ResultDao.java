package com.crtb.measure.data;

import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ResultDao extends BaseDao {
    private static final String TABLE = SectionDao.TABLE + " LEFT OUTER JOIN " + PointDao.TABLE + " ON "
    		+ "(" + SectionDao.TABLE + "." + SectionDao.ID + "=" + PointDao.TABLE + "." + PointDao.SECTION_ID + ")";
    
    private static final String[] COLUMNS = {
    	SectionDao.SECTION_CODE,
    	SectionDao.FACE_KILO,
    	SectionDao.BUILD_STEP,
    	SectionDao.INSTRUMENT,
    	SectionDao.SURVEYOR_NAME,
    	SectionDao.SURVEYOR_ID,
    	SectionDao.DESCRIPTION,
    	PointDao.INNER_CODE,
    	PointDao.MTIME,
    	PointDao.MVALUES,
    	PointDao.XYZS
    };
	
    public Map<String, String> queryResult() {
    	Map<String, String> result = null;
    	SQLiteDatabase db  = mDbHelper.getReadableDatabase();
    	String selection = SectionDao.UPLOAD + "=0"; 
    	Cursor cursor = db.query(TABLE, COLUMNS, selection, null, null, null, null);
    	if (cursor != null && cursor.moveToFirst()) {
    		try {
    			result = new HashMap<String, String>();
    			result.put("sectcode", cursor.getString(0));
    			result.put("facedk", cursor.getString(1)); //facekilo
    			result.put("buildstep", cursor.getString(2));
    			result.put("instr", cursor.getString(3));
    			result.put("surveyor", cursor.getString(4));
    			result.put("surveyID", cursor.getString(5));
    			result.put("description", cursor.getString(6));
    			result.put("meascodes", cursor.getString(7)); // innercodes
    			result.put("surveydate", cursor.getString(8));
    			result.put("measvals", cursor.getString(9)); //values
    			result.put("meascoords", cursor.getString(10));//xyz
    		} finally {
    			cursor.close();
    		}
    	}
		return result;
    }

}
