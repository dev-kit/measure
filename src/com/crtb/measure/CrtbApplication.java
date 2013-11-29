package com.crtb.measure;

import com.crtb.measure.data.BaseDao;
import com.crtb.measure.data.DbHelper;

import android.app.Application;

public class CrtbApplication extends Application {

	@Override
	public void onCreate() {
		BaseDao.setDbHelper(new DbHelper(getApplicationContext()));
		super.onCreate();
	}
	
}
