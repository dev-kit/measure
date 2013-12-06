
package com.crtb.measure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.crtb.measure.util.BlueToothManager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BlueToothSearch extends Activity {
    static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    Button btnSearch, btnDis, btnExit;

    ToggleButton tbtnSwitch;

    ListView lvBTDevices;

    ArrayAdapter<String> adtDevices;

    List<String> lstDevices = new ArrayList<String>();

    BluetoothAdapter btAdapt;

    public static BluetoothSocket btSocket;

    static AcceptThread sAcceptThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_search);

        btnSearch = (Button)this.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new ClickEvent());
        btnExit = (Button)this.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ClickEvent());
        btnDis = (Button)this.findViewById(R.id.btnDis);
        btnDis.setOnClickListener(new ClickEvent());

        tbtnSwitch = (ToggleButton)this.findViewById(R.id.tbtnSwitch);
        tbtnSwitch.setOnClickListener(new ClickEvent());

        lvBTDevices = (ListView)this.findViewById(R.id.lvDevices);
        adtDevices = new ArrayAdapter<String>(BlueToothSearch.this,
                android.R.layout.simple_list_item_1, lstDevices);
        lvBTDevices.setAdapter(adtDevices);
        lvBTDevices.setOnItemClickListener(new ItemClickEvent());

        btAdapt = BluetoothAdapter.getDefaultAdapter();

        if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)
            tbtnSwitch.setChecked(false);
        else if (btAdapt.getState() == BluetoothAdapter.STATE_ON)
            tbtnSwitch.setChecked(true);

        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(searchDevices, intent);

//        if (sAcceptThread == null) {
//            sAcceptThread = new AcceptThread();
//            sAcceptThread.start();
//        }
    }

    private BroadcastReceiver searchDevices = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e(keyName, String.valueOf(b.get(keyName)));
            }

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String str = device.getName() + "|" + device.getAddress();
                if (lstDevices.indexOf(str) == -1)
                    lstDevices.add(str);
                adtDevices.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(searchDevices);
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            btAdapt.cancelDiscovery();
            String str = lstDevices.get(arg2);
            String[] values = str.split("\\|");
            String address = values[1];
            Log.e("address", values[1]);
            UUID uuid = UUID.fromString(SPP_UUID);
            BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
            try {
                if (btSocket != null) {
                    btSocket.close();
                }
                btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
                btSocket.connect();
                BlueToothManager.getInstance(BlueToothSearch.this.getApplicationContext())
                        .setBTSocket(btSocket);
                Log.v("benson", "client get accepted");
                Toast.makeText(BlueToothSearch.this, R.string.bt_setup, 1000).show();
                BlueToothSearch.this.finish();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(BlueToothSearch.this, R.string.bt_connect_error, 1000).show();
            }

        }

    }

    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnSearch)//
            {
                if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {
                    Toast.makeText(BlueToothSearch.this, R.string.open_bt, 1000).show();
                    return;
                }
                setTitle(R.string.bt_title + btAdapt.getAddress());
                lstDevices.clear();
                adtDevices.notifyDataSetChanged();
                btAdapt.startDiscovery();
            } else if (v == tbtnSwitch) {
                if (tbtnSwitch.isChecked() == false)
                    btAdapt.enable();
                else if (tbtnSwitch.isChecked() == true)
                    btAdapt.disable();
            } else if (v == btnDis) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
            } else if (v == btnExit) {
                try {
                    if (btSocket != null)
                        btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BlueToothSearch.this.finish();
            }
        }

    }

    /**
     * Blue tooth server
     */

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client
                // code
                UUID uuid = UUID.fromString(SPP_UUID);
                tmp = btAdapt.listenUsingRfcommWithServiceRecord("aaaa", uuid);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            Log.v("benson", "server online");
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                    Log.v("benson", "server accept");
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    byte[] buffer = "aaaaa benson  fffffff".getBytes();
                    try {
                        socket.getOutputStream().write(buffer);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // Do work to manage the connection (in a separate thread)
                    // manageConnectedSocket(socket);
                    // mmServerSocket.close();
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
