package com.example.arduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyCustomInterface {


    public double[] arlat;
    public double[] arlongi;
    public String[] arstring;
    public String minperson;
    public double[] distance;
    public double minimum = 0.0;
    public int minimumindex = 0;

    public double latitude;
    public double longitude;
    public TextView tvnearestperson;
    public EditText personname;
    public TextView tv, tv2, tv3;
    public Button btn_1;
    public Button btn_2;
    public Button btn_3;
    public Button accidenttriggerbtn;
    public String keyintent;
    public TextView tvgeolocation;
    int indexofkeyelement;
    private GpsTracker gpsTracker;
    private TextView tvLatitude, tvLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // View view = new View(this);

        tvgeolocation = (TextView) findViewById(R.id.tvGEOLOCATION);
        tvLatitude = (TextView) findViewById(R.id.tv_lat);
        tvLongitude = (TextView) findViewById(R.id.tv_long);
        tvnearestperson = (TextView) findViewById(R.id.tv_minperson);
        //  personname = (EditText) findViewById(R.id.editTextTextPersonName);


        Bundle b = getIntent().getExtras();


        keyintent = b.getString("KEYEXTRA").trim();
        String nameintent = b.getString("NAME");
        String vehicleintent = b.getString("VEHICLENO");
        String phoneintent = b.getString("PHONENO");

        System.out.println(nameintent);
        System.out.println(keyintent);

        System.out.println(vehicleintent);
        System.out.println(phoneintent);


        if (b.containsKey("ALERTINTENTEXTRA")) {
            getLocations();

            retrivedatadb obj = new retrivedatadb(MainActivity.this, MainActivity.this);
            minperson = obj.retrievedatafromdb(latitude, longitude);
        }


        // personname.setText(emailintent);
        // tvnearestperson.setText("WELCOME " + nameintent);
        tvnearestperson.setMovementMethod(new ScrollingMovementMethod());


        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        Button btnterminal = (Button) findViewById(R.id.btn_terminal);
        accidenttriggerbtn = (Button) findViewById(R.id.bTEMPACCIDENT);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        accidenttriggerbtn.setOnClickListener(new View.OnClickListener() {//BUTTON FOR ACCIDENT TRIGGER
            @Override
            public void onClick(View v) {


                retrivedatadb obj = new retrivedatadb(MainActivity.this, MainActivity.this);
                minperson = obj.retrievedatafromdb(latitude, longitude);
                // tvnearestperson.setText(minperson);


            }
        });


        btn_1.setOnClickListener(new View.OnClickListener() {//BUTTON FOR GET LOCATION
            @Override
            public void onClick(View v) {
                getLocations();


            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {//BUTTON FOR INSERT
            @Override
            public void onClick(View v) {

                Insert b = new Insert();
                b.insert2database(keyintent, latitude, longitude, nameintent, "no", phoneintent, vehicleintent);//method of Insert class
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {//BUTTON FOR RETRIVE
            @Override
            public void onClick(View v) {
                retrivedatadb obj = new retrivedatadb(MainActivity.this, MainActivity.this);
                minperson = obj.retrievedatafromdb(latitude, longitude);
                // tvnearestperson.setText(minperson);
            }
        });
        Insert bc = new Insert();

        //critical code insert


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                getLocations();

                getgeolocation();
                Insert b = new Insert();
                b.insert2database(keyintent, latitude, longitude, nameintent, "no", phoneintent, vehicleintent);//method of Insert class
                //  handler.postDelayed(this, 1000L * 60 * 60);  // 1 second delay=1000L 20sec=20000L
                handler.postDelayed(this, 20000L);  // 1 second delay=1000L 20sec=20000L


            }
        };
        handler.post(runnable);


        btnterminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tts = new Intent(MainActivity.this, LedControl.class);
                startActivity(tts);
            }
        });

        //end critical code for insert


        //critical code


        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cities = databaseRef.child("USERS");
        // Query citiesQuery = cities.orderByKey();
        Query citiesQuery = cities.orderByChild("Isalerted").equalTo("yes");


        citiesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Intent intentss = getIntent();
                String emailintent = intentss.getStringExtra("INTENTEXTRA");
                keyintent = intentss.getStringExtra("KEYEXTRA");


                //for get alert
                double[] arlatz;
                double[] arlongiz;
                String[] arstringz;
                String[] arkeyz;
                String[] araccidentpersonkeyz;
                String[] arvehiclenoplatez;
                String[] aremailz;
                String[] arphonenoz;

                List<String> lstnamez = new ArrayList<String>();
                List<String> lstlatz = new ArrayList<String>();
                List<String> lstlongiz = new ArrayList<String>();
                List<String> lstkeymz = new ArrayList<String>();
                List<String> lstaccidentpersonkeyz = new ArrayList<String>();

                List<String> lstvehiclenoplatez = new ArrayList<String>();

                List<String> lstphonez = new ArrayList<String>();


                //end for getalert

                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        lstlatz.add(postSnapshot.child("Lat").getValue().toString());
                        lstnamez.add(postSnapshot.child("Name").getValue().toString());
                        lstlongiz.add(postSnapshot.child("Long").getValue().toString());
                        lstkeymz.add(postSnapshot.child("Keym").getValue().toString());
                        lstaccidentpersonkeyz.add(postSnapshot.child("Accident").getValue().toString());

                        lstvehiclenoplatez.add(postSnapshot.child("Vehicleno").getValue().toString());
                        lstphonez.add(postSnapshot.child("Phone").getValue().toString());
                    }
                }

                arlatz = new double[lstlatz.size()];
                arlongiz = new double[lstlongiz.size()];
                arstringz = new String[lstnamez.size()];
                arkeyz = new String[lstkeymz.size()];
                araccidentpersonkeyz = new String[lstaccidentpersonkeyz.size()];

                arvehiclenoplatez = new String[lstvehiclenoplatez.size()];
                arphonenoz = new String[lstphonez.size()];


                for (int i = 0; i < lstlatz.size(); ++i) {
                    arlatz[i] = Double.parseDouble(lstlatz.get(i));
                    arlongiz[i] = Double.parseDouble(lstlongiz.get(i));
                    arstringz[i] = lstnamez.get(i);
                    arkeyz[i] = lstkeymz.get(i);
                    araccidentpersonkeyz[i] = lstaccidentpersonkeyz.get(i);

                    arvehiclenoplatez[i] = lstvehiclenoplatez.get(i);
                    arphonenoz[i] = lstphonez.get(i);

                }


                System.out.println("alerted keys no============" + arkeyz.length);

                for (int i = 0; i < arkeyz.length; i++) {

                    if (arkeyz[i].equals(keyintent)) {
                        System.out.println(arkeyz[0]);
                        if (arkeyz[0].equals(keyintent) && !araccidentpersonkeyz[0].equals("no")) {
                            Intent inten = new Intent(MainActivity.this, AlertActivity.class);

                            Insert b2 = new Insert();
                            // b2.insert2database(arkeyz[0],arlatz[0],arlongiz[0],arstringz[0],arkeyz[0],"no");//method of Insert class
                            b2.insert2database(arkeyz[i], arlatz[i], arlongiz[i], arstringz[i], "no", arphonenoz[i], arvehiclenoplatez[i]);//method of Insert class

                            inten.putExtra("ACCPERSONKEY", araccidentpersonkeyz[i]);
                            System.out.println("Main Act:" + araccidentpersonkeyz[i] + "====");
                            startActivity(inten);
                        }
                    }
                }
