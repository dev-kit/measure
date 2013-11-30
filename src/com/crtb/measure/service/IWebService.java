package com.crtb.measure.service;

import java.util.Map;

import android.os.Handler;

public interface IWebService {
	/**
	 * 
	 * @param userName 登陆账号
	 * @param password 登陆密码
	 */
	public void getZoneAndSiteCodeAsync(String userName, String password, Handler handler);
	/**
	 * 
	 * @param siteCode 工点编号
	 */
	public void getPartInfosAsync(String siteCode, Handler handler);
	/**
	 * 
	 * @param siteCode 工点编号
	 * @param sectionName 断面名称
	 */
	public void getSectInfosAsync(String siteCode, String sectionName, Handler handler);
	/**
	 * 
	 * @param siteCode 工点编号
	 */
	public void getSurveyorsAsync(String siteCode, Handler handler);
	/**
	 * 
	 * @param result
	 * @param handler
	 */
	public void getTestResultDataAsync(Map<String, String> result, Handler handler);
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getMeasureResult();
}
