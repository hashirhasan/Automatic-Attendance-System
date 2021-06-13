package com.example.auotmaticattendance.Profile_section;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.auotmaticattendance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {

    private TextView mName,mName1, mPhone,memail,mgender,mdepartment,meditprofile,mTeacher;
    private FirebaseAuth mauth;
    private Button mconfirm;
    private ImageView mprofilepic;
    private DatabaseReference mdatabasereference;
    private ProgressBar progressBar1;
    private Animator currentAnimator;
    private int shortAnimationDuration;
    LinearLayout linearLayout1,linearLayout2;
    private ConstraintLayout constraintLayout;
    private String userid,name,phone,email,department,gender,profileimageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setTitle("My Profile");
        constraintLayout=findViewById(R.id.container);
        mTeacher=findViewById(R.id.teacher);
        mName1=findViewById(R.id.name1);
        mName=findViewById(R.id.name);
        mPhone=findViewById(R.id.phone);
        mgender=findViewById(R.id.gender);
        memail=findViewById(R.id.email);
        mdepartment=findViewById(R.id.department);
        mprofilepic=findViewById(R.id.profilepic);
        meditprofile=findViewById(R.id.edit_pro);
        linearLayout1=findViewById(R.id.linearLayout1);
        linearLayout2=findViewById(R.id.linearLayout2);
//        mback=findViewById(R.id.back);
//        mconfirm=findViewById(R.id.confirm);
//        progressBar =findViewById(R.id.progressBar);
        progressBar1=findViewById(R.id.progressBar1);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mauth=FirebaseAuth.getInstance();
        userid=mauth.getCurrentUser().getUid();

//        progressBar.setVisibility(View.INVISIBLE);
        mTeacher.setVisibility(View.INVISIBLE);
        meditprofile.setVisibility(View.INVISIBLE);
        mprofilepic.setVisibility(View.INVISIBLE);
        linearLayout1.setVisibility(View.INVISIBLE);
        linearLayout2.setVisibility(View.INVISIBLE);

//        mconfirm.setVisibility(View.INVISIBLE);
        mName1.setVisibility(View.INVISIBLE);
        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        getuserinfo();

        meditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(), EditProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightstart,R.anim.leftend );
                return;
            }
        });

        mprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(mprofilepic,mprofilepic.getDrawable());
                setTitle("My Profile Photo");
                constraintLayout.setBackgroundColor(Color.BLACK);
                linearLayout1.setBackgroundColor(0x00000000);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

    }
    private void getuserinfo()
    {
        mdatabasereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0)
                {

                    Map map=(Map) snapshot.getValue();

                    if(map.get("name")!=null)
                    {
                        name=map.get("name").toString();
                        mName.setText(name);
                        mName1.setText(name);
                    }
                    if(map.get("phone")!=null)
                    {
                        phone=map.get("phone").toString();
                        mPhone.setText(phone);
                    }
                    if(map.get("gender")!=null)
                    {
                        gender=map.get("gender").toString();
                        mgender.setText(gender);
                    }
                    if(map.get("department")!=null)
                    {
                        department=map.get("department").toString();
                        mdepartment.setText(department);
                    }
                    if(map.get("email")!=null)
                    {
                        email=map.get("email").toString();
                        memail.setText(email);
                    }
                    if(map.get("profilepic")!=null)
                    {
                        profileimageurl=(String) map.get("profilepic");

                        switch(profileimageurl)
                        {
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.profile).placeholder(R.mipmap.profile).into(mprofilepic);

                                break;
                            default:
                                Glide.with(getApplication()).load(profileimageurl).into(mprofilepic);

                                break;
                        }
                    }
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mprofilepic.getDrawable()!=null) {
                            progressBar1.setVisibility(View.INVISIBLE);
                            mTeacher.setVisibility(View.VISIBLE);
                            meditprofile.setVisibility(View.VISIBLE);
                            mprofilepic.setVisibility(View.VISIBLE);
//                    mconfirm.setVisibility(View.VISIBLE);
                            mName1.setVisibility(View.VISIBLE);
                            linearLayout1.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.VISIBLE);
                        }
                        handler.postDelayed(this, 1000);
                    }
                }, 1000);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void zoomImageFromThumb(final View thumbView, Drawable imageResdrawable) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.preview);
        expandedImageView.setImageDrawable(imageResdrawable);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(400);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(300);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        constraintLayout.setBackgroundColor(0x00000000);
                        linearLayout1.setBackgroundColor(Color.parseColor("#00cfc8"));
                        setTitle("My Profile");
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        constraintLayout.setBackgroundColor(0x00000000);
                        linearLayout1.setBackgroundColor(Color.parseColor("#00cfc8"));
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        setTitle("My Profile");
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }


}