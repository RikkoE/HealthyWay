package com.example.rikko.healthyway;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    ArrayAdapter<String> btArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView out = (TextView) findViewById(R.id.out);

        //final Button turnON = (Button) findViewById(R.id.turnON);
        final Button scan = (Button) findViewById(R.id.scan);
        //final Button turnOFF = (Button) findViewById(R.id.turnOFF);

        final ToggleButton toggleBlue = (ToggleButton) findViewById(R.id.toggleBluetooth);

        final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        btArrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1);
        final ListView device_list = (ListView) findViewById(R.id.devicesfound);
        device_list.setAdapter(btArrayAdapter);

        boolean btOn = false;

        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    //discovery starts, we can show progress dialog or perform other tasks
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    //discovery finishes, dismiss progress dialog
                } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    //bluetooth device found
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    //Toast.makeText(getApplicationContext(),"Found device " + device.getName() +"\n" +device.getAddress(),Toast.LENGTH_LONG).show();
                    btArrayAdapter.add(device.getName() + "\n"+device.getAddress());
                    btArrayAdapter.notifyDataSetChanged();
                }
            }
        };

        btOn = bluetooth.isEnabled();

        if (!btOn) {
            toggleBlue.setChecked(false);
        } else {
            toggleBlue.setChecked(true);
        }

        toggleBlue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!bluetooth.isEnabled()) {
                        Toast.makeText(getApplicationContext(),
                                "Turning on Bluetooth", Toast.LENGTH_LONG).show();
                        // Intent enableBtIntent = new
                        // Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
                    }
                } else {
                    btArrayAdapter.clear();
                    bluetooth.disable();
                    Toast.makeText(getApplicationContext(),
                            "Turning off Bluetooth", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*turnON.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!bluetooth.isEnabled()) {
                    Toast.makeText(getApplicationContext(),
                            "Turning ON Bluetooth", Toast.LENGTH_LONG);
                    // Intent enableBtIntent = new
                    // Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
                }
            }
        });

        turnOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                btArrayAdapter.clear();
                bluetooth.disable();
                Toast.makeText(getApplicationContext(),
                        "TURNING OFF BLUETOOTH", Toast.LENGTH_LONG);
            }
        });
        */

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (bluetooth.isEnabled()) {
                    btArrayAdapter.clear();
                    IntentFilter filter = new IntentFilter();
                    Toast.makeText(getApplicationContext(),
                            "Starting scan...", Toast.LENGTH_LONG).show();
                    bluetooth.startDiscovery();

                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

                    registerReceiver(mReceiver, filter);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Turn on Bluetooth to scan", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}