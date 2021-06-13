package com.example.auotmaticattendance.Attendance_section.Upload_Attendance;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.auotmaticattendance.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UploadAttendanceActivity extends AppCompatActivity {

    private Button takephoto;
    private File file;
    private TextureView textureView;
    private byte[] imagedata;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    private String cameraid;
    CameraDevice cameraDevice;
    CameraCaptureSession cameraCaptureSession;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;

    private Size imageDimensions;
    private ImageReader imageReader;
    Handler mBackgroundHandler;
    HandlerThread mBackgroundThread;
    String branch,subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_attendance);
       Bundle bundle=getIntent().getExtras();
       branch=bundle.getString("branch");
       subject=bundle.getString("subject");


        textureView=findViewById(R.id.photoview);
        takephoto=findViewById(R.id.takephoto);

        assert  textureView!=null;
        textureView.setSurfaceTextureListener(TextureListner);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    takepicture();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101)
        {
            if(grantResults[0]==PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(this, "Sorry, Camera Permission is necessary!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    TextureView.SurfaceTextureListener TextureListner=new TextureView.SurfaceTextureListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            try {
                openCamera();
            } catch (@SuppressLint("NewApi") CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private final StateCallback stateCallback=new StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
              cameraDevice=camera;
            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
              cameraDevice.close();
        }

        @Override
        public void onError( CameraDevice camera, int error) {
              cameraDevice.close();
              cameraDevice=null;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture=textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimensions.getWidth(),imageDimensions.getHeight());
        Surface surface=new Surface(texture);
        captureRequestBuilder=cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);

        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if(cameraDevice==null)
                {
                    return;
                }
                cameraCaptureSession=session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {

                Toast.makeText(UploadAttendanceActivity.this, "Configuration Changed", Toast.LENGTH_SHORT).show();
            }
        },null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updatePreview() throws CameraAccessException {

        if(cameraDevice==null)
        {
            return;
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),null,mBackgroundHandler);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() throws CameraAccessException {
        CameraManager manager=(CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraid=manager.getCameraIdList()[0];
        CameraCharacteristics cameraCharacteristics=manager.getCameraCharacteristics(cameraid);
        StreamConfigurationMap map=cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        imageDimensions=map.getOutputSizes(SurfaceTexture.class)[0];
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(UploadAttendanceActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
            return;
        }
        manager.openCamera(cameraid,stateCallback,null);
    }

    private void takepicture() throws CameraAccessException {
            if(cameraDevice==null)
            {
                return;
            }
            CameraManager manager=(CameraManager) getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics=manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegsizes=null;
            jpegsizes=characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);

            int width=640;
            int height=480;
            if(jpegsizes!=null && jpegsizes.length>0)
            {
                width=jpegsizes[0].getWidth();
                height=jpegsizes[0].getHeight();
            }

            ImageReader reader=ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
            List<Surface> outputSurfaces=new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            final  CaptureRequest.Builder captureBuilder=cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE,CameraMetadata.CONTROL_MODE_AUTO);

            int rotation=getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation));

            Long tslong=System.currentTimeMillis()/1000;
            String ts=tslong.toString();

            file=new File(Environment.getExternalStorageDirectory()+"/"+ts+".jpg");

            ImageReader.OnImageAvailableListener readerListner=new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image=null;
                    image=reader.acquireLatestImage();
                    ByteBuffer buffer=image.getPlanes()[0].getBuffer();
                    byte[] data=new byte[buffer.capacity()];
                   buffer.get(data);
                   Log.d("tag",data.toString());
                    try {
                        save(data);
                        Toast.makeText(UploadAttendanceActivity.this, "let us captured", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(UploadAttendanceActivity.this, "let us not ", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }finally {
                        if(image!=null)
                        {
                           image.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListner,mBackgroundHandler);
          final CameraCaptureSession.CaptureCallback captureCallbackListner=new CameraCaptureSession.CaptureCallback() {
              @Override
              public void onCaptureCompleted(@NonNull CameraCaptureSession session, CaptureRequest request, @NonNull TotalCaptureResult result) {
                  super.onCaptureCompleted(session, request, result);
//                  Toast.makeText(UploadAttendanceActivity.this, "captured", Toast.LENGTH_SHORT).show();
                  try {
                      createCameraPreview();
                  } catch (CameraAccessException e) {
                      e.printStackTrace();
                  }
              }
          };

          cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
              @Override
              public void onConfigured(CameraCaptureSession session) {
                  try {
                      session.capture(captureBuilder.build(),captureCallbackListner,mBackgroundHandler);
                  } catch (CameraAccessException e) {
                      e.printStackTrace();
                  }
              }

              @Override
              public void onConfigureFailed(CameraCaptureSession session) {

              }
          },mBackgroundHandler);
    }

    private void save(byte[] data) throws IOException {
        OutputStream outputStream=null;

        outputStream=new FileOutputStream(file);
        outputStream.write(data);
        outputStream.close();
        Intent intent=new Intent(UploadAttendanceActivity.this,UploadpictureActivity.class);
        intent.putExtra("picture",file);
        Bundle bundle=new Bundle();
        bundle.putString("branch",branch);
        bundle.putString("subject",subject);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.rightstart,R.anim.leftend);
        finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();

        startBackgroundThread();
        if(textureView.isAvailable())
        {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }else{
            textureView.setSurfaceTextureListener(TextureListner);
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread=new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler=new Handler(mBackgroundThread.getLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void stopBackgroundThread() throws InterruptedException {
        mBackgroundThread.quitSafely();
        mBackgroundThread.join();
        mBackgroundThread=null;
        mBackgroundHandler=null;
    }




}