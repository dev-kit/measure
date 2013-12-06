package com.crtb.measure.data;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

import com.crtb.measure.util.Utils;

public class BasicData {
	private String mUserName;
	private String mLoginDate;
	private String mZoneCode;
	private String mSiteCode;
	private HashMap<String, ArrayList<String>> mProjectData = new HashMap<String, ArrayList<String>>();
	private HashMap<String , String> mSectionData = new HashMap<String, String>();
	private ArrayList<String> mSurveyors = new ArrayList<String>();
	
	public void setUserName(String userName) {
		mUserName = userName;
	}
	
	public void setLoginDate(Date date) {
		mLoginDate = Utils.formatDate(date);
	}
	
	public void setZoneCode(String zoneCode) {
		mZoneCode = zoneCode;
	}
	
	public void setSiteCode(String siteCode) {
		mSiteCode = siteCode;
	}
	
	public String getSiteCode() {
		return mSiteCode;
	}
	
	public void addProject(String name) {
		mProjectData.put(name, new ArrayList<String>());
	}
	
	public void addSectionCode(String name, String code) {
		if ("anyType{}".equals(code)) {
			return ;
		}
		ArrayList<String> sectionCodes = mProjectData.get(name);
		if (sectionCodes != null) {
			sectionCodes.add(code);
		}
	}
	
	public void addInnerCodes(String sectionCode, String innerCodes) {
		if ("anyType{}".equals(sectionCode) || "anyType{}".equals(innerCodes)) {
			return ;
		}
		mSectionData.put(sectionCode, innerCodes);
	}
	
	public void store() {
		UserDao userDao = new UserDao();
		userDao.insert(mUserName, mZoneCode, mSiteCode, mLoginDate);
		
		SurveyorDao surveyorDao = new SurveyorDao();
		for(String surveyor : mSurveyors) {
			String temp[] = surveyor.split("#");
			surveyorDao.insert(temp[0], temp[1]);
		}
		
		ProjectDao projectDao = new ProjectDao();
		for(String projectName : mProjectData.keySet()) {
			projectDao.insert(projectName);
		}
		BasicInfoDao basicInfoDao = new BasicInfoDao();
		for(String projectName : mProjectData.keySet()) {
			ArrayList<String> sectionCodes = mProjectData.get(projectName);
			for(String code : sectionCodes) {
				basicInfoDao.insert(mZoneCode, mSiteCode, code, mSectionData.get(code));
			}
		}
	}
	
	public void addSurveyor(String surveyor) {
		mSurveyors.add(surveyor);
	}
}