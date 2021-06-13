package com.example.auotmaticattendance.Account_Access_Section;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auotmaticattendance.MainActivity;
import com.example.auotmaticattendance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {


    private Button msignup;
    private TextView mlogin;
    private EditText mEmail,mPassword,mName,mphone;
    private Spinner mdepartment;
    private ConstraintLayout layout;
    private Boolean wificonnected=false,mobileconnectd=false;

    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Register");
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    Intent intent=new Intent(getApplication(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.rightstart,R.anim.leftend);
                    finish();
                    return;
                }
            }
        };
        mauth= FirebaseAuth.getInstance();
        mlogin=findViewById(R.id.login);
        msignup=findViewById(R.id.signup);
        mName= findViewById(R.id.name);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mphone=findViewById(R.id.phone);
        mdepartment=findViewById(R.id.department);
        layout=findViewById(R.id.layout);
        msignup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                        final String name = mName.getText().toString();
                        final String email = mEmail.getText().toString();
                        final String password = mPassword.getText().toString();
                        final String phone = mphone.getText().toString();
                        final String department = mdepartment.getSelectedItem().toString();

                        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                        if(networkInfo!=null && networkInfo.isConnected())
                        {
                            Log.d("tag","sdfgfdsdfdsdfd");
                            wificonnected=networkInfo.getType()== ConnectivityManager.TYPE_WIFI;
                            mobileconnectd=networkInfo.getType()== ConnectivityManager.TYPE_MOBILE;

                            if(wificonnected || mobileconnectd) {
                                if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !phone.isEmpty()) {
                                mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            mauth.fetchSignInMethodsForEmail(email).
                                                    addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                                            boolean check = !task.getResult().getSignInMethods().isEmpty();
                                                            if (!check) {
                                                                Toast.makeText(SignupActivity.this, "Sign In Error", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(SignupActivity.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            String userId = mauth.getCurrentUser().getUid();
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                            HashMap userinfo = new HashMap();
                                            userinfo.put("email", email);
                                            userinfo.put("name", name);
                                            userinfo.put("gender", "default");
                                            userinfo.put("phone", phone);
                                            userinfo.put("department", department);
                                            userinfo.put("profilepic", "default");
                                            Toast.makeText(SignupActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                            databaseReference.updateChildren(userinfo);
                                        }
                                    }
                                });
                            }
                            }
                        }else{
                            Toast.makeText(SignupActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.rightstart,R.anim.leftend);
                finish();
                return;
            }
        });
            }

            @Override
            protected void onStart() {
                super.onStart();
                mauth.addAuthStateListener(firebaseAuthStateListener);
            }

            @Override
            protected void onStop() {
                super.onStop();
                mauth.removeAuthStateListener(firebaseAuthStateListener);
            }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==android.R.id.home) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.leftstart,R.anim.rightend);
        }
        return super.onOptionsItemSelected(item);
    }
    public void hidekeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}