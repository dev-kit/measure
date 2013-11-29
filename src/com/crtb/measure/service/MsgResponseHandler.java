package com.crtb.measure.service;

public interface MsgResponseHandler {
	/**
	 * 
	 * @param response
	 * @param action
	 */
	public void handleResponse(Object response, String action);
	
}
