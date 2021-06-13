package com.example.auotmaticattendance.Account_Access_Section;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auotmaticattendance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FogotPassActivity extends AppCompatActivity {

    private Button mforgetbutton;
    private String email;
    private EditText mail;
    private TextView msigninText;
    FirebaseAuth mauth;
    ProgressBar progressBar;
    LinearLayout forgotLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_pass);
        setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mforgetbutton=findViewById(R.id.forgetpass);
        mail=findViewById(R.id.email);
        progressBar=findViewById(R.id.progressBar);
        forgotLayout=findViewById(R.id.forgotlayout);
        msigninText=findViewById(R.id.logintext);
        mauth=FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);


        msigninText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FogotPassActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.leftstart,R.anim.rightend);
                finish();
            }
        });
        mforgetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Validate();
            }

        });
    }

    private void Validate() {
         email=mail.getText().toString();
         if(email.isEmpty())
         {
             mail.setError("Email Required");
         }else{
             forgotpass();
         }
    }

    private void forgotpass() {
        forgotLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mauth.sendPasswordResetEmail(email).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(FogotPassActivity.this, "Check ur email", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(FogotPassActivity.this,LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            forgotLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(FogotPassActivity.this, "erro: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() ==android.R.id.home) {
//            super.onBackPressed();
//            finish();
//            overridePendingTransition(R.anim.leftstart,R.anim.rightend);
//        }
//        return super.onOptionsItemSelected(item);
//    }
    public void hidekeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}