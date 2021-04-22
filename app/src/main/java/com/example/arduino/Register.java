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



public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String name,email,password,vehicleno,phoneno;
    EditText nameet;
    EditText emailet;
    EditText passwordet;
    EditText vehiclenoet;
    EditText phonenoet;
    Button btnreg;


    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameet=(EditText)findViewById(R.id.etNAMEreg);
        emailet=(EditText)findViewById(R.id.etEMAILreg);
        passwordet=(EditText)findViewById(R.id.etPASSWORDreg);
        vehiclenoet=(EditText)findViewById(R.id.etVEHICLENOreg);
        phonenoet=(EditText)findViewById(R.id.etPHONEreg);
        btnreg=(Button)findViewById(R.id.btREGISTERreg);

        name=nameet.getText().toString();
        email=emailet.getText().toString();
        password=passwordet.getText().toString();
        vehicleno=vehiclenoet.getText().toString();
        phoneno=phonenoet.getText().toString();

        mAuth = FirebaseAuth.getInstance();


        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                name=nameet.getText().toString();
//                email=emailet.getText().toString();
//                password=passwordet.getText().toString();
//                vehicleno=vehiclenoet.getText().toString();
//                phoneno=phonenoet.getText().toString();

                email=emailet.getText().toString();
                password=passwordet.getText().toString();

                System.out.println(email+"========"+password);

                createAccount(email,password);

            }
        });



    }

     void createAccount(String emailx, String passwordx) {
        // [START create_user_with_email]

        mAuth.createUserWithEmailAndPassword(emailx, passwordx)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(Register.this, "Registration Success.", Toast.LENGTH_LONG).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            String authkey=user.getUid();

                            //critical code
                                    Intent intent=new Intent(Register.this,MainActivity.class);
                                    Bundle b = new Bundle();

                                    name=nameet.getText().toString();
//                                    email=emailet.getText().toString();
//                                    password=passwordet.getText().toString();
                                    vehicleno=vehiclenoet.getText().toString();
                                    phoneno=phonenoet.getText().toString();

                                    b.putString("KEYEXTRA",authkey);
                                    b.putString("NAME",name);
                                    b.putString("VEHICLENO",vehicleno);
                                    b.putString("PHONENO",phoneno);

                                    intent.putExtras(b);

                                    startActivity(intent);
                            //end critical code


                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                                Toast.makeText(Register.this, "VERIFICATION EMAIL SENT.",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                            System.out.println("registration successful of "+user);


                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }


}