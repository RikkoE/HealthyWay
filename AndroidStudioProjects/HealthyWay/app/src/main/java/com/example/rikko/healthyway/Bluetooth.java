package com.example.rikko.healthyway;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;



public class Bluetooth extends Activity {


    private static Bluetooth bt;
    private static final int ENABLE_BLUETOOTH_REQUEST = 1;

    public Bluetooth() {
        bt=this;
    }

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
      }
        }
    };

    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

    public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
    }

    public static void turnOn() {
        bt.bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!bt.bluetooth.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            bt.startActivityForResult(enableBluetoothIntent,0);
        }
    }

    public void discovery() {
        if (!bluetooth.isDiscovering()) {
            startActivityForResult(new Intent(
                    BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE), 0);

        }
    }

    public void scan() {
        IntentFilter filter = new IntentFilter();
        bluetooth.startDiscovery();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);

    }


    public void turnOff() {
        bluetooth.disable();
    }
}
