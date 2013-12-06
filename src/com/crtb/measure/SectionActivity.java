/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crtb.measure;

import java.util.Date;

import com.crtb.measure.data.BasicData;
import com.crtb.measure.data.BasicInfoDao;
import com.crtb.measure.service.CrtbWebService;
import com.crtb.measure.service.IWebService;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * A list view example where the data comes from a cursor, and a
 * SimpleCursorListAdapter is used to map each item to a two-line display.
 */
public class SectionActivity extends ListActivity {

    SimpleCursorAdapter mAdapter;

    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = getSharedPreferences("crtb.xml", MODE_PRIVATE);
        String userName = mPreferences.getString("username", "");
        String password = mPreferences.getString("password", "");
        mBasicData.setUserName(userName);
        mBasicData.setLoginDate(new Date());
        mWebService = CrtbWebService.getInstance();
        mWebService.getZoneAndSiteCodeAsync(userName, password, mHandler);

        showSectionView();
    }

    private void showSectionView() {
        // Get a cursor with all phones
        mCursor = BasicInfoDao.query(null);
        startManagingCursor(mCursor);

        // Map Cursor columns to views defined in simple_list_item_2.xml
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, mCursor,
                new String[] {
                        BasicInfoDao.SECTION_NAME, BasicInfoDao.UPLOAD
                }, new int[] {
                        android.R.id.text1, android.R.id.text2
                });
        // Used to display a readable string for the phone type
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                // Let the adapter handle the binding if the column is not TYPE
                if (cursor == null || cursor.getCount() == 0) {
                    return true;
                }
                String sectionName = cursor.getString(COLUMN_SECTION_NAME);
                if (TextUtils.isEmpty(sectionName)) {
                    sectionName = cursor.getString(COLUMN_SECTION_CODE);
                }
                int upload = cursor.getInt(COLUMN_UPLOAD);
                if (columnIndex == COLUMN_SECTION_NAME) {
                    ((TextView)view).setText(sectionName);
                }
                if (columnIndex == COLUMN_UPLOAD) {
                    ((TextView)view).setText(upload > 0 ? R.string.upload : R.string.not_upload);
                }
                return true;
            }
        });
        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, PointActivity.class);
        intent.putExtra("section_id", id - 1);
        startActivity(intent);
    }

    public void refresh() {
        stopManagingCursor(mCursor);
        mCursor.close();
        showSectionView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        refresh();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.refresh);
        return super.onPrepareOptionsMenu(menu);
    }

    private static final int COLUMN_SECTION_NAME = 3;

    private static final int COLUMN_SECTION_CODE = 4;

    private static final int COLUMN_UPLOAD = 6;

    private IWebService mWebService;

    private BasicData mBasicData = new BasicData();

    private int mCount = 0;

    private SharedPreferences mPreferences;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IWebService.MSG_GET_ZONE_AND_SITE_CODE_DONE:
                    String data = (String)msg.obj;
                    String[] temp = data.split(",");
                    mBasicData.setZoneCode(temp[0]);
                    mBasicData.setSiteCode(temp[1]);
                    if (!TextUtils.isEmpty(mBasicData.getSiteCode())) {
                        mWebService.getPartInfosAsync(mBasicData.getSiteCode(), mHandler);
                        mWebService.getSurveyorsAsync(mBasicData.getSiteCode(), mHandler);
                    }
                    break;
                case IWebService.MSG_GET_PART_INFOS_DONE:
                    for (String name : ((String)msg.obj).split(",")) {
                        mBasicData.addProject(name);
                        mWebService.getSectInfosAsync(mBasicData.getSiteCode(), name, mHandler);
                    }
                    break;
                case IWebService.MSG_GET_SECT_INFOS_DONE:
                    String[] temp2 = ((String)msg.obj).split(":");
                    String projectName = temp2[0];
                    for (String sectionCode : temp2[1].split(",")) {
                        mBasicData.addSectionCode(projectName, sectionCode);
                        mWebService.getTestCodesAsync(sectionCode, mHandler);
                        mCount++;
                    }
                    break;
                case IWebService.MSG_GET_TEST_CODES_DONE:
                    String[] temp3 = ((String)msg.obj).split(":");
                    String sectionCode = temp3[0];
                    String innerCodes = temp3[1];
                    mBasicData.addInnerCodes(sectionCode, innerCodes);
                    mCount--;
                    if (mCount == 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mBasicData.store();
                                SectionActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        refresh();
                                    }
                                    
                                });
                            }
                        }).start();
                    }
                    break;
                case IWebService.MSG_GET_SURVEYORS_DONE:
                    for (String surveyor : ((String)msg.obj).split(",")) {
                        mBasicData.addSurveyor(surveyor);
                    }
                    break;
                default:
                    break;
            }
        };
    };
}
