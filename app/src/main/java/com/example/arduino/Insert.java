package com.example.arduino;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class Insert {
    public void insert2database(String keyz,double latitude,double longitude,String name,String keym,String accident)
    {
        HashMap<String,String> student=new HashMap<>();

        student.put("Name",name);  //JUST CHANGE VALUE HERE
        student.put("Lat", String.valueOf(latitude));   //JUST CHANGE VALUE HERE
        student.put("Long", String.valueOf(longitude));   //JUST CHANGE VALUE HERE
        student.put("Phone", String.valueOf("11181111"));   //JUST CHANGE VALUE HERE
        student.put("accident",accident);
        student.put("isalerted","no");
        student.put("keym",keym);
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference tasksRef = rootRef.child("USERS").push();
//        tasksRef.setValue(student);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef = rootRef.child("USERS").child(keyz);
        tasksRef.setValue(student);

    }


    public void update2database(String keyzz,double latitudee,double longitudee,String namee,String accidentt)
    {
        HashMap<String,String> student=new HashMap<>();

        student.put("Name",namee);  //JUST CHANGE VALUE HERE
        student.put("Lat", String.valueOf(latitudee));   //JUST CHANGE VALUE HERE
        student.put("Long", String.valueOf(longitudee));   //JUST CHANGE VALUE HERE
        student.put("Phone", String.valueOf("11181111"));   //JUST CHANGE VALUE HERE
        student.put("accident",accidentt);
        student.put("isalerted","yes");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef = rootRef.child("USERS").child(keyzz);
        tasksRef.setValue(student);
    }






}




