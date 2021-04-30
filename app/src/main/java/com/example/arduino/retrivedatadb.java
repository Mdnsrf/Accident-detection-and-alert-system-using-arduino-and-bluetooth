package com.example.arduino;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.ChildKey;

import java.util.ArrayList;
import java.util.List;

public class retrivedatadb {

    Context context;
    public retrivedatadb(Context context,MyCustomInterface otherNameInterface){
        this.context=context;
        this.otherNameInterface = otherNameInterface;
    }

    private MyCustomInterface otherNameInterface;

//for get alert
    public double[] arlatz;
    public double[] arlongiz;
    public String[] arstringz;
    public String[] arkeyz;

    List<String> lstnamez = new ArrayList<String>();
    List<String> lstlatz = new ArrayList<String>();
    List<String> lstlongiz = new ArrayList<String>();
    List<String> lstkeymz = new ArrayList<String>();

    //end for getalert




    public double[] arlat;
    public double[] arlongi;
    public String[] arstring;
    public String[] arkey;
    public double[] distance;

    public String[] arphoneno;
    public String[] arvehicleno;


    public double minimum = 0.0;
    public int minimumindex = 0;

    public double latitude;
    public double longitude;

    List<String> lstname = new ArrayList<String>();
    List<String> lstlat = new ArrayList<String>();
    List<String> lstlongi = new ArrayList<String>();
    List<String> lstkeym = new ArrayList<String>();

    List<String> lstphone=new ArrayList<String>();
    List<String> lstvehicleno=new ArrayList<String>();




    String returnstring = " ";


    String retrievedatafromdb(double latitudecld, double longitudecld) {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cities = databaseRef.child("USERS");
      // Query citiesQuery = cities.orderByKey();
//        Query citiesQuery = cities.orderByChild("accident").equalTo("yes");
       Query citiesQuery = cities.orderByChild("Name");

        citiesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    lstlat.add(postSnapshot.child("Lat").getValue().toString());
                    lstname.add(postSnapshot.child("Name").getValue().toString());
                    lstlongi.add(postSnapshot.child("Long").getValue().toString());
                    lstkeym.add(postSnapshot.child("Keym").getValue().toString());

                    lstphone.add(postSnapshot.child("Phone").getValue().toString());
                    lstvehicleno.add(postSnapshot.child("Vehicleno").getValue().toString());


                }
                databaseRef.removeEventListener(this);


//                for (String elem : lstname) {
//                    System.out.println("Element : " + elem);
//                }
//                System.out.println(lstname.size() + " " + lstlat.size() + " " + lstlongi.size());


                arlat = new double[lstlat.size()];
                arlongi = new double[lstlongi.size()];
                arstring = new String[lstname.size()];
                arkey = new String[lstkeym.size()];
                distance = new double[lstlat.size()];

                arphoneno=new String[lstphone.size()];
                arvehicleno=new  String[lstphone.size()];

                for (int i = 0; i < lstlat.size(); ++i) {
                    arlat[i] = Double.parseDouble(lstlat.get(i));
                    arlongi[i] = Double.parseDouble(lstlongi.get(i));
                    arstring[i] = lstname.get(i);
                    arkey[i]=lstkeym.get(i);

                    arphoneno[i]=lstphone.get(i);
                    arvehicleno[i]=lstvehicleno.get(i);


                    distance[i] = distance(latitudecld, longitudecld, arlat[i], arlongi[i], 'K');

                }

                System.out.println("latitude :" + arlat.length + " long:" + arlongi.length + " string:" + arstring.length + " distance:" + distance.length+" key:"+arkey.length);


                for (double di : distance) {
                    System.out.print(di + "   ");

                }

                if (distance.length != 0) {
                    minimumindex = FindSmallest(distance);//finding minimum index
                    System.out.println("=====minimum index=====" + minimumindex);
                    System.out.println("=====minimum person name=====" + arstring[minimumindex]);
                    returnstring = returnstring.concat(arstring[minimumindex]);
                } else {
                    System.out.println("null distance array");
                }


//                TextView txtView = (TextView) ((Activity)context).findViewById(R.id.tv_minperson);
//                txtView.setText(returnstring);

//                for (int i = 0; i < arlat.length; i++) {
//                    txtView.append("\n Distance "+String.format("%.2f",distance[i])+" KM "+arstring[i]);
//
//                }

