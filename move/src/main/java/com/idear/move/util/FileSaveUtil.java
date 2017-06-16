package com.idear.move.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件存储相关工具类
 */
public class FileSaveUtil {
    public static final String SD_CARD_PATH =
            Environment.getExternalStorageDirectory().toString() + "/MOVE/";

    public static final String voice_dir = SD_CARD_PATH + "/voice_data/";

    /**
     * 检测SD卡是否可用
     *
     * @return true 可用，false不可用。
     */
    public static boolean isSDExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取文件夹下的所有文件名
     */
    public static List<String> getFileName(String fileName) {
        List<String> fileList = new ArrayList<String>();
        String path = fileName; // 路径
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return null;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (!fs.isDirectory()) {
                fileList.add(fs.getName());
            }
        }
        return fileList;
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File createSDFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            if (file.isDirectory()) {
                boolean mkdirs = file.mkdirs();
            } else {
                boolean newFile = file.createNewFile();
            }
        }
        return file;
    }

    /**
     * 在SD卡上创建文件夹
     *
     * @throws IOException
     */
    public static File createSDCardDirectory(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }
        return file;
    }


    /**
     * 往文本文件中写字节流
     * @param filePath
     * @param data
     * @param isAppend
     * @return
     */
    public synchronized static boolean writeBytes(String filePath, byte[] data, boolean isAppend) {
        try {
            FileOutputStream fos;
            if (isAppend)
                fos = new FileOutputStream(filePath, true);
            else
                fos = new FileOutputStream(filePath);
            fos.write(data);
            fos.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public synchronized static String readSDFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        File f1 = new File(fileName);
        String str = null;
        try {
            InputStream is = new FileInputStream(f1);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            @SuppressWarnings("resource")
            BufferedReader reader = new BufferedReader(input);
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 新建文件夹到手机本地
     * @param fileFolder ,文件夹的路径名称
     * @return
     */
    public static boolean createDir(String fileFolder) {
        File dir = new File(fileFolder);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }

    /**
     * 新建文件到手机本地
     *
     * @param fileNameWithPath ,文件名包含路径
     * @return , true新建成功, false新建失败
     */
    public static boolean createFile(String fileNameWithPath) {
        File file = new File(fileNameWithPath);
        try {
            if (isSDExist() && file.exists()) {
                if (file.delete()) {
                    return file.createNewFile();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 新建文件到手机指定路径
     * @param dirPath  ,文件的文件夹目录路径
     * @param fileName ,文件名
     * @return , true新建成功, false新建失败
     */
    public static boolean createFile(String dirPath, String fileName) {
        File file = new File(dirPath, fileName);
        try {
            if (isSDExist() && file.exists()) {
                if (file.delete()) {
                    return file.createNewFile();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        // 如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                // 删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前空目录
        return dirFile.delete();
    }

    /**
     * 保存图片的Bitmap数据到sd卡指定路径
     *
     * @param fileNameWithPath ,图片的路径
     * @param bitmap           ,图片的bitmap数据
     */
    public static void savePhotoToPath(String fileNameWithPath, Bitmap bitmap) {
        if (isSDExist()) {
            File file = new File(fileNameWithPath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                if (bitmap != null) {
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                        fos.flush();
                        fos.close();
                    }
                }
            } catch (FileNotFoundException e) {
                file.delete();
                e.printStackTrace();
            } catch (IOException e) {
                file.delete();
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean saveBitmap(Bitmap bm, String path,String filename) {
        try {
            File f = getFilePath(path,filename);
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 先创建文件路径，然后再创建文件名路径,open failed: ENOENT (No such file or directory).
     * @param filePath
     * @param fileName
     * @return
     */
    public static File getFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 把文件转换成base64
     *
     * @param path
     * @return
     */
    public static String encodeBase64File(String path) throws Exception {
        byte[] videoBytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        @SuppressWarnings("resource")
        FileInputStream fis = new FileInputStream(new File(path));
        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = fis.read(buf)))
            baos.write(buf, 0, n);
        videoBytes = baos.toByteArray();
        return Base64.encodeToString(videoBytes, Base64.NO_WRAP);
    }

    /**
     * 根据相册媒体库路径转换成sd卡路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isOverKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isOverKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @SuppressLint("NewApi")
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }
}