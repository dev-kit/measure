
package com.crtb.measure.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.crtb.measure.R;

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

    private long mWaitTime = 10000;

    private static String TEST_RET = "%R1P,0,0:0,1000,200,3000,123456";
    // ASCII-Request
    // %R1Q,2082:WaitTime[long],Mode[long]
    // ASCII-Response
    // %R1P,0,0:RC,E[double],N[double],H[double],CoordTime[long],
    // E-Cont[double],N-Cont[double],H-Cont[double],CoordContTime[long]
    private static String COMMAND_MEASURE = "%R1Q,2082:" + 10000 + TMC_AUTO_INC;

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

    public void setBTSocket(BluetoothSocket btSocket) {
        mBTSocket = btSocket;
    }

    public BluetoothSocket getBTSocket() {
        return mBTSocket;
    }

    public int getState() {
        return mBTAdapt.getState();
    }

    private void ensureConnect() {
        if (!mBTSocket.isConnected()) {
            try {
                mBTSocket.connect();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(sContext, R.string.bt_connect_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Coordinate measure() {
        ensureConnect();
        OutputStream out;
        InputStream input;
        byte[] buffer = new byte[1024];
        try {
            out = mBTSocket.getOutputStream();
            input = mBTSocket.getInputStream();
            out.write(COMMAND_MEASURE.getBytes());
            input.read(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(sContext, R.string.bt_connect_error, Toast.LENGTH_SHORT).show();
        }
//        parseRetCode(TEST_RET);
        return parseRetCode(String.valueOf(buffer));
        // int input = mBTSocket.getInputStream().read();
        // while (input != -1) {
        // Log.v("benson", "client get " + input);
        // input = mBTSocket.getInputStream().read();
        // }
    }

    public class Coordinate {
        private double E;

        private double N;

        private double H;

        private long CoordTime;
    }

    private Coordinate parseRetCode(String ret) {
        Coordinate coordinate = null;
        if (!TextUtils.isEmpty(ret)) {
            String[] rets = ret.split(",");
            //compare GRC_OK 
            if (rets[2].equals("0:0")) {
                coordinate = new Coordinate();
                coordinate.E = Double.parseDouble(rets[3]);
                coordinate.N = Double.parseDouble(rets[4]);
                coordinate.H = Double.parseDouble(rets[5]);
                coordinate.CoordTime = Long.parseLong(rets[6]);
            }
        }
        return coordinate;

    }
}
