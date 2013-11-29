package com.crtb.measure.service;

import java.util.HashMap;

public class MsgResponseHandlerFactory {
	private static final String TAG = "MsgResponseHandlerFactory";
	
	private static HashMap<String, MsgResponseHandler> sHandlers = new HashMap<String, MsgResponseHandler>();
	
	private static MsgResponseHandler sHandler1 = new MsgResponseHandler() {
		@Override
		public void handleResponse(Object response, String action) {
			//getZoneAndSiteCodeResponse{getZoneAndSiteCodeResult=anyType{string=XKSJ01BD03SD01#第三工区/XKSJ01SD0001#某某隧道名称; }; }
			//XKSJ01BD03SD01#第三工区/XKSJ01SD0001#某某隧道名称
//				SoapObject data = (SoapObject)(response.getProperty(action + "Result"));
//				String text = data.getPropertyAsString(0);
//				Log.d(TAG, text);
			}
			
	};
	
	static {
		sHandlers.put("getZoneAndSiteCodeResponse", sHandler1);
	}
	
	public static MsgResponseHandler getHandler(String action) {
		return sHandlers.get(action + "Response");
	}

}
