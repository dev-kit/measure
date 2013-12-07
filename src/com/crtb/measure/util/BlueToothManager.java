
package com.crtb.measure.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.crtb.measure.AppContext;
import com.crtb.measure.R;
import com.crtb.measure.data.PointDao;

public class BlueToothManager {
    public static int TMC_MEA_INC = 0;

    public static int TMC_AUTO_INC = 1;

    public static int TMC_PLANE_INC = 2;

    public static int TMC_APRIORI_INC = 3;

    public static int TMC_ADJ_INC = 4;

    public static int TMC_REQUIRE_INC = 5;

    private BluetoothAdapter mBTAdapt;

    public static BluetoothSocket mBTSocket;

    private static BlueToothManager sBlueToothManager;

    private static Context sContext;

    private static long mWaitTime = 10000;

    private static String TEST_RET = "%R1P,0,0:0,1000,200,3000,123456";

    // ASCII-Request
    // %R1Q,2082:WaitTime[long],Mode[long]
    // ASCII-Response
    // %R1P,0,0:RC,E[double],N[double],H[double],CoordTime[long],
    // E-Cont[double],N-Cont[double],H-Cont[double],CoordContTime[long]
    private static String COMMAND_MEASURE = "%R1Q,2082:" + mWaitTime + TMC_AUTO_INC;

    private BlueToothManager() {
        mBTAdapt = BluetoothAdapter.getDefaultAdapter();
    }

    public static synchronized BlueToothManager getInstance(Context context) {
        if (sBlueToothManager == null) {
            sBlueToothManager = new BlueToothManager();
            sContext = context;
        }
        return sBlueToothManager;
    }

    public synchronized void setBTSocket(BluetoothSocket btSocket) {
        mBTSocket = btSocket;
    }

    public BluetoothSocket getBTSocket() {
        return mBTSocket;
    }

    public int getState() {
        return mBTAdapt.getState();
    }

    private boolean ensureConnect() {
        if (mBTSocket == null) {
            return false;
        }
        //
        // if (!mBTSocket.isConnected()) {
        // try {
        // mBTSocket.connect();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // Toast.makeText(sContext, R.string.bt_connect_error,
        // Toast.LENGTH_SHORT).show();
        // return false;
        // }
        // }
        return true;
    }

    public synchronized ContentValues measure() {
        final byte[] buffer = new byte[1024];
        final Coordinate testObject = new Coordinate(null);
        if (ensureConnect()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        OutputStream out;
                        InputStream input;
                        out = mBTSocket.getOutputStream();
                        input = mBTSocket.getInputStream();
                        out.write(COMMAND_MEASURE.getBytes());
                        input.read(buffer);
                        testObject.E = 1;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
//                        Toast.makeText(sContext, R.string.bt_connect_error, Toast.LENGTH_SHORT)
//                                .show();
                    } finally {
                        synchronized (buffer) {
                            buffer.notify();
                        }
                    }

                }

            }).start();
            synchronized (buffer) {
                try {
                    buffer.wait(mWaitTime * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (AppContext.mTest && testObject.E != 1) {
            return new Coordinate(String.valueOf(TEST_RET)).toContentValues();
        }

        return new Coordinate(String.valueOf(buffer)).toContentValues();

    }

    public class Coordinate {
        double E;

        double N;

        double H;

        long CoordTime;

        static final String INTERVAL = "#";

        Coordinate(String ret) {
            if (!TextUtils.isEmpty(ret)) {
                String[] rets = ret.split(",");
                // compare GRC_OK
                if (rets[2].equals("0:0")) {
                    E = Double.parseDouble(rets[3]);
                    N = Double.parseDouble(rets[4]);
                    H = Double.parseDouble(rets[5]);
                    CoordTime = Long.parseLong(rets[6]);
                }
            }
        }

        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            String xyzs = E + INTERVAL + N + INTERVAL + H;
            values.put(PointDao.XYZS, xyzs);
            values.put(PointDao.MTIME, CoordTime);
            return values;
        }
    }

}
