package com.idear.move.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * 作者:geminiyang on 2017/6/14.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class CameraUtil {
    /**
     * 获取图片存储路径,一个Activity 里面一次业务流程只能调用一次
     * @return
     */
    public static String[] getSavePicPath() {
        final String dir = FileSaveUtil.SD_CARD_PATH + "image_data/";
        try {
            FileSaveUtil.createSDCardDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName = defineFileName(".jpg");
        return new String[]{dir,fileName};
    }

    /**
     * 创建相机拍照图片名称
     *
     * @param fileType ,文件的类型，即扩展名，例如.jpg 、.mp4 、.mp3等
     * @return 图片文件名,格式形式20161011_111523.jpg
     */
     private static String defineFileName(String fileType) {
        return DateUtil.picDateFormat(System.currentTimeMillis())+ fileType;
    }

    /**
     * 更新系统的Media库
     *
     * @param context
     */
    public static void updateSystemMedia(Context context) {
        MediaScannerConnection.scanFile(context, new String[]{
                android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
        }, null, null);
    }

    /**
     * 打开手机最近相册
     *
     * @return intent, Activity调用的intent
     */
    public static Intent openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        return intent;
    }

    /**
     * 打开手机画廊
     *
     * @return intent, Activity调用的intent
     */
    public static Intent openGallery() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * 打开手机最近相册
     *
     * @return intent, Activity调用的intent
     */
    public static Intent openRecentPhotoList() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra("crop", "true");
            intent.putExtra("scale", "true");
            intent.putExtra("scaleUpIfNeeded", true);
        }
        intent.setType("image/*");
        return  intent;
    }

    /**
     * 打开手机系统相机拍照
     *
     * @param uri , 用于保存手机拍照后所获图片的uri
     * @return intent, Activity调用的intent
     */
    public static Intent openCamera(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //intent.putExtra("autofocus", true);//进行自动对焦操作
        return intent;
    }

    /**
     * 必须接受外面传递的接收外部文件名
     * @return
     */
    public static Intent openMyCamera(String filepath) {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(filepath));
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//所拍的照保存在指定路径
        return openCameraIntent;
    }

    /**
     * 打开手机系统的图片裁剪Activity
     *
     * @param inUri  , 待裁剪图片的uri
     * @param outUri , 裁剪后图片保存的uri
     * @return intent , Activity调用的intent
     */
    public static Intent cropPicture(Uri inUri, Uri outUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inUri, "image/*");     //设置图片资源路径
        intent.putExtra("scale", true);
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        return intent;
    }
}
