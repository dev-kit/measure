package com.crtb.measure.service;

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
	public void getPartInfos(String siteCode);
	/**
	 * 
	 * @param siteCode 工点编号
	 * @param sectionName 断面名称
	 */
	public void getSectInfos(String siteCode, String sectionName);
	/**
	 * 
	 * @param siteCode 工点编号
	 */
	public void getSurveyors(String siteCode);
	
	public void uploadSectionPointInfo();
}
