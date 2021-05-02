package com.example.arduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogIN extends AppCompatActivity {

    private static final String TAG = "LOGIN";
    Button login;
    Button register;
    EditText email;
    EditText password;
    String authkey;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_i_n);


        login = (Button) findViewById(R.id.bLOGIN);
        register = (Button) findViewById(R.id.bREGISTER);
        email = (EditText) findViewById(R.id.etEMAIL);
        password = (EditText) findViewById(R.id.etPASSWORD);
        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                String pas = password.getText().toString();
                signIn(em, pas);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIN.this, Register.class);
                startActivity(intent);

            }
        });


    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            String mdn=currentUser.getEmail();
//            String authkey=currentUser.getUid();
//            System.out.println(mdn);
//
//            Toast.makeText(LogIN.this, " logged in successfully!.",
//                    Toast.LENGTH_LONG).show();
//
//
////            Intent intent=new Intent(LogIN.this,MainActivity.class);
////            intent.putExtra("INTENTEXTRA",mdn);
////            intent.putExtra("KEYEXTRA",authkey);
////            startActivity(intent);
//        }
//    }


    private void createAccount(String email, String password) {
        // [START create_user_with_email]

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(LogIN.this, "Registration Success.",
                                    Toast.LENGTH_SHORT).show();


                            FirebaseUser user = mAuth.getCurrentUser();


                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                                Toast.makeText(LogIN.this, "VERIFICATION EMAIL SENT.",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                            System.out.println(user);


                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LogIN.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String emailid = user.getEmail();
                            authkey = user.getUid();
                            System.out.println(emailid);
                            dataretrive(authkey);


                        } else {

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogIN.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent

                        Toast.makeText(LogIN.this, "VERIFICATION EMAIL SENT.",
                                Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void dataretrive(String authhkey) {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cities = databaseRef.child("USERS");
        // Query citiesQuery = cities.orderByKey();
        Query citiesQuery = cities.orderByChild("Keym").equalTo(authhkey);

        citiesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String[] arstringz;
                String[] arvehiclenoplatez;
                String[] arphonenoz;

                List<String> lstnamez = new ArrayList<String>();
                List<String> lstvehiclenoplatez = new ArrayList<String>();
                List<String> lstphonez = new ArrayList<String>();

                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                        lstnamez.add(postSnapshot.child("Name").getValue().toString());
                        lstvehiclenoplatez.add(postSnapshot.child("Vehicleno").getValue().toString());
                        lstphonez.add(postSnapshot.child("Phone").getValue().toString());
                    }
                }


                arstringz = new String[lstnamez.size()];
                arvehiclenoplatez = new String[lstvehiclenoplatez.size()];
                arphonenoz = new String[lstphonez.size()];

                for (int i = 0; i < lstphonez.size(); ++i) {

                    arstringz[i] = lstnamez.get(i);
                    arvehiclenoplatez[i] = lstvehiclenoplatez.get(i);
                    arphonenoz[i] = lstphonez.get(i);

                }

                Intent intent = new Intent(LogIN.this, BluetoothActivity.class);

                Bundle b = new Bundle();

                b.putString("KEYEXTRA", authkey);
                b.putString("NAME", arstringz[0]);
                b.putString("VEHICLENO", arvehiclenoplatez[0]);
                b.putString("PHONENO", arphonenoz[0]);

                intent.putExtras(b);
                startActivity(intent);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}