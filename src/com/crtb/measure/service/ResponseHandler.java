package com.crtb.measure.service;

import org.ksoap2.serialization.SoapObject;

public interface ResponseHandler {
	/**
	 * 
	 * @param response
	 */
	public void handleResponse(SoapObject response);
}