                otherNameInterface.sendData(arlat,arlongi,arstring,arkey,distance,arphoneno,arvehicleno);



            }//end on data changed

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });//end single value event listener()


        return returnstring;
    }//end retrieve data from db()




//    void getlocationalertmethod (){
//
//
//
//
//
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference cities = databaseRef.child("USERS");
//       // Query citiesQuery = cities.orderByKey();
//       Query citiesQuery = cities.orderByChild("isalerted").equalTo("yes");
//
//
//       citiesQuery.addValueEventListener(new ValueEventListener() {
//           @Override
//           public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//               for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                   lstlatz.add(postSnapshot.child("Lat").getValue().toString());
//                   lstnamez.add(postSnapshot.child("Name").getValue().toString());
//                   lstlongiz.add(postSnapshot.child("Long").getValue().toString());
//                   lstkeymz.add(postSnapshot.child("keym").getValue().toString());
//               }
//
//               arlatz = new double[lstlatz.size()];
//               arlongiz = new double[lstlongiz.size()];
//               arstringz = new String[lstnamez.size()];
//               arkeyz = new String[lstkeymz.size()];
//
//               for (String ky:arkeyz) {
//                   if(ky.equals("j")){
//
//                   }
//               }
//
//
//           }
//
//           @Override
//           public void onCancelled(@NonNull DatabaseError error) {
//
//           }
//       });
//
//
//    }
















    public int FindSmallest(double[] arr1) {
        int index = 0;
        double min = arr1[index];

        for (int i = 1; i < arr1.length; i++) {

            if (arr1[i] < min) {
                min = arr1[i];
                index = i;
            }

        }
        return index;
    }


    public double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}




        /*
                for (int i=0;i<3;i++)
                {
                    distance[i]=distance(latitude,longitude,arlat[i], arlongi[i], 'K');
                }

                minimum=distance[0];
                for(int x=0;x<arlat.length;x++)
                {
                    if(distance[x]<minimum)
                    {
                        minimum=distance[x];
                        minimumindex=x;
                    }
                }   */

//tv2.setText(String.valueOf(arstring[minimumindex]));








//        databaseRef.orderByKey().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebasefailed", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebasesuccess", String.valueOf(task.getResult().getValue()));
//                    Log.d("firebasesuccess", String.valueOf(task.getResult().child("USERS").child()));
//            }
//        });
//        return "";
//    }


//    {
//
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference cities = databaseRef.child("USERS");
//        Query citiesQuery = cities.orderByKey();
//
//
//
//        citiesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    lstlat.add(postSnapshot.child("Lat").getValue().toString());
//                    lstname.add(postSnapshot.child("Name").getValue().toString());
//                    lstlongi.add(postSnapshot.child("Long").getValue().toString());
//
//                }
//
//                    databaseRef.removeEventListener(this);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//              returnstring= returnstring.concat("no data recieved");
//            }
//        });
//
//        //to convert latitudes and longitudes from string to long and convert all 3 lists to Array and also get distance
//        arlat = new double[lstlat.size()];
//        arlongi = new double[lstlongi.size()];
//        arstring=new String[lstname.size()];
//        distance=new double[lstlat.size()];
//
//        for (int i = 0; i < lstlat.size(); ++i) {
//            arlat[i] = Double.parseDouble(lstlat.get(i));
//            arlongi[i] = Double.parseDouble(lstlongi.get(i));
//            arstring[i]=lstname.get(i);
//            System.out.println(arstring[0]);
//            distance[i]=distance(latitudecld,longitudecld,arlat[i], arlongi[i], 'K');
//            returnstring= returnstring.concat(arstring[i]);
//        }
//
//        for (int i=0;i<arlat.length;i++)
//        {
//            System.out.println("latitude:"+arlat[i]+" longitude:"+arlongi[i]+" name:"+arstring[i]);
//        }
//
//        if (distance.length!=0)
//          minimumindex = FindSmallest(distance);//finding minimum index
//        else
//            returnstring=  returnstring.concat("\nnull distance array");
//
//
//
//        if(minimumindex>0)
//             return arstring[minimumindex];
//        else
//            return "could not retrive data"+returnstring;
//    }
