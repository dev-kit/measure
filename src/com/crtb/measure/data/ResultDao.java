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
    	SectionDao.TABLE + "." + SectionDao.DESCRIPTION,
    	PointDao.MTIME,
    	PointDao.INNER_CODE,
    	PointDao.MVALUES,
    	PointDao.XYZS
    };
    
    private static final int IDX_SECTION_CODE = 0;
    private static final int IDX_FACE_KILO = 1;
    private static final int IDX_BUILD_STEP = 2;
    private static final int IDX_INSTRUMENT = 3;
    private static final int IDX_SURVEYOR_NAME = 4;
    private static final int IDX_SURVEYOR_ID = 5;
    private static final int IDX_DESCRIPTION = 6;
    private static final int IDX_SURVEY_DATE = 7;
    private static final int IDX_INNER_CODE = 8;
    private static final int IDX_MVALUES = 9;
    private static final int IDX_XYZS = 10;
	
    public HashMap<String, String> getMeasureResult() {
    	Map<String, String> result = null;
    	SQLiteDatabase db  = mDbHelper.getReadableDatabase();
    	String selection = SectionDao.UPLOAD + "=0"; 
    	Cursor cursor = db.query(TABLE, COLUMNS, selection, null, null, null, null);
    	//TimUtils.printCursorContent("ResultDao", cursor);
    	if (cursor != null && cursor.moveToFirst()) {
    		try {
    			String sectCode = null;
    			String facedk = null;
    			String buildStep = null;
    			String instrument = null;
    			String surveyor = null;
    			String surveyorId = null;
    			String description = null;
    			String surveyDate = null;
    			StringBuilder measCodes = new StringBuilder();
    			StringBuilder measVals = new StringBuilder();
    			StringBuilder measCoords = new StringBuilder();
    			do {
					if (sectCode == null) {
						sectCode = cursor.getString(IDX_SECTION_CODE);
					}
					if (facedk == null) {
						facedk = cursor.getString(IDX_FACE_KILO);
					}
					if (buildStep == null) {
						buildStep = cursor.getString(IDX_BUILD_STEP);
					}
					if (instrument == null) {
						instrument = cursor.getString(IDX_INSTRUMENT);
					}
					if (surveyor == null) {
						surveyor = cursor.getString(IDX_SURVEYOR_NAME);
					}
					if (surveyorId == null) {
						surveyorId = cursor.getString(IDX_SURVEYOR_ID);
					}
					if (description == null) {
						description = cursor.getString(IDX_DESCRIPTION);
					}
					if (surveyDate == null) {
						//surveyDate =  cursor.getString(IDX_SURVEY_DATE);
						surveyDate =  "crtb";
					}
					measCodes.append(cursor.getString(IDX_INNER_CODE));
					measCodes.append("/");
					measVals.append(cursor.getString(IDX_MVALUES));
					measVals.append("/");
					measCoords.append(cursor.getString(IDX_XYZS));
					measCoords.append("/");
				} while(cursor.moveToNext());
    			measCodes.deleteCharAt(measCodes.lastIndexOf("/"));
    			measVals.deleteCharAt(measVals.lastIndexOf("/"));
    			measCoords.deleteCharAt(measCoords.lastIndexOf("/"));
    			result = new HashMap<String, String>();
    			if (sectCode != null) {
    				result.put("sectcode", sectCode);
    			} else {
    				result.put("sectcode", "crtb");
    			}
    			if (facedk != null) {
    				result.put("facedk", facedk); //facekilo
    			} else {
    				result.put("facedk", "crtb");
    			}
    			if (buildStep != null) {
    				result.put("buildstep", buildStep);
    			} else {
    				result.put("buildstep", "crtb");
    			}
    			if (instrument != null) {
    				result.put("instr", instrument);
    			} else {
    				result.put("instr", "crtb");
    			}
    			if (surveyor != null) {
    				result.put("surveyor", surveyor);
    			} else {
    				result.put("surveyor", "crtb");
    			}
    			if (surveyorId != null) {
    				result.put("surveyID", surveyorId);
    			} else {
    				result.put("surveyID", "crtb");
    			}
    			if (description != null) {
    				result.put("description", description);
    			} else {
    				result.put("description", "crtb");
    			}
    			result.put("meascodes", measCodes.toString()); // innercodes
    			result.put("measvals", measVals.toString()); //values
    			result.put("meascoords", measCoords.toString());//xyz
    		} finally {
    			cursor.close();
    		}
    	}
		return result;
    }

}
