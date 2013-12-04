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
import com.crtb.measure.data.SectionDao;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * A list view example where the data comes from a cursor, and a
 * SimpleCursorListAdapter is used to map each item to a two-line display.
 */
public class PointActivity extends ListActivity {

    String[] mPointsList = null;

    private String mSectCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_list);
        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        int sectionID = (int)getIntent().getLongExtra("section_id", -1);
        if (sectionID < 0) {
            return;
        }

        // String where = BasicInfoDao.ID + "=" + sectionID;
        Cursor c = BasicInfoDao.queryAllBasicInfo(null);

        if (c == null || c.getCount() <= sectionID) {
            return;
        }

        if (c.moveToPosition(sectionID)) {
            try {
                String innerCodes = c.getString(c.getColumnIndex(BasicInfoDao.INNER_CODES));
                if (!TextUtils.isEmpty(innerCodes)) {
                    mPointsList = innerCodes.split("/|#");
                }
                mSectCode = c.getString(c.getColumnIndex(BasicInfoDao.SECTION_CODE));
            } catch (SQLException e) {
                c.close();
            }
        }

        String where = SectionDao.ID + "=" + mSectCode;
        Cursor sectionCursor = SectionDao.getSection(where);

        if (sectionCursor != null || sectionCursor.getCount() != 0) {
            // query DB
//            return;
        }

        if (mPointsList == null) {
            return;
        }
        // setListAdapter(new ArrayAdapter<String>(this,
        // android.R.layout.simple_list_item_1,
        // mPointsList));
        setListAdapter(new PointAdapter(this));
        getListView().setTextFilterEnabled(true);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    // private class PointCursorAdapter extends CursorAdapter {
    //
    // private LayoutInflater mInflater;
    //
    // public PointCursorAdapter(Context context, Cursor c) {
    // super(context, c);
    // // TODO Auto-generated constructor stub
    // mInflater =
    // (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    // }
    //
    // @Override
    // public void bindView(View view, Context context, Cursor cursor) {
    //
    // TextView pointNameView = (TextView)view.findViewById(R.id.point_name);
    // pointNameView.setText(mPointsList[position]);
    // // Null tag means the view has the correct data
    // view.setTag(null);
    //
    // }
    //
    // @Override
    // public View newView(Context context, Cursor cursor, ViewGroup parent) {
    // View view = mInflater.inflate(R.layout.point_list_item, parent, false);
    //
    // return view;
    // }
    //
    // }

    private class PointAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public PointAdapter(Context context) {
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /**
         * The number of items in the list is determined by the number of
         * speeches in our array.
         * 
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
            return mPointsList.length;
        }

        /**
         * Since the data comes from an array, just returning the index is
         * sufficent to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         * 
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         * 
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Make a view to hold each row.
         * 
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         *      android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.point_list_item, parent, false);
            } else {
                view = (TextView)convertView;
            }

            TextView pointNameView = (TextView)view.findViewById(R.id.point_name);
            pointNameView.setText(mPointsList[position]);
            // Null tag means the view has the correct data
            view.setTag(null);

            return view;
        }
    }

}
