package com.example.auotmaticattendance.Attendance_section;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.auotmaticattendance.Attendance_section.View_Attendance.ViewAttendanceActivity;
import com.example.auotmaticattendance.Attendance_section.Upload_Attendance.selectbranchsubActivity;
import com.example.auotmaticattendance.R;

public class AttendanceuploadviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendanceuploadview);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LinearLayout muploadattendance=findViewById(R.id.uploadattend);
        LinearLayout mviewattendance=findViewById(R.id.viewattend);

        muploadattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AttendanceuploadviewActivity.this, selectbranchsubActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.rightstart,R.anim.leftend);
                return;
            }
        });

        mviewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ViewAttendanceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.rightstart,R.anim.leftend);
                return;
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