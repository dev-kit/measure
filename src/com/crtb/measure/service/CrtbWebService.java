package com.crtb.measure.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Handler;

public class CrtbWebService implements IWebService {
	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String TRAFFIC_SERVICE_URI_GET  = "https://lccs.cr-tb.com/DTMS/ictrcp/basedown.asmx";
	private static final String TRAFFIC_SERVICE_URI_POST = "https://lccs.cr-tb.com/DTMS/ictrcp/testdata.asmx";
	private static final int CONNECITON_TIME_OUT = 10000;
	
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
					send(message, new MsgResponseHandler(handler), TRAFFIC_SERVICE_URI_GET);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void getPartInfosAsync(final String siteCode, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("gdcode", siteCode);
				SoapObject message = SoapMessageFactory.createMessage("getPartInfos", parameters);
				try {
					send(message, new MsgResponseHandler(handler), TRAFFIC_SERVICE_URI_GET);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void getSectInfosAsync(final String siteCode, final String sectionName, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("gdcode", siteCode);
				parameters.put("partName", sectionName);
				SoapObject message = SoapMessageFactory.createMessage("getSectInfos", parameters);
				try {
					send(message, new MsgResponseHandler(handler), TRAFFIC_SERVICE_URI_GET);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void getSurveyorsAsync(final String siteCode, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("gdcode", siteCode);
				SoapObject message = SoapMessageFactory.createMessage("getSurveyors", parameters);
				try {
					send(message, new MsgResponseHandler(handler), TRAFFIC_SERVICE_URI_GET);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public void getTestResultDataAsync(final Map<String, String> result, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SoapObject message = SoapMessageFactory.createMessage("getTestResultData", result);
				try {
					send(message, new MsgResponseHandler(handler), TRAFFIC_SERVICE_URI_POST);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	public Map<String, String> getMeasureResult() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("sectcode", "tim");
		result.put("meascodes", "tim");
		result.put("facedk", "tim");
		result.put("buildstep", "tim");
		result.put("instr", "tim");
		result.put("surveydate", "tim");
		result.put("measvals", "tim");
		result.put("meascoords", "tim");
		result.put("surveyor", "tim");
		result.put("surveyID", "tim");
		result.put("description", "tim");
		return result;
	}
	
	private static void send(SoapObject rpcMessage, MsgResponseHandler handler, String url) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(110);
        soapEnvelope.bodyOut = rpcMessage;
        soapEnvelope.dotNet  = true;
        soapEnvelope.setOutputSoapObject(rpcMessage);
        HttpTransportSE localHttpTransportSE = new HttpTransportSE(url, CONNECITON_TIME_OUT);
        localHttpTransportSE.call(NAMESPACE + rpcMessage.getName(), soapEnvelope);
        handler.handleResponse(soapEnvelope.bodyIn, rpcMessage.getName());
    }
}
