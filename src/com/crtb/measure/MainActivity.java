package com.crtb.measure;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.crtb.measure.data.UserDao;
import com.crtb.measure.service.CrtbWebService;
import com.crtb.measure.service.IWebService;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		UserDao userDao = new UserDao();
		userDao.createUser();
		IWebService webService = CrtbWebService.getInstance();
		webService.getZoneAndSiteCodeAsync("sdb", "123", new Handler());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}