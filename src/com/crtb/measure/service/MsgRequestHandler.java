package com.crtb.measure.service;

import java.io.IOException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class MsgRequestHandler {
	private static final String TAG = "MsgRequestHandler";
	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String TRAFFIC_SERVICE_URI = "https://lccs.cr-tb.com/DTMS/ictrcp/basedown.asmx";
	private static final int CONNECITON_TIME_OUT = 10000;
	
	public static void send(SoapObject rpcMessage, MsgResponseHandler handler) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(110);
        soapEnvelope.bodyOut = rpcMessage;
        soapEnvelope.dotNet  = true;
        soapEnvelope.setOutputSoapObject(rpcMessage);
        HttpTransportSE localHttpTransportSE = new HttpTransportSE(TRAFFIC_SERVICE_URI, CONNECITON_TIME_OUT);
        localHttpTransportSE.call(NAMESPACE + rpcMessage.getName(), soapEnvelope);
        handler.handleResponse(soapEnvelope.bodyIn, rpcMessage.getName());
    }
	
}