//                if (arkeyz.length > 0) {
//                    System.out.println(arkeyz[0]);
//                    if (arkeyz[0].equals(keyintent) && !araccidentpersonkeyz[0].equals("no")) {
//                        Intent inten = new Intent(MainActivity.this, AlertActivity.class);
//
//                        Insert b2 = new Insert();
//                        // b2.insert2database(arkeyz[0],arlatz[0],arlongiz[0],arstringz[0],arkeyz[0],"no");//method of Insert class
//                        b2.insert2database(arkeyz[0], arlatz[0], arlongiz[0], arstringz[0], "no", arphonenoz[0], arvehiclenoplatez[0]);//method of Insert class
//
//                        inten.putExtra("ACCPERSONKEY", araccidentpersonkeyz[0]);
//                        System.out.println("Main Act:" + araccidentpersonkeyz[0] + "====");
//                        startActivity(inten);
//                    }
//
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("FAILED to retrive data of nearest person");
            }
        });


        //end critical code


        getLocations();
    }//END ON CREATE

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLocations();
        Bundle b = getIntent().getExtras();
        keyintent = b.getString("KEYEXTRA").trim();
        String nameintent = b.getString("NAME");
        String vehicleintent = b.getString("VEHICLENO");
        String phoneintent = b.getString("PHONENO");

        Insert be = new Insert();
        be.insert2databasefinal(keyintent, latitude, longitude, nameintent, "no", phoneintent, vehicleintent);//method of Insert class
    }

    public void tempaccfn() {

        retrivedatadb obj = new retrivedatadb(MainActivity.this, MainActivity.this);
        minperson = obj.retrievedatafromdb(latitude, longitude);
    }


    public void getLocations() {

        gpsTracker = new GpsTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            System.out.println(latitude + "  ======  " + longitude);
            tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));

        } else {
            gpsTracker.showSettingsAlert();
        }
        System.out.println("============insied get location=========");
    }


    @Override
    public void sendData(double[] latf, double[] lonf, String[] namef, String[] keynf, double distancef[], String phonef[], String vehiclenof[]) {


        for (int i = 0; i < keynf.length; i++) {
            System.out.println("\nindex:" + i + " key:" + keynf[i] + "" + " name:" + namef[i] + "" + " distance:" + distancef[i]);
        }


        if (namef.length > 0) {

            indexofkeyelement = findindexofkeyelement(keynf);

            int minindex = FindSmallest(distancef, keynf);

            String minindexstring = String.valueOf(minindex);
            //tvnearestperson.append("minimum index="+minindex);
            tvnearestperson.append("Alerting!..\n");
            tvnearestperson.append(namef[minindex]);


            //critical code

            String npkey = keynf[minindex];
            double nplat = latf[minindex];
            double nplong = lonf[minindex];
            String npname = namef[minindex];

            String npphoneno = phonef[minindex];
            String npvehicleno = vehiclenof[minindex];

            System.out.println("Before UPDATE::" + npkey + " " + nplat + " " + nplong + " " + npname + " " + npphoneno + " " + npvehicleno);


            Insert a = new Insert();
            a.update2database(npkey, nplat, nplong, npname, keyintent, npphoneno, npvehicleno);


            //end of critical code


        }


    }

    private int findindexofkeyelement(String[] keynf) {
        for (int i = 0; i < keynf.length; i++) {
            if (keynf[i].equals(keyintent)) {
                return i;
            }
        }
        return 0;
    }

    public int FindSmallest(double[] arr1, String[] arrstr) {
        int index = 0;
        double min = 99999.9999;

        Bundle bd = getIntent().getExtras();
        String keyka = bd.getString("KEYEXTRA");


        for (int i = 0; i < arr1.length; i++) {

            if (arr1[i] <= min && !(arrstr[i].equals(keyka))) {

                min = arr1[i];
                index = i;
            }


        }
        return index;
    }

    void getgeolocation() {

        try {

            getLocations();
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                tvgeolocation.setText("Waiting for Location");
            } else {
                tvgeolocation.setText(addresses.get(0).getPremises() + " ,"
                        + addresses.get(0).getSubLocality()
                        + " ," + addresses.get(0).getLocality()
                        + ", " + addresses.get(0).getAdminArea()
                        + " ," + addresses.get(0).getPostalCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
