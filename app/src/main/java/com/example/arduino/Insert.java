package com.example.arduino;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class Insert {
    public void insert2database(double latitude,double longitude,String name)
    {
        HashMap<String,String> student=new HashMap<>();

        student.put("Name",name);  //JUST CHANGE VALUE HERE
        student.put("Lat", String.valueOf(latitude));   //JUST CHANGE VALUE HERE
        student.put("Long", String.valueOf(longitude));   //JUST CHANGE VALUE HERE
        student.put("Phone", String.valueOf("11181111"));   //JUST CHANGE VALUE HERE

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef = rootRef.child("USERS").push();
        tasksRef.setValue(student);
    }
}




