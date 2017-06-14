//package com.idear.move.Activity;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.idear.move.util.CameraUtil;
//
//import java.io.File;
//
///**
// * 作者:geminiyang on 2017/6/14.
// * 邮箱:15118871363@163.com
// * github地址：https://github.com/geminiyang/ShareTransilation
// */
//
//public class CameraActivity extends AppCompatActivity {
//
//    private final String TAG = getClass().getSimpleName();
//    private final int ACT_GALLERY = 0;
//    private final int ACT_CAMERA = 1;
//    private final int ACT_CROP = 2;
//    private final int ACT_PERMISSION = 3;
//    private Context context = null;
//    private Uri pictureUri = null;
//    private String filePath = android.os.Environment.getExternalStorageDirectory() + File.separator + "DICM" + File.separator;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = this;
//        setContentView(R.layout.activity_camera);
//        CameraUtil.createDir(filePath);
//        initView();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        switch (requestCode) {
//            case ACT_GALLERY:
//                galleryBack(data.getData());
//                break;
//            case ACT_CAMERA:
//                startCrop(pictureUri);
//                break;
//            case ACT_CROP:
//                cropBack(pictureUri);
//                break;
//            default:
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permission, grantResults);
//        switch (requestCode) {
//            case ACT_PERMISSION:
//                if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
//                    Log.d(TAG, "permission allowed");
//                    Intent intent = CameraUtil.openCamera(pictureUri);
//                    startActivityForResult(intent, ACT_CAMERA);
//                } else {
//                    Log.d(TAG, "permission denied");
//                }
//                break;
//        }
//    }
//
//    private void initView() {
//        Button openGallery = (Button) findViewById(R.id.btn_gallery);
//        Button openCamera = (Button) findViewById(R.id.btn_camera);
//
//        if (openGallery != null) {
//            openGallery.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startGallery();
//                }
//            });
//        }
//
//        if (openCamera != null) {
//            openCamera.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startCamera();
//                }
//            });
//        }
//
//    }
//
//    private void startGallery() {
//        Intent intent = CameraUtil.openGallery();
//        startActivityForResult(intent, ACT_GALLERY);
//    }
//
//    private void startCamera() {
//        String fileName = CameraUtil.createFileName(".jpg");
//        CameraUtil.createFile(filePath, fileName);
//        File file = new File(filePath, fileName);
//        pictureUri = Uri.fromFile(file);
//
//        //request the camera permission dynamic above android 6.0
//        boolean permission = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
//        if (permission) {
//            Intent intent = CameraUtil.openCamera(pictureUri);
//            startActivityForResult(intent, ACT_CAMERA);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, ACT_PERMISSION);
//        }
//
//    }
//
//    private void startCrop(Uri inUri) {
//        Intent intent = CameraUtil.cropPicture(inUri, pictureUri);
//        startActivityForResult(intent, ACT_CROP);
//    }
//
//    private void galleryBack(Uri inUri) {
//        //get picture uri from gallery
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(inUri, projection, null, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                startCrop(Uri.fromFile(new File(filePath)));
//            }
//            if (!cursor.isClosed()) {
//                cursor.close();
//            }
//        }
//    }
//
//    private void cropBack(Uri inUri) {
//        if (inUri == null) {
//            return;
//        }
//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(inUri));
//            //to display or save the picture
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
