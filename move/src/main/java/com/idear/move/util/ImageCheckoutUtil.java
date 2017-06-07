package com.idear.move.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageCheckoutUtil {
    /**
     * 检测bitmap图片内存大小(单位字节)
     * @param data
     * @return
     */
    @SuppressLint("NewApi")
    public static int getImageSize(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    /**
     * 获取本地路径中的图片变为bitmap
     * @param url
     * @return
     */
    public static Bitmap getLocalBitmap(String url) {
        try {
            ByteArrayOutputStream out;
            FileInputStream fis = new FileInputStream(url);
            BufferedInputStream bis = new BufferedInputStream(fis);
            out = new ByteArrayOutputStream();
            @SuppressWarnings("unused")
            int hasRead = 0;
            byte[] buffer = new byte[1024 * 2];
            while ((hasRead = bis.read(buffer)) > 0) {
                // 读出多少数据，向输出流中写入多少
                out.write(buffer);
                out.flush();
            }
            out.close();
            fis.close();
            bis.close();
            byte[] data = out.toByteArray();

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 2;
            return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
