package com.example.arduino;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

//keyz
//latitude
//longitude
//name
//accident
//phoneno
//vehicleno

public class Insert {
    public void insert2database(String keyz,double latitude,double longitude,String name,String accident,String phoneno,String vehicleno)
    {
        HashMap<String,String> student=new HashMap<>();

        student.put("Keym",keyz);

        student.put("Lat", String.valueOf(latitude));   //JUST CHANGE VALUE HERE
        student.put("Long", String.valueOf(longitude));   //JUST CHANGE VALUE HERE
        student.put("Name",name);  //JUST CHANGE VALUE HERE
        student.put("Phone", phoneno);   //JUST CHANGE VALUE HERE
        student.put("Accident",accident);
        student.put("Isalerted","no");
        student.put("Vehicleno",vehicleno);

//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference tasksRef = rootRef.child("USERS").push();
//        tasksRef.setValue(student);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef = rootRef.child("USERS").child(keyz);
        tasksRef.setValue(student);
        return;

    }
//keyz
//latitude
//longitude
//name
//accident
//phoneno
//vehicleno

    public void update2database(String keyzz,double latitudee,double longitudee,String namee,String accidentt,String phonenoo,String vehiclenoo)
    {
        HashMap<String,String> student=new HashMap<>();

        student.put("Keym",keyzz);
        student.put("Lat", String.valueOf(latitudee));   //JUST CHANGE VALUE HERE
        student.put("Long", String.valueOf(longitudee));   //JUST CHANGE VALUE HERE
        student.put("Name",namee);  //JUST CHANGE VALUE HERE
        student.put("Accident",accidentt);

        student.put("Phone", phonenoo);   //JUST CHANGE VALUE HERE
        student.put("Vehicleno",vehiclenoo);

        student.put("Isalerted","yes");


        DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef2 = rootRef2.child("USERS").child(keyzz);
        tasksRef2.setValue(student);
        return;
    }








}




