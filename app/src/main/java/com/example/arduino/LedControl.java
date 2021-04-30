package com.example.arduino;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class LedControl extends AppCompatActivity {
    String address = null;

    String keyintent;
    String nameintent;
    String vehicleintent;
    String phoneintent;



    Button btn1, btn2, btn3, btn4, btn5, btnDis;
    //String address = null;
    TextView lumn;
    TextView recievetxt;

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;
    private InputStream inputStream;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_control);


        Bundle b = getIntent().getExtras();
         keyintent = b.getString("KEYEXTRA");
         nameintent  = b.getString("NAME");
         vehicleintent = b.getString("VEHICLENO");
         phoneintent = b.getString("PHONENO");

        Intent intent = getIntent();
        address = intent.getStringExtra(BluetoothActivity.EXTRA_ADDRESS);

        btn1 =  findViewById(R.id.button2);
        btn2 =  findViewById(R.id.button3);
        //For additional actions to be performed
        btn3 =  findViewById(R.id.button5);
        btn4 =  findViewById(R.id.button6);
        btn5 =  findViewById(R.id.button7);
        btnDis = findViewById(R.id.button4);
        lumn =  findViewById(R.id.textView2);
        recievetxt = findViewById(R.id.tv_recieve);
        recievetxt.setMovementMethod(new ScrollingMovementMethod());

        new LedControl.ConnectBT().execute();


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sendSignal("1");
                //  beginListenForData();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sendSignal("2");
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sendSignal("3");
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sendSignal("4");
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                // sendSignal("5");
                recievetxt.setText("");

                Intent i = new Intent(LedControl.this, AlertActivity.class);
                //i.putExtra(EXTRA_ADDRESS, address);
                startActivity(i);
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Disconnect();
            }
        });
    }

    private void sendSignal ( String number ) {
        if ( btSocket != null ) {
            try {
                btSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void Disconnect () {
        if ( btSocket!=null ) {
            try {
                btSocket.close();
            } catch(IOException e) {
                msg("Error");
            }
        }

        finish();
    }

    private void msg (String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    public class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;
        private ProgressDialog progress;

        @Override
        protected  void onPreExecute () {
            progress = ProgressDialog.show(LedControl.this, "Connecting...", "Please Wait!!!");
        }

        @Override
        protected Void doInBackground (Void... devices) {
            try {
                if ( btSocket==null || !isBtConnected ) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();

//                    my added code
                    if(btSocket!=null){
                        // beginListenForData();
                    }
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected");
                isBtConnected = true;
            }
            beginListenForData();
            progress.dismiss();

            Intent i = new Intent(LedControl.this,MainActivity.class);
            Bundle b = new Bundle();
            b.putString("KEYEXTRA",keyintent);
            b.putString("NAME",nameintent);
            b.putString("VEHICLENO",vehicleintent);
            b.putString("PHONENO",phoneintent);

         //   b.putString("ALERTINTENTEXTRA","yes");

            i.putExtras(b);
            startActivity(i);
        }



    }
    void beginListenForData()
    {

        try {
            inputStream=btSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];



        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {
                                    recievetxt.append(string);
                                    System.out.println("------------"+string);
                                    if(string.equals("A")){
//                                        Intent i = new Intent(LedControl.this, AlertActivity.class);
//                                        startActivity(i);

//                                        MainActivity aa=new MainActivity();
//                                        aa.tempaccfn();

                                        Intent i = new Intent(LedControl.this,MainActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString("KEYEXTRA",keyintent);
                                        b.putString("NAME",nameintent);
                                        b.putString("VEHICLENO",vehicleintent);
                                        b.putString("PHONENO",phoneintent);

                                        b.putString("ALERTINTENTEXTRA","yes");

                                        i.putExtras(b);
                                        startActivity(i);


                                    }
                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }
}