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

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.crtb.measure.data.BasicInfoDao;
import com.crtb.measure.data.PointDao;
import com.crtb.measure.data.SectionDao;
import com.crtb.measure.data.SurveyorDao;
import com.crtb.measure.service.CrtbWebService;
import com.crtb.measure.service.IWebService;
import com.crtb.measure.util.BlueToothManager;

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
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CrtbWebService.getInstance().getTestResultDataAsync(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                    	if (msg.what == IWebService.MSG_GET_TEST_RESULT_DATA_DONE) {
                    		final String sectionCode = (String)msg.obj;
                    		Toast.makeText(PointActivity.this, "上传完毕!" + sectionCode, Toast.LENGTH_SHORT).show();
                    		new Thread(new Runnable() {
								@Override
								public void run() {
									BasicInfoDao.submit(BasicInfoDao.SECTION_CODE + "=" + "\'" + sectionCode + "\'");
									SectionDao.submit(SectionDao.SECTION_CODE + "=" + "\'" + sectionCode + "\'");
									refresh();
								}
							}).start();
                    	}
                    }
                });
            }
        });

        int sectionID = (int)getIntent().getLongExtra("section_id", -1);
        if (sectionID < 0) {
            return;
        }

        setListAdapter(new PointCursorAdapter(PointActivity.this, null));
        getListView().setTextFilterEnabled(true);
        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            SectionDao.syncBasicInfo();
            Cursor c = BasicInfoDao.query(null);

            int sectionID = (int)getIntent().getLongExtra("section_id", -1);
            if (c == null || c.getCount() <= sectionID) {
                return null;
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

            Cursor points = null;
            if (!TextUtils.isEmpty(mSectCode)) {
                points = PointDao.getPointsBySection(mSectCode);
            }

            return points;
        }

        @Override
        protected void onPostExecute(Cursor c) {
            if (c == null) {
                setListAdapter(new PointAdapter(PointActivity.this));
                return;
            }
            // setListAdapter(new ArrayAdapter<String>(this,
            // android.R.layout.simple_list_item_1,
            // mPointsList));
            ListAdapter listAdapter = getListAdapter();
            if (listAdapter instanceof CursorAdapter) {
                ((CursorAdapter)listAdapter).changeCursor(c);
            } else {
                setListAdapter(new PointCursorAdapter(PointActivity.this, c));
            }
            super.onPostExecute(c);
        }

    }

    private class PointCursorAdapter extends CursorAdapter {

        private LayoutInflater mInflater;

        public PointCursorAdapter(Context context, Cursor c) {
            super(context, c);
            // TODO Auto-generated constructor stub
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView pointNameView = (TextView)view.findViewById(R.id.point_name);
            pointNameView.setText(cursor.getString(cursor.getColumnIndex(PointDao.INNER_CODE)));
            String XYZS = cursor.getString(cursor.getColumnIndex(PointDao.XYZS));
            if (!TextUtils.isEmpty(XYZS)) {
                String[] list = XYZS.split("/|#");
                if (list.length > 0) {
                    ((TextView)view.findViewById(R.id.x)).setText(list[0]);
                }
                if (list.length > 1) {
                    ((TextView)view.findViewById(R.id.y)).setText(list[1]);
                }
                if (list.length > 2) {
                    ((TextView)view.findViewById(R.id.z)).setText(list[2]);
                }

            }
            String time = cursor.getString(cursor.getColumnIndex(PointDao.MTIME));
            if (!TextUtils.isEmpty(time)) {
                ((TextView)view.findViewById(R.id.measure_time)).setText(time);
            }
            View measure = view.findViewById(R.id.measure);
            long pointId = cursor.getLong(cursor.getColumnIndex(PointDao.ID));
            measure.setTag(pointId);
            measure.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final long id = (Long)v.getTag();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            String where = PointDao.ID + "='" + id + "'";
                            ContentValues values = BlueToothManager.getInstance(null).measure();
                            PointDao.update(values, where, null);

                            Cursor c = SurveyorDao.query(null);
                            if (c != null & mSectCode != null) {
                                try {
                                    if (c.moveToNext()) {
                                        String surveyorName = c.getString(c
                                                .getColumnIndex(SurveyorDao.SURVEYOR_NAME));
                                        String surveyorID = c.getString(c
                                                .getColumnIndex(SurveyorDao.SURVEYOR_ID));

                                        values.clear();
                                        values.put(SectionDao.SURVEYOR_NAME, surveyorName);
                                        values.put(SectionDao.SURVEYOR_ID, surveyorID);
                                        String where2 = SectionDao.SECTION_CODE + "='" + mSectCode
                                                + "'";
                                        SectionDao.update(values, where2, null);
                                    }
                                } catch (SQLiteException e) {
                                    c.close();
                                }
                            } else {
                                Log.w("crtb.PointActivity",
                                        "can't update due tomSectCode == null or c == null");
                            }
                            refresh();
                        }

                    }).start();

                }
            });

            view.setTag(null);

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.point_list_item, parent, false);

            return view;
        }

    }

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
                view = convertView;
            }

            TextView pointNameView = (TextView)view.findViewById(R.id.point_name);
            pointNameView.setText(mPointsList[position]);
            // Null tag means the view has the correct data
            view.setTag(null);

            return view;
        }
    }

    public void refresh() {
        new MyAsyncTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case MENU_REFRESH:
                refresh();
                break;
            case MENU_BT_SETTING:
                Intent intent = new Intent(this, BlueToothSearch.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_REFRESH, 0, R.string.refresh);
        menu.add(0, MENU_BT_SETTING, 0, R.string.bt_setting);
        return super.onPrepareOptionsMenu(menu);
    }

    private final static int MENU_REFRESH = 0;

    private final static int MENU_BT_SETTING = 1;
}
