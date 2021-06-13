package com.example.auotmaticattendance.Attendance_section.Upload_Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.auotmaticattendance.R;

public class selectbranchsubActivity extends AppCompatActivity {


    Spinner mbranch,msubject;
    String branch,subject;
    private Button mtakeAttendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectbranchsub);
        mbranch=findViewById(R.id.branch);
        msubject=findViewById(R.id.subject);
        mtakeAttendance=findViewById(R.id.takeAttendance);

        mbranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i
                    , long id) {
                String selectedBranch = (String) mbranch.getItemAtPosition(i);
                branch=selectedBranch;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        msubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                String selectedSubject = (String) msubject.getItemAtPosition(i);
                subject=selectedSubject;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mtakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectbranchsubActivity.this, UploadAttendanceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("branch",branch);
                bundle.putString("subject",subject);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.rightstart,R.anim.leftend);
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