package com.example.arduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyCustomInterface {


    public double[] arlat;
    public double[] arlongi;
    public String[] arstring;
    public String minperson;
    public  double[] distance;
    public  double minimum=0.0;
    public int minimumindex=0;

    public double latitude;
    public double longitude;

    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude;
    public TextView tvnearestperson;
    public EditText personname;
    public TextView tv,tv2,tv3;
    public Button btn_1;
    public Button btn_2;
    public Button btn_3;
    public Button accidenttriggerbtn;
    String keyintent;


//    //for get alert
//    public double[] arlatz;
//    public double[] arlongiz;
//    public String[] arstringz;
//    public String[] arkeyz;
//
//    List<String> lstnamez = new ArrayList<String>();
//    List<String> lstlatz = new ArrayList<String>();
//    List<String> lstlongiz = new ArrayList<String>();
//    List<String> lstkeymz = new ArrayList<String>();
//
//    //end for getalert





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


       // View view = new View(this);
        tvLatitude=(TextView)findViewById(R.id.tv_lat);
        tvLongitude=(TextView)findViewById(R.id.tv_long);
        tvnearestperson=(TextView)findViewById(R.id.tv_minperson);
        personname=(EditText)findViewById(R.id.editTextTextPersonName);

//
//        Intent intentss=getIntent();
//        String emailintent=intentss.getStringExtra("INTENTEXTRA");
//         keyintent=intentss.getStringExtra("KEYEXTRA");

        Bundle b = getIntent().getExtras();


               keyintent = b.getString("KEYEXTRA");
        String nameintent  = b.getString("NAME");
        String vehicleintent = b.getString("VEHICLENO");
        String phoneintent = b.getString("PHONENO");






       // personname.setText(emailintent);
        tvnearestperson.setText("WELCOME "+nameintent);
        tvnearestperson.setMovementMethod(new ScrollingMovementMethod());



        btn_1=(Button)findViewById(R.id.btn_1);
        btn_2=(Button)findViewById(R.id.btn_2);
        btn_3=(Button)findViewById(R.id.btn_3);
        accidenttriggerbtn=(Button)findViewById(R.id.bTEMPACCIDENT);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }




        accidenttriggerbtn.setOnClickListener(new View.OnClickListener() {//BUTTON FOR ACCIDENT TRIGGER
            @Override
            public void onClick(View v) {


                retrivedatadb obj=new retrivedatadb(MainActivity.this,MainActivity.this);
                minperson=obj.retrievedatafromdb(latitude,longitude);
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
                b.insert2database(keyintent,latitude,longitude,nameintent,"no",phoneintent,vehicleintent);//method of Insert class
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {//BUTTON FOR RETRIVE
            @Override
            public void onClick(View v) {
                retrivedatadb obj=new retrivedatadb(MainActivity.this,MainActivity.this);
                 minperson=obj.retrievedatafromdb(latitude,longitude);
                // tvnearestperson.setText(minperson);
            }
        });


        //critical code


        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cities = databaseRef.child("USERS");
        // Query citiesQuery = cities.orderByKey();
        Query citiesQuery = cities.orderByChild("Isalerted").equalTo("yes");


        citiesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Intent intentss=getIntent();
                String emailintent=intentss.getStringExtra("INTENTEXTRA");
                keyintent=intentss.getStringExtra("KEYEXTRA");


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
                List<String>lstaccidentpersonkeyz= new ArrayList<String>();

                List<String>lstvehiclenoplatez=new ArrayList<String>();

                List<String>lstphonez=new ArrayList<String>();


                //end for getalert

                if(snapshot.exists()){
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
                araccidentpersonkeyz=new String[lstaccidentpersonkeyz.size()];

                arvehiclenoplatez=new String[lstvehiclenoplatez.size()];
                arphonenoz=new String[lstphonez.size()];


                for (int i = 0; i < lstlatz.size(); ++i) {
                    arlatz[i] = Double.parseDouble(lstlatz.get(i));
                    arlongiz[i] = Double.parseDouble(lstlongiz.get(i));
                    arstringz[i] = lstnamez.get(i);
                    arkeyz[i]=lstkeymz.get(i);
                    araccidentpersonkeyz[i]=lstaccidentpersonkeyz.get(i);

                    arvehiclenoplatez[i]=lstvehiclenoplatez.get(i);
                    arphonenoz[i]=lstphonez.get(i);

                }


                System.out.println("alerted keys no---=-=-=-=-============"+arkeyz.length);
                if(arkeyz.length>0)
                {
                    System.out.println(arkeyz[0]);
                    if(arkeyz[0].equals(keyintent))
                    {
                        Intent inten=new Intent(MainActivity.this,AlertActivity.class);

                        Insert b2 = new Insert();
                       // b2.insert2database(arkeyz[0],arlatz[0],arlongiz[0],arstringz[0],arkeyz[0],"no");//method of Insert class
                        b2.insert2database(arkeyz[0],arlatz[0],arlongiz[0],arstringz[0],"no",arphonenoz[0],arvehiclenoplatez[0]);//method of Insert class

                        inten.putExtra("ACCPERSONKEY", araccidentpersonkeyz[0]);
                        System.out.println("Main Act:"+araccidentpersonkeyz[0]+"====");
                        startActivity(inten);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("FAILED to retrive data of nearest person");
            }
        });



        //end critical code






        getLocations();
    }//END ON CREATE










    public void getLocations(){

        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            System.out.println(latitude+"  ======  "+longitude);
            tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));

        }
        else {
            gpsTracker.showSettingsAlert();
        }
        System.out.println("============insied get location=========");
}


    @Override
    public void sendData(double[] latf, double[] lonf, String[] namef,String keynf[],double distancef[],String phonef[],String vehiclenof[]) {

        if(namef.length>0){

            int minindex=FindSmallest(distancef,keynf);

            String minindexstring=String.valueOf(minindex);
            tvnearestperson.append(minindexstring+" ");
            tvnearestperson.append(namef[minindex]);

            //critical code

            String npkey=keynf[minindex];
            double nplat=latf[minindex];
            double nplong=lonf[minindex];
            String npname=namef[minindex];

            String npphoneno=phonef[minindex];
            String npvehicleno=vehiclenof[minindex];




            Insert a = new Insert();
            a.update2database(npkey,nplat,nplong,npname,keyintent,npphoneno,npvehicleno);




            //end of critical code





        }




//
//        Bundle b = new Bundle();
//        b.putDoubleArray("latf", latf);
//        b.putDoubleArray("longf", lonf);
//        b.putStringArray("namef", namef);
//        Intent i=new Intent(this,MapsActivity.class);
//        i.putExtras(b);
//        startActivity(i);

    }

    public int FindSmallest(double[] arr1,String[] arrstr ) {
        int index = 0;
        double min = arr1[index];

        for (int i = 1; i < arr1.length; i++) {

            if (arr1[i] < min && !(arrstr[i].equals(keyintent))) {

                min = arr1[i];
                index = i;
            }

        }
        return index;
    }
}