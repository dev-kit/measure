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

import com.crtb.measure.data.BasicInfoDao;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * A list view example where the data comes from a cursor, and a
 * SimpleCursorListAdapter is used to map each item to a two-line display.
 */
public class SectionActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a cursor with all phones
        Cursor c = BasicInfoDao.queryAllBasicInfo(null);
        startManagingCursor(c);

        // Map Cursor columns to views defined in simple_list_item_2.xml
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, c, new String[] {
                        BasicInfoDao.SECTION_NAME, BasicInfoDao.UPLOAD
                }, new int[] {
                        android.R.id.text1, android.R.id.text2
                });
        // Used to display a readable string for the phone type
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                // Let the adapter handle the binding if the column is not TYPE
                if (cursor == null || cursor.getCount() == 0) {
                    return true;
                }
                String sectionName = cursor.getString(COLUMN_SECTION_NAME);
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
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, PointActivity.class);
        intent.putExtra("section_id", id - 1);
        startActivity(intent);
    }

    private static final int COLUMN_SECTION_NAME = 3;;
    private static final int COLUMN_UPLOAD = 6;
}
