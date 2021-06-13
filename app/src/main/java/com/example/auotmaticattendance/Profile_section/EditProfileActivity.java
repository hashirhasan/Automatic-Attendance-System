package com.example.auotmaticattendance.Profile_section;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.auotmaticattendance.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText mName, mPhone,memail;
    Spinner mgender,mdepartment;
    private FirebaseAuth mauth;
    private Button mconfirm;
    private ImageView mprofilepic,camerapic;
    private Uri imageuri;
    private DatabaseReference mdatabasereference;
    private ProgressBar progressBar;
    LinearLayout linearLayout1,linearLayout2;
    private String userid,name,phone,email,gender,department;
    ProgressDialog progressDialog;
//    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");
        mName=findViewById(R.id.name);
        mPhone=findViewById(R.id.phone);
        mgender=findViewById(R.id.gender);
        mdepartment=findViewById(R.id.department);
        memail=findViewById(R.id.email);
        mprofilepic=findViewById(R.id.profilepic);
        camerapic=findViewById(R.id.cameraimg);
        linearLayout1=findViewById(R.id.linearLayout1);
        linearLayout2=findViewById(R.id.linearLayout2);
        mconfirm=findViewById(R.id.confirm);
        progressBar=findViewById(R.id.progressBar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        mauth=FirebaseAuth.getInstance();
        userid=mauth.getCurrentUser().getUid();
        mprofilepic.setVisibility(View.INVISIBLE);
        camerapic.setVisibility(View.INVISIBLE);
        linearLayout1.setVisibility(View.INVISIBLE);
        linearLayout2.setVisibility(View.INVISIBLE);
        mconfirm.setVisibility(View.INVISIBLE);

        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        getuserinfo();

        camerapic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               choosePicture();
            }
        });


        mconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.INVISIBLE);
                name = mName.getText().toString();
                phone = mPhone.getText().toString();
                department = mdepartment.getSelectedItem().toString();
                gender = mgender.getSelectedItem().toString();
                if (!name.equals("") && !phone.equals(""))
                {
                    progressDialog = new ProgressDialog(EditProfileActivity.this);
                progressDialog.setTitle("Updating Profile");
                progressDialog.setMessage("Please wait, while we are setting your data");
                progressDialog.show();
//                mprofilepic.setVisibility(View.INVISIBLE);
//                camerapic.setVisibility(View.INVISIBLE);
//                linearLayout1.setVisibility(View.INVISIBLE);
//                linearLayout2.setVisibility(View.INVISIBLE);
//                mconfirm.setVisibility(View.INVISIBLE);

                saveuserinfo();
                if (imageuri != null) {
                    uploadPicture();
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    overridePendingTransition(R.anim.leftstart, R.anim.rightend);
                    finish();
                }
            }
            }
        });

    }

    private  void choosePicture()
    {
//        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent,1);
        CropImage.activity().setAspectRatio(1,1).start(EditProfileActivity.this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();
                mprofilepic.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadPicture()
    {
        Bitmap bitmap = ((BitmapDrawable) mprofilepic.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final StorageReference filepath= FirebaseStorage.getInstance().getReference().child("profileimages").child(userid);

        filepath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map newpost =new HashMap();
                        newpost.put("profilepic",String.valueOf(uri));
                        mdatabasereference.updateChildren(newpost).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                                overridePendingTransition(R.anim.leftstart, R.anim.rightend);
                                finish();
                            }
                        });
//
                    }
                });
            }
        });

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
                        mName.requestFocus(4);
                    }
                    if(map.get("phone")!=null)
                    {
                        phone=map.get("phone").toString();
                        mPhone.setText(phone);
                    }
                    if(map.get("gender")!=null)
                    {
                        gender=map.get("gender").toString();
                        mgender.setSelection(((ArrayAdapter<String>)mgender.getAdapter()).getPosition(gender));
                    }
                    if(map.get("department")!=null)
                    {
                        department=map.get("department").toString();
                        mdepartment.setSelection(((ArrayAdapter<String>)mdepartment.getAdapter()).getPosition(department));
                    }
                    if(map.get("email")!=null)
                    {
                        email=map.get("email").toString();
                        memail.setText(email);
                    }

                    if(map.get("profilepic")!=null)
                    {
                        String profileimageurl=(String) map.get("profilepic");

                        switch(profileimageurl)
                        {
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.profile).into(mprofilepic);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileimageurl).into(mprofilepic);
                                break;
                        }
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    mprofilepic.setVisibility(View.VISIBLE);
                    camerapic.setVisibility(View.VISIBLE);
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    mconfirm.setVisibility(View.VISIBLE);
                    memail.setEnabled(false);
                    mgender.setEnabled(false);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    private int saveuserinfo()
    {

        Map userinfo=new HashMap();
        userinfo.put("name",name);
        userinfo.put("phone",phone);
        userinfo.put("gender",gender);
        userinfo.put("department",department);

//        Toast.makeText(SignupActivity.this, userinfo.toString(), Toast.LENGTH_SHORT).show();
        mdatabasereference.updateChildren(userinfo);

       return 1;
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