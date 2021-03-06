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

public class LogIN extends AppCompatActivity {

    Button login;
    Button register;
    EditText email;
    EditText password;

    private FirebaseAuth mAuth;

    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_i_n);


        login=(Button)findViewById(R.id.bLOGIN);
        register=(Button)findViewById(R.id.bREGISTER);
        email=(EditText)findViewById(R.id.etEMAIL);
        password=(EditText)findViewById(R.id.etPASSWORD);
        mAuth = FirebaseAuth.getInstance();






        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em=email.getText().toString();
                String pas=password.getText().toString();
               signIn(em,pas);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em=email.getText().toString();
                String pas=password.getText().toString();
                createAccount(em,pas);
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String mdn=currentUser.getEmail();
            String authkey=currentUser.getUid();
            System.out.println(mdn);

            Toast.makeText(LogIN.this, "Already logged in.",
                    Toast.LENGTH_LONG).show();


//            Intent intent=new Intent(LogIN.this,MainActivity.class);
//            intent.putExtra("INTENTEXTRA",mdn);
//            intent.putExtra("KEYEXTRA",authkey);
//            startActivity(intent);
        }
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
                            Toast.makeText(LogIN.this, "Authentication failed.",
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

                            String mdn=user.getEmail();
                            String authkey=user.getUid();


                            System.out.println(mdn);

                            Intent intent=new Intent(LogIN.this,MainActivity.class);
                            intent.putExtra("INTENTEXTRA",mdn);
                            intent.putExtra("KEYEXTRA",authkey);
                            startActivity(intent);

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogIN.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

}