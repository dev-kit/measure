package com.crtb.measure.service;

import org.ksoap2.serialization.SoapObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MsgResponseHandlerWrapper implements MsgResponseHandler {
	private static final String TAG = "MsgResponseHandlerWrapper";
	private MsgResponseHandler mResponseHandler;
	private Handler mUiHandler;
	
	public MsgResponseHandlerWrapper(MsgResponseHandler responseHandler, Handler uiHandler) {
		mResponseHandler = responseHandler;
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

	@Override
	public void handleResponse(Object response, String action) {
		Log.d(TAG, "response: " + response);
		if (response == null || !(response instanceof SoapObject)) {
			Log.d(TAG, "Invalid response.");
			return;
		}
		if (mResponseHandler != null) {
			mResponseHandler.handleResponse(response, action);
		}
	}
}
