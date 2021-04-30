package com.example.arduino;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    ListView devicelist;
    AlertDialog.Builder builder1;

    String keyintent;
    String nameintent;
    String vehicleintent;
    String phoneintent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        Bundle b = getIntent().getExtras();
         keyintent = b.getString("KEYEXTRA");
         nameintent  = b.getString("NAME");
         vehicleintent = b.getString("VEHICLENO");
         phoneintent = b.getString("PHONENO");


        Button btnPaired;
        Button btnskppair;
        builder1 = new AlertDialog.Builder(this);

        btnPaired = (Button) findViewById(R.id.button);
        btnskppair = (Button) findViewById(R.id.buttonskippair);
        devicelist = (ListView) findViewById(R.id.listView);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if ( myBluetooth==null ) {
            Toast.makeText(getApplicationContext(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
            //finish();


            builder1.setMessage("Bluetooth not available on your device.No one will be able to spot you but you'll still receive contingency alerts from other users.Still want to continue?").setTitle("continue without bluetooth?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "continue",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent i = new Intent(BluetoothActivity.this, MainActivity.class);

                            Bundle b = new Bundle();
                            b.putString("KEYEXTRA",keyintent);
                            b.putString("NAME",nameintent);
                            b.putString("VEHICLENO",vehicleintent);
                            b.putString("PHONENO",phoneintent);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();




        }
        else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });


        btnskppair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("No one will be able to spot you but you'll still receive contingency alerts from other users.").setTitle("continue without bluetooth?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(BluetoothActivity.this, MainActivity.class);

                                Bundle b = new Bundle();
                                b.putString("KEYEXTRA",keyintent);
                                b.putString("NAME",nameintent);
                                b.putString("VEHICLENO",vehicleintent);
                                b.putString("PHONENO",phoneintent);
                                i.putExtras(b);
                                startActivity(i);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



            }
        });

    }

    private void pairedDevicesList () {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if ( pairedDevices.size() > 0 ) {
            for ( BluetoothDevice bt : pairedDevices ) {
                list.add(bt.getName().toString() + "\n" + bt.getAddress().toString());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length()-17);

            Intent i = new Intent(BluetoothActivity.this, LedControl.class);
            i.putExtra(EXTRA_ADDRESS, address);
            Bundle b = new Bundle();
            b.putString("KEYEXTRA",keyintent);
            b.putString("NAME",nameintent);
            b.putString("VEHICLENO",vehicleintent);
            b.putString("PHONENO",phoneintent);
            i.putExtras(b);
            startActivity(i);




        }
    };
}

//import androidx.appcompat.app.AppCompatActivity;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.Intent;
//import android.os.Bundle;
//import android.service.controls.Control;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Set;
//import java.util.UUID;
//
//public class MainActivity extends AppCompatActivity {
//
//    private UUID UUID = null;
//    // Initializing the Adapter for bluetooth
//    private BluetoothAdapter BluetoothAdap = null;
//    private Set Devices;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // comes in Oncreate method of the activity
//        BluetoothAdap = BluetoothAdapter.getDefaultAdapter();
//
//
//    }













