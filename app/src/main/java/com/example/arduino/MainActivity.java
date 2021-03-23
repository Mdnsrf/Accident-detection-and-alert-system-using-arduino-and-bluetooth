package com.example.arduino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


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

        tvnearestperson.setMovementMethod(new ScrollingMovementMethod());



        btn_1=(Button)findViewById(R.id.btn_1);
        btn_2=(Button)findViewById(R.id.btn_2);
        btn_3=(Button)findViewById(R.id.btn_3);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


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
                a.insert2database(latitude,longitude,namepara);//method of Insert class
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrivedatadb obj=new retrivedatadb(MainActivity.this);
                 minperson=obj.retrievedatafromdb(latitude,longitude);
                 tvnearestperson.setText(minperson);
            }
        });




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



}