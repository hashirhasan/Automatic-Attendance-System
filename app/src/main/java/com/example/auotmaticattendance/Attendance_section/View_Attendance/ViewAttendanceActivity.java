package com.example.auotmaticattendance.Attendance_section.View_Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.auotmaticattendance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceActivity extends AppCompatActivity {


    private Spinner mbranch,msubject;
    private RecyclerView mrecyclerView;
    private RecyclerView.Adapter mviewattendadapter;
    private  RecyclerView.LayoutManager mviewattendlayoutManager;
    private ProgressBar mprogressbar;
    private String branch,subject;
    private TextView emptylist;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        setTitle("Students Attendance");
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        mbranch=findViewById(R.id.branch);
        msubject=findViewById(R.id.subject);
        emptylist=findViewById(R.id.emptylist);
        mprogressbar=findViewById(R.id.progressBar);
        mrecyclerView=findViewById(R.id.recyclerView);
        mrecyclerView.setNestedScrollingEnabled(false);
        mrecyclerView.setHasFixedSize(true);
        mviewattendlayoutManager=new LinearLayoutManager(ViewAttendanceActivity.this);
        mrecyclerView.setLayoutManager(mviewattendlayoutManager);
        mviewattendadapter=new view_attenAdapter(getdatasetviewattend(),ViewAttendanceActivity.this);
        branch = mbranch.getSelectedItem().toString();
        subject = msubject.getSelectedItem().toString();
//        getstudendid();
        mrecyclerView.setAdapter(mviewattendadapter);


        mbranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                resultsViewAttend.clear();
                mviewattendadapter.notifyDataSetChanged();
                emptylist.setVisibility(View.INVISIBLE);
                mprogressbar.setVisibility(View.VISIBLE);
                String selectedBranch = (String) mbranch.getItemAtPosition(i);
                branch=selectedBranch;
                getstudendid();
                mviewattendadapter.notifyDataSetChanged();
                count=1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         msubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                 if (count == 1) {
                     resultsViewAttend.clear();
                     mviewattendadapter.notifyDataSetChanged();
                     emptylist.setVisibility(View.INVISIBLE);
                     mprogressbar.setVisibility(View.VISIBLE);
                     String selectedSubject = (String) msubject.getItemAtPosition(i);
                     subject = selectedSubject;
                     getstudendid();
                     mviewattendadapter.notifyDataSetChanged();
                 }
             }
             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });


    }


    private void getstudendid() {

        DatabaseReference userdb= FirebaseDatabase.getInstance().getReference().child("Students").child("1").child(branch);
        userdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot match:dataSnapshot.getChildren())
                    {
                        fetchstudentsinfo(match.getKey());
                    }
                }else{
                    emptylist.setVisibility(View.VISIBLE);
                    mprogressbar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchstudentsinfo(String key){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Students").child("1").child(branch).child(key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String rollno=snapshot.getKey();
                    String name=(String) snapshot.child("name").getValue();
                   String attended=String.valueOf(snapshot.child("subjects").child(subject).child("Attended").getValue());
                   String totalclass= String.valueOf(snapshot.child("subjects").child(subject).child("Total Classes").getValue());
                    viewobject obk=new viewobject(rollno,name,attended,totalclass);
                    resultsViewAttend.add(obk);
                }
                mviewattendadapter.notifyDataSetChanged();
                mprogressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private ArrayList<viewobject> resultsViewAttend=new ArrayList<viewobject>();
    private List<viewobject> getdatasetviewattend() {
        return resultsViewAttend;
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