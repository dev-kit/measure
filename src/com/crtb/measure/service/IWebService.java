package com.crtb.measure.service;

import java.util.Map;

import android.os.Handler;

public interface IWebService {
	public static final int MSG_GET_ZONE_AND_SITE_CODE_DONE = 1;
	public static final int MSG_GET_PART_INFOS_DONE = 2;
	public static final int MSG_GET_SECT_INFOS_DONE = 3;
	public static final int MSG_GET_TEST_CODES_DONE = 4;
	public static final int MSG_GET_SURVEYORS_DONE  = 5;
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
	public void getSectInfosAsync(String siteCode, String projectName, Handler handler);
	/**
	 * 
	 * @param sectionCode
	 * @param handler
	 */
	public void getTestCodesAsync(String sectionCode, Handler handler);
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
	public void getTestResultDataAsync(Handler handler);
}
