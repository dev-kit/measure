package com.crtb.measure;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;

import com.crtb.measure.data.BasicData;
import com.crtb.measure.service.CrtbWebService;
import com.crtb.measure.service.IWebService;

public class MainActivity extends Activity {
	private IWebService mWebService;
	private BasicData mBasicData = new BasicData();
	private int mCount = 0;
	private SharedPreferences mPreferences;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case IWebService.MSG_GET_ZONE_AND_SITE_CODE_DONE:
				String data = (String)msg.obj;
				String[] temp = data.split(",");
				mBasicData.setZoneCode(temp[0]);
				mBasicData.setSiteCode(temp[1]);
				if (!TextUtils.isEmpty(mBasicData.getSiteCode())) {
					mWebService.getPartInfosAsync(mBasicData.getSiteCode(), mHandler);
					mWebService.getSurveyorsAsync(mBasicData.getSiteCode(), mHandler);
				}
				break;
			case IWebService.MSG_GET_PART_INFOS_DONE:
				for(String name: ((String)msg.obj).split(",")) {
					mBasicData.addProject(name);
					mWebService.getSectInfosAsync(mBasicData.getSiteCode(), name, mHandler);
				}
				break;
			case IWebService.MSG_GET_SECT_INFOS_DONE:
				String[] temp2 = ((String)msg.obj).split(":");
				String projectName = temp2[0];
				for(String sectionCode: temp2[1].split(",")) {
					mBasicData.addSectionCode(projectName, sectionCode);
				    mWebService.getTestCodesAsync(sectionCode, mHandler);
				    mCount++;
				}
				break;
			case IWebService.MSG_GET_TEST_CODES_DONE:
				String[] temp3 = ((String)msg.obj).split(":");
				String sectionCode = temp3[0];
				String innerCodes = temp3[1];
				mBasicData.addInnerCodes(sectionCode, innerCodes);
				mCount--;
				if(mCount == 0) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							mBasicData.store();
						}
					}).start();
				}
				break;
			case IWebService.MSG_GET_SURVEYORS_DONE:
				for(String surveyor: ((String)msg.obj).split(",")) {
					mBasicData.addSurveyor(surveyor);
				}
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mPreferences = getSharedPreferences("crtb.xml", MODE_PRIVATE);
		String userName = mPreferences.getString("username", "");
		String password = mPreferences.getString("password", "");
		mBasicData.setUserName(userName);
		mBasicData.setLoginDate(new Date());
		mWebService = CrtbWebService.getInstance();
		mWebService.getZoneAndSiteCodeAsync(userName, password, mHandler);
		
		Intent intent = new Intent(this, SectionActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
