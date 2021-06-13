package com.example.auotmaticattendance.Account_Access_Section;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.auotmaticattendance.MainActivity;
import com.example.auotmaticattendance.Profile_section.EditProfileActivity;
import com.example.auotmaticattendance.Profile_section.MyProfileActivity;
import com.example.auotmaticattendance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Depart_PhoneActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private Button mconfirm;
    private Spinner mdepartment;
    private EditText mphone;
    DatabaseReference databaseReference;
    FirebaseAuth mauth;
    String department,phoneno,gender,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depart__phone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mauth=FirebaseAuth.getInstance();
        userid=mauth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        mdepartment=findViewById(R.id.department);
        mphone=findViewById(R.id.phone);
        mconfirm=findViewById(R.id.confirm);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.depart_arrays, R.layout.spinner_text_color);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mdepartment.setAdapter(adapter);
        Bundle bundle = getIntent().getExtras();
         gender= bundle.getString("gender");

        mconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneno = mphone.getText().toString();
                department = mdepartment.getSelectedItem().toString();
                if (!phoneno.isEmpty()) {
                    progressDialog = new ProgressDialog(Depart_PhoneActivity.this);
                    progressDialog.setTitle("Completing Your Profile");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    Map userinfo = new HashMap();
                    userinfo.put("phone", phoneno);
                    userinfo.put("department", department);
                    userinfo.put("gender", gender);
                    databaseReference.updateChildren(userinfo);
                    Intent intent = new Intent(Depart_PhoneActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(Depart_PhoneActivity.this, "Profile Completed!", Toast.LENGTH_SHORT).show();
                    overridePendingTransition(R.anim.rightstart, R.anim.leftend);
                    finish();
                }
            }
        });


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

}