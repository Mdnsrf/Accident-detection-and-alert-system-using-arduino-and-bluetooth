package com.example.arduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        Intent intentss=getIntent();
        String accidentpersonkey=intentss.getStringExtra("ACCPERSONKEY");


        //critical code

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cities = databaseRef.child("USERS");
        // Query citiesQuery = cities.orderByKey();
        Query citiesQuery = cities.orderByChild("keym").equalTo(accidentpersonkey);


        citiesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for get alert
                double[] arlatz;
                double[] arlongiz;
                String[] arstringz;
                String[] arkeyz;
                String[] araccidentpersonkeyz;

                List<String> lstnamez = new ArrayList<String>();
                List<String> lstlatz = new ArrayList<String>();
                List<String> lstlongiz = new ArrayList<String>();
                List<String> lstkeymz = new ArrayList<String>();
                List<String>lstaccidentpersonkeyz= new ArrayList<String>();

                //end for getalert

                if(snapshot.exists()){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        lstlatz.add(postSnapshot.child("Lat").getValue().toString());
                        lstnamez.add(postSnapshot.child("Name").getValue().toString());
                        lstlongiz.add(postSnapshot.child("Long").getValue().toString());
                        lstkeymz.add(postSnapshot.child("keym").getValue().toString());
                        lstaccidentpersonkeyz.add(postSnapshot.child("accident").getValue().toString());
                    }
                }
                databaseRef.removeEventListener(this);

                arlatz = new double[lstlatz.size()];
                arlongiz = new double[lstlongiz.size()];
                arstringz = new String[lstnamez.size()];
                arkeyz = new String[lstkeymz.size()];
                araccidentpersonkeyz=new String[lstaccidentpersonkeyz.size()];


                for (int i = 0; i < lstlatz.size(); ++i) {
                    arlatz[i] = Double.parseDouble(lstlatz.get(i));
                    arlongiz[i] = Double.parseDouble(lstlongiz.get(i));
                    arstringz[i] = lstnamez.get(i);
                    arkeyz[i]=lstkeymz.get(i);
                    araccidentpersonkeyz[i]=lstaccidentpersonkeyz.get(i);
                }

                System.out.println("AlertActivity:===inside alert actity===");
                if(arkeyz.length>0)
                {
                    System.out.println("AlertActivity:===key of accident person===:"+arkeyz[0]);
                    if(arkeyz[0].equals(accidentpersonkey))
                    {
                        Intent intens=new Intent(AlertActivity.this,MapsActivity.class);

                        Bundle b = new Bundle();

                        b.putDouble("ACPLAT",arlatz[0]);
                        b.putDouble("ACPLONG",arlongiz[0]);
                        b.putString("ACPNAME",arstringz[0]);

                        intens.putExtras(b);

                        startActivity(intens);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("FAILED to retrive data of nearest person");
            }
        });
        //end critical code

    }
}





//                        intens.putExtra("ACPNAME",arstringz[0]);
//                        intens.putExtra("ACPLAT",arlatz[0]);
//                        intens.putExtra("ACPLONGI",arlongiz[0]);