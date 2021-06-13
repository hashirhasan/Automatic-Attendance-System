package com.example.auotmaticattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.auotmaticattendance.Account_Access_Section.ChooseLoginRegistrationActivity;
import com.example.auotmaticattendance.Account_Access_Section.GenderActivity;
import com.example.auotmaticattendance.Attendance_section.AttendanceuploadviewActivity;
import com.example.auotmaticattendance.Profile_section.MyProfileActivity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mauth;
    private DatabaseReference databaseReference;
  TextView mgentext;
  private LinearLayout mprofile,mattendance,mdashboard;
  private  TextView personname;
//    private TextView username;
    private String userid,gender,name;
    private Animation pulse;
    ProgressBar progressBar;
    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       setTitle("Dashboard");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

// Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        mauth=FirebaseAuth.getInstance();
        userid=mauth.getCurrentUser().getUid();
        personname=findViewById(R.id.personname);
       mprofile= (LinearLayout) findViewById(R.id.profile);
      mattendance=(LinearLayout) findViewById(R.id.attendance);
      mdashboard=(LinearLayout) findViewById(R.id.dashboard);
      mgentext=findViewById(R.id.gender);
      progressBar=findViewById(R.id.progressBar);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
      progressBar.setVisibility(View.VISIBLE);
      mdashboard.setVisibility(View.INVISIBLE);
        pulse = AnimationUtils.loadAnimation(this, R.anim.blink);
        progressBar.startAnimation(pulse);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            name = bundle.getString("name");
            if (!name.equals("")) {
                personname.setText("Hi,  " + name);
            }
        }
        mprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MyProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.rightstart,R.anim.leftend);
                return;
            }
        });

        mattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AttendanceuploadviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.rightstart,R.anim.leftend);
                return;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.signout) {
            googleSignInClient.signOut();
            mauth.signOut();
            Intent intent=new Intent(getApplicationContext(), ChooseLoginRegistrationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.leftstart,R.anim.rightend);
            finish();
        }
        return true;
    }



    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0)
                {

                    Map map=(Map) snapshot.getValue();
                    String name=map.get("name").toString();
                    String gen=map.get("gender").toString();
                    String depart=map.get("department").toString();
                    if(gen.equals("default") || depart.equals("default"))
                    {
                        Intent intent=new Intent(getApplicationContext(), GenderActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",name);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }else{
                        personname.setText("Hi,  " + name);
                        progressBar.setVisibility(View.INVISIBLE);
                        progressBar.clearAnimation();
                        mdashboard.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}