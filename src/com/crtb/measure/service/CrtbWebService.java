package com.crtb.measure.service;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.os.Handler;

public class CrtbWebService implements IWebService {
    private MsgRequestHandler  mRequestHandler  = new MsgRequestHandler();
    
	private static CrtbWebService sInstance = new CrtbWebService();
    
	private CrtbWebService() { 
	}
	
	public static IWebService getInstance() {
		return sInstance;
	}
	
	@Override
	public void getZoneAndSiteCodeAsync(final String userName, final String password, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("uname", userName);
				parameters.put("upassword", password);
				SoapObject message = SoapMessageFactory.createMessage("getZoneAndSiteCode", parameters);
				try {
					mRequestHandler.send(message, new MsgResponseHandlerWrapper(null, handler));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void getPartInfos(String siteCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSectInfos(String siteCode, String sectionName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSurveyors(String siteCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadSectionPointInfo() {
		// TODO Auto-generated method stub
		
	}

}
