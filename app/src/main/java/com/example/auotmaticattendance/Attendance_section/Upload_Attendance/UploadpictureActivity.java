package com.example.auotmaticattendance.Attendance_section.Upload_Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auotmaticattendance.MainActivity;
import com.example.auotmaticattendance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UploadpictureActivity extends AppCompatActivity {

    TextView mphase1,mphase2,mphase3,mphase4;
    ProgressBar progressBar1,progressBar2,progressBar3,progressBar4;
    Button mupload,mhiddenupload;
    ImageView mimageView;
    private DatabaseReference userdb;
    String branch,subject,profileimageurl,imagedata;
    private  int count=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpicture);
         setTitle("Upload Attendance");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         Bundle bundle=getIntent().getExtras();
         subject=bundle.getString("subject");
         branch=bundle.getString("branch");
        File pictureFile = (File)getIntent().getExtras().get("picture");

        mphase1=findViewById(R.id.phase1);
        mphase2=findViewById(R.id.phase2);
        mphase3=findViewById(R.id.phase3);
        mphase4=findViewById(R.id.phase4);
        progressBar1=findViewById(R.id.progressBar1);
        progressBar2=findViewById(R.id.progressBar2);
        progressBar3=findViewById(R.id.progressBar3);
        progressBar4=findViewById(R.id.progressBar4);
        mupload=findViewById(R.id.upload);
        mhiddenupload=findViewById(R.id.hiddenupload);
       mimageView=findViewById(R.id.imageview);
        mphase1.setVisibility(View.INVISIBLE);
        mphase2.setVisibility(View.INVISIBLE);
        mphase3.setVisibility(View.INVISIBLE);
        mphase4.setVisibility(View.INVISIBLE);
        progressBar1.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);
        progressBar3.setVisibility(View.INVISIBLE);
        progressBar4.setVisibility(View.INVISIBLE);

        if(pictureFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
            Bitmap  rotatedbitmap=rotate(myBitmap);
            mimageView.setImageBitmap(rotatedbitmap);

        }

        mupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Uploading...");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                count=61;
                mupload.setVisibility(View.INVISIBLE);
                mimageView.setVisibility(View.INVISIBLE);
              progressBar1.setVisibility(View.VISIBLE);
              mphase1.setVisibility(View.VISIBLE);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar1.setVisibility(View.INVISIBLE);
                        progressBar2.setVisibility(View.VISIBLE);
                        mphase1.setVisibility(View.INVISIBLE);
                        mphase2.setVisibility(View.VISIBLE);
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar2.setVisibility(View.INVISIBLE);
                                progressBar3.setVisibility(View.VISIBLE);
                                mphase2.setVisibility(View.INVISIBLE);
                                mphase3.setVisibility(View.VISIBLE);

                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar3.setVisibility(View.INVISIBLE);
                                        progressBar4.setVisibility(View.VISIBLE);
                                        mphase3.setVisibility(View.INVISIBLE);
                                        mphase4.setVisibility(View.VISIBLE);
                                        final Handler handler = new Handler(Looper.getMainLooper());
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                userdb= FirebaseDatabase.getInstance().getReference().child("Students").child("1").child("IT-1");
                                                userdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists() && snapshot.getChildrenCount()>0)
                                                        {
                                                            for(DataSnapshot match:snapshot.getChildren())
                                                            {
                                                                saveuserinfo(match.getKey());
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                Intent intent=new Intent(UploadpictureActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                Toast.makeText(UploadpictureActivity.this, "Attendance Uploaded", Toast.LENGTH_LONG).show();
                                               finish();
                                            }
                                        }, 5000);
                                    }
                                }, 5000);
                            }
                        }, 5000);
                    }
                }, 5000);

            }
        });


        mhiddenupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Uploading...");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                count=61;
                mupload.setVisibility(View.INVISIBLE);
                mimageView.setVisibility(View.INVISIBLE);
              progressBar1.setVisibility(View.VISIBLE);
              mphase1.setVisibility(View.VISIBLE);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar1.setVisibility(View.INVISIBLE);
                        progressBar2.setVisibility(View.VISIBLE);
                        mphase1.setVisibility(View.INVISIBLE);
                        mphase2.setVisibility(View.VISIBLE);
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar2.setVisibility(View.INVISIBLE);
                                progressBar3.setVisibility(View.VISIBLE);
                                mphase2.setVisibility(View.INVISIBLE);
                                mphase3.setVisibility(View.VISIBLE);

                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar3.setVisibility(View.INVISIBLE);
                                        progressBar4.setVisibility(View.VISIBLE);
                                        mphase3.setVisibility(View.INVISIBLE);
                                        mphase4.setVisibility(View.VISIBLE);
                                        final Handler handler = new Handler(Looper.getMainLooper());
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent=new Intent(UploadpictureActivity.this,MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                Toast.makeText(UploadpictureActivity.this, "NO right Information uploaded!  Try Uploading Again!!", Toast.LENGTH_LONG).show();
                                               finish();
                                            }
                                        }, 5000);
                                    }
                                }, 5000);
                            }
                        }, 5000);
                    }
                }, 5000);

            }
        });
    }

    private Bitmap rotate(Bitmap decodebitmap) {

        int w=decodebitmap.getWidth();
        int h=decodebitmap.getHeight();

        Matrix matrix=new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodebitmap,0,0,w,h,matrix,true);
    }

    private void saveuserinfo(String key)
    {
        DatabaseReference mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Students").child("1").child("IT-1").child(key).child("subjects").child(subject);
        Map userinfo=new HashMap();
        userinfo.put("Attended","32");
        userinfo.put("Total Classes","45");

        mdatabasereference.updateChildren(userinfo);
    }

    @Override
    public void onBackPressed() {
        count--;
        if(count==0) {
            super.onBackPressed();

            finish();
            overridePendingTransition(R.anim.leftstart, R.anim.rightend);
        }
    }


        @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==android.R.id.home) {
               if(count==0) {
                   super.onBackPressed();
                   finish();
                   overridePendingTransition(R.anim.leftstart, R.anim.rightend);
               }
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(true)
        {
            onBackPressed();
            return true;
        }else{
            return false;
        }
    }
}