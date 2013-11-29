package com.crtb.measure.data;

import android.content.Context;

public abstract class BaseDao {
	protected static DbHelper mDbHelper;
	
	public BaseDao(Context context) {
		if (mDbHelper == null) {
			mDbHelper = new DbHelper(context);
		}
	}
}
