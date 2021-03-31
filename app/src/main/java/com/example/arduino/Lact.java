package com.example.arduino;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.core.Tag;

public class Lact extends AppCompatActivity {

    //    Button btnlogin=(Button)findViewById(R.id.btnLogin);
//    Button btnRegister=(Button)findViewById(R.id.btnSignUp);
    EditText edemail=(EditText)findViewById(R.id.etEmail);
    EditText edpassword=(EditText)findViewById(R.id.etPassword);
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnlogin=(Button)findViewById(R.id.btnLogin);
        Button btnRegister=(Button)findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edemail.getText().toString();
                String password=edpassword.getText().toString();

                signIn(email,password);

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email=edemail.getText().toString();
                String password=edpassword.getText().toString();


                createAccount(email,password);


            }
        });

    }


    private void createAccount(String email, String password) {
        // [START create_user_with_email]

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println(user);
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Lact.this, "Authentication failed.",
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
                            System.out.println(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Lact.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

}