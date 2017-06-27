package com.idear.move.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片压缩相关操作的工具类
 */
public class PictureUtil {

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth 申请压缩的宽
	 * @param reqHeight 申请压缩的高
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// 计算出缩放比例为 宽高比例中的较小值
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 根据路径获得图片并压缩返回bitmap用于显示
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
        //计算缩放比例
		options.inSampleSize = calculateInSampleSize(options, 320, 480);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

    /**
     * 压缩图片自定义压缩比
     * @param srcPath
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    public static Bitmap compressSizeImage(String srcPath,int reqHeight,int reqWidth) {
        Bitmap bitmap = null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height= newOpts.outHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int scale = 1;//scale=1表示不缩放

        if (height > reqHeight || width > reqWidth) {
            //计算图片高度和我们需要高度的最接近比例值
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            //宽度比例值
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //取比例值中的较大值作为inSampleSize
            scale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        newOpts.inSampleSize = scale;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

	/**
	 * 压缩大图片
	 * @param srcPath
	 * @return
	 */
	public static Bitmap compressSizeImage(String srcPath) {
        Bitmap bitmap = null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
          
        newOpts.inJustDecodeBounds = false;  
        int width = newOpts.outWidth;
        int height= newOpts.outHeight;
        //分辨率，所以高和宽我们设置为
        float reqHeight = 800f;
        float reqWidth = 480f;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int scale = 1;//scale=1表示不缩放

        if (height > reqHeight || width > reqWidth) {
            //计算图片高度和我们需要高度的最接近比例值
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            //宽度比例值
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //取比例值中的较小值作为inSampleSize
            scale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        newOpts.inSampleSize = scale;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    }

    /**
     * 按比例进行压缩
     * @param image
     * @return
     */
    public static Bitmap compressWithScale(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {
            //判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 按比例进行压缩
     * @param srcPath
     * @return
     */
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
	 * 图片质量压缩
     */
	public static Bitmap compressImage(Bitmap bitmap) {
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int beginRate = 100;
		//第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
		bitmap.compress(Bitmap.CompressFormat.JPEG, beginRate, baos);
        while ( beginRate>=10 && baos.toByteArray().length / 1024  > 200) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩
			beginRate = beginRate - 10;//每次都减少10
			baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, beginRate, baos);//这里压缩beginRate%，把压缩后的数据存放到baos中
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap newBitmap = BitmapFactory.decodeStream(isBm);
        if (newBitmap!=null) {
            return newBitmap;
        } else {
            return bitmap;
        }
    }  
	
	 /** 
	 * 获取图片文件的信息，是否旋转了90度，如果是则反转 
	 * @param bitmap 需要旋转的图片 
	 * @param path   图片的路径 
	 */  
	public static Bitmap reviewPicRotate(Bitmap bitmap,String path){
	    int degree = getPicRotate(path);
	    if(degree!=0){  
	        Matrix m = new Matrix();    
	        int width = bitmap.getWidth();    
	        int height = bitmap.getHeight();    
	        m.setRotate(degree); // 旋转angle度    
	        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,m, true);// 从新生成图片    
	    }  
	    return bitmap;  
	}  
	  
	/** 
	 * 读取图片文件旋转的角度 
	 * @param path 图片绝对路径 
	 * @return 图片旋转的角度 
	 */  
	public static int getPicRotate(String path) {
	    int degree  = 0;  
	    try {  
	        ExifInterface exifInterface = new ExifInterface(path);
	        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
	        switch (orientation) {  
	        case ExifInterface.ORIENTATION_ROTATE_90:  
	            degree = 90;  
	            break;  
	        case ExifInterface.ORIENTATION_ROTATE_180:  
	            degree = 180;  
	            break;  
	        case ExifInterface.ORIENTATION_ROTATE_270:  
	            degree = 270;  
	            break;  
	        }  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    return degree;  
	}  
}
