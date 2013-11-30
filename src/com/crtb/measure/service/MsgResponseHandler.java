package com.crtb.measure.service;

import org.ksoap2.serialization.SoapObject;

import com.crtb.measure.data.SurveyorDao;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MsgResponseHandler {
	private static final String TAG = "MsgResponseHandler";
	private Handler mUiHandler;
	
	public MsgResponseHandler(Handler uiHandler) {
		mUiHandler = uiHandler;
	}

	private void notifySuccess() {
		Message msg = Message.obtain();
		mUiHandler.sendMessage(msg);
	}
	
	private void notifyFailed() {
		Message msg = Message.obtain();
		mUiHandler.sendMessage(msg);
	}

	public void handleResponse(Object response, String action) {
		Log.d(TAG, "response: " + response);
		try {
			if (!(response instanceof SoapObject)) {
				Log.d(TAG, "response is invalid");
				return;
			}
			SoapObject result = (SoapObject) response;
			SoapObject data = (SoapObject) result.getProperty(action + "Result");
			if (data == null) {
				Log.d(TAG, "Invalid data.");
				return;
			}
			//XKSJ01BD03SD01#第三工区/XKSJ01SD0001#某某隧道名称
			if ("getZoneAndSiteCodeResponse".equals(result.getName())) {
				Log.d(TAG, data.getPropertyAsString(0));
			} else if ("getPartInfosResponse".equals(result.getName())) {
				//getPartInfosResponse{getPartInfosResult=anyType{string=进口; string=出口; }; }
				for(int i = 0 ; i < data.getPropertyCount(); i++) {
					Log.d(TAG, data.getPropertyAsString(i));
				}
			} else if ("getSectInfosResponse".equals(result.getName())){
				for(int i = 0 ; i < data.getPropertyCount(); i++) {
					Log.d(TAG, data.getPropertyAsString(i));
				}
			} else if("getSurveyorsResponse".equals(result.getName())) {
				SurveyorDao surveyorDao = new SurveyorDao();
				for(int i = 0; i < data.getPropertyCount(); i++) {
					String[] surveyor = data.getPropertyAsString(i).split("#");
					surveyorDao.insert(surveyor[0], surveyor[1]);
				}
			} else if ("getTestResultDataResponse".equals(result.getName())) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
