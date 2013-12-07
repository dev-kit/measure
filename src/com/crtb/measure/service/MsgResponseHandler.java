package com.crtb.measure.service;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.R.integer;
import android.R.string;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MsgResponseHandler {
	private static final String TAG = "MsgResponseHandler";
	private Handler mUiHandler;
	
	public MsgResponseHandler(Handler uiHandler) {
		mUiHandler = uiHandler;
	}

	public void handleResponse(Object response, SoapObject message) {
		String action = message.getName();
		Log.d(TAG, "response: " + response);
		try {
			if (!(response instanceof SoapObject)) {
				Log.d(TAG, "response is invalid");
				return;
			}
			SoapObject result = (SoapObject) response;
			Object data = result.getProperty(action + "Result");
			if (data == null) {
				Log.d(TAG, "Invalid data.");
				return;
			}
			//XKSJ01BD03SD01#第三工区/XKSJ01SD0001#某某隧道名称
			if ("getZoneAndSiteCodeResponse".equals(result.getName())) {
				final String[] temp = ((SoapObject)data).getPropertyAsString(0).split("/");
				final String zone_code = temp[0].split("#")[0];
				final String site_code = temp[1].split("#")[0];
				Message msg = Message.obtain();
				msg.what = IWebService.MSG_GET_ZONE_AND_SITE_CODE_DONE;
				msg.obj = zone_code + "," + site_code;
				mUiHandler.sendMessage(msg);
				Log.d(TAG, "zone_code: " + zone_code + ", " + "site_code: " + site_code);
			} else if ("getPartInfosResponse".equals(result.getName())) {
				//getPartInfosResponse{getPartInfosResult=anyType{string=进口; string=出口; }; }
				StringBuilder sb = new StringBuilder();
				final int count = ((SoapObject)data).getPropertyCount();
				for(int i = 0 ; i < count; i++) {
					sb.append(((SoapObject)data).getPropertyAsString(i));
					sb.append(",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
				Message msg = Message.obtain();
				msg.what = IWebService.MSG_GET_PART_INFOS_DONE;
				msg.obj = sb.toString();
				mUiHandler.sendMessage(msg);
			} else if ("getSectInfosResponse".equals(result.getName())){
				StringBuilder sb = new StringBuilder();
				final int count = ((SoapObject)data).getPropertyCount();
				for(int i = 0 ; i < count; i++) {
					sb.append(((SoapObject)data).getPropertyAsString(i));
					sb.append(",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
				Message msg = Message.obtain();
				msg.what = IWebService.MSG_GET_SECT_INFOS_DONE;
				String projectName = message.getPropertyAsString("partName");
				msg.obj = projectName + ":" + sb.toString();
				mUiHandler.sendMessage(msg);
			} else if("getTestCodesResponse".equals(result.getName())) {
				StringBuilder sb = new StringBuilder();
				final int count = ((SoapObject)data).getPropertyCount();
				for(int i = 0 ; i < count; i++) {
					sb.append(((SoapObject)data).getPropertyAsString(i));
					sb.append("/");
				}
				sb.deleteCharAt(sb.lastIndexOf("/"));
				Message msg = Message.obtain();
				msg.what = IWebService.MSG_GET_TEST_CODES_DONE;
				String sectionCode = message.getPropertyAsString("sectcode");
				msg.obj = sectionCode + ":" + sb.toString();
				mUiHandler.sendMessage(msg);
				Log.d(TAG, "innercodes: " + sb.toString());
			} else if("getSurveyorsResponse".equals(result.getName())) {
				StringBuilder sb = new StringBuilder();
				final int count = ((SoapObject)data).getPropertyCount();
				for(int i = 0; i < count; i++) {
					sb.append(((SoapObject)data).getPropertyAsString(i));
					sb.append(",");
				}
				Message msg = Message.obtain();
				msg.what = IWebService.MSG_GET_SURVEYORS_DONE;
				msg.obj = sb.toString();
				mUiHandler.sendMessage(msg);
			} else if ("getTestResultDataResponse".equals(result.getName())) {
				String resultCode = ((SoapPrimitive)data).toString();
				String sectionCode = message.getPropertyAsString("sectcode");
				Message msg = Message.obtain();
				msg.what = IWebService.MSG_GET_TEST_RESULT_DATA_DONE;
				msg.obj = sectionCode;
				mUiHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
