package com.example.arduino;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements MyCustomInterface {


    public double[] arlat;
    public double[] arlongi;
    public String[] arstring;
    public String minperson;
    public  double[] distance;
    public  double minimum=0.0;
  //  public int minimumindex=0;

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


        Intent intentss=getIntent();
        String emailintent=intentss.getStringExtra("INTENTEXTRA");
         keyintent=intentss.getStringExtra("KEYEXTRA");


       // personname.setText(emailintent);
        tvnearestperson.setText(emailintent);
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




        accidenttriggerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


       btn_1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getLocations();
           }
       });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Insert a = new Insert();

               String namepara= personname.getText().toString();
                a.insert2database(keyintent,latitude,longitude,emailintent,keyintent,"no");//method of Insert class
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrivedatadb obj=new retrivedatadb(MainActivity.this,MainActivity.this);
                 minperson=obj.retrievedatafromdb(latitude,longitude);
                 tvnearestperson.setText(minperson);
            }
        });
//
//                Intent intent=new Intent(MainActivity.this,LogIN.class);
//                startActivity(intent);


    }


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
        System.out.println("insied get location=========");
}


    @Override
    public void sendData(double[] latf, double[] lonf, String[] namef,String keynf[],double distancef[]) {

        if(namef.length>0){

            tvnearestperson.setText(namef[0]);

            int minindex=FindSmallest(distancef,keynf);

            //tvnearestperson.setText(minindex);
            String minindexstring=String.valueOf(minindex);
            tvnearestperson.append(minindexstring);
        }





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

            if (arr1[i] < min && !arrstr[i].equals(keyintent)) {
                min = arr1[i];
                index = i;
            }

        }
        return index;
    }
}