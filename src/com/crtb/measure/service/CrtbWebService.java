package com.crtb.measure.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.crtb.measure.data.ResultDao;

import android.os.Handler;
import android.util.Log;

public class CrtbWebService implements IWebService {
	private static final String TAG = "CrtbWebService";
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
	public void getSectInfosAsync(final String siteCode, final String projectName, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("gdcode", siteCode);
				parameters.put("partName", projectName);
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
	public void getTestCodesAsync(final String sectionCode, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("sectcode", sectionCode);
				SoapObject message = SoapMessageFactory.createMessage("getTestCodes", parameters);
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
	public void getTestResultDataAsync(final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ResultDao resultDao = new ResultDao();
				HashMap<String, String> measureResult = resultDao.getMeasureResult();
				if (measureResult != null) {
					SoapObject message = SoapMessageFactory.createMessage("getTestResultData", resultDao.getMeasureResult());
					try {
						send(message, new MsgResponseHandler(handler), TRAFFIC_SERVICE_URI_POST);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private static void send(SoapObject rpcMessage, MsgResponseHandler handler, String url) throws IOException, XmlPullParserException {
		Log.d(TAG, "sending request: " + rpcMessage.toString());
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(110);
        soapEnvelope.bodyOut = rpcMessage;
        soapEnvelope.dotNet  = true;
        soapEnvelope.setOutputSoapObject(rpcMessage);
        HttpTransportSE localHttpTransportSE = new HttpTransportSE(url, CONNECITON_TIME_OUT);
        localHttpTransportSE.call(NAMESPACE + rpcMessage.getName(), soapEnvelope);
        handler.handleResponse(soapEnvelope.bodyIn, rpcMessage);
    }
}
