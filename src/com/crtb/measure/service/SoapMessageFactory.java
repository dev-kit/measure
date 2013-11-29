package com.crtb.measure.service;

import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.text.TextUtils;

public class SoapMessageFactory {
	private static final String NAMESPACE = "http://tempuri.org/";
	
	public static SoapObject createMessage(String action, Map<String, String> parameters) {
		if (TextUtils.isEmpty(action)) {
			throw new IllegalArgumentException("SoapMessageFactory.createMessage: action is NULL." );
		}
		SoapObject message = new SoapObject(NAMESPACE, action);
		for(String key : parameters.keySet()) {
			message.addProperty(key, parameters.get(key));
		}
		return message;
	}
}
