package com.ybxiang.driver.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    public static String textFormatGreen(String value) {
        return "<font color=#409629>" + value + "</font>";
    }

    public static String textFormatBlue(String value) {
        return "<font color=#0000FF>" + value + "</font>";
    }

    public static String textFormatRed(String value) {
        value = value.replace("{", "<font color=#FE5722>");
        value = value.replace("}", "</font>");
        return value;
    }

    public static String textFormatBold(String value) {
        return "<b>" + value + "</b>";
    }

    public static class MyImageLoadingListener implements ImageLoadingListener {

        private Context context;
        private ImageView imageView;

        public MyImageLoadingListener(Context context, ImageView imageView) {
            this.context = context;
            this.imageView = imageView;
        }

        @Override
        public void onLoadingStarted() {

        }

        @Override
        public void onLoadingFailed(FailReason failReason) {
            imageView.setImageResource(R.drawable.ic_img_loading);
        }

        @Override
        public void onLoadingComplete(Bitmap loadedImage) {

        }

        @Override
        public void onLoadingCancelled() {

        }
    };

    /**
     * 保存图片
     * @param url
     * @param mBitmap
     */
    public static void saveBitmap(String url, Bitmap mBitmap) {
        FileOutputStream fOut = null;
        File f = new File(url);
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 缩小图片
     *
     * @param bm
     * @return
     */
    public static Bitmap getScaleBitmapSmall(Bitmap bm) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth = 150;
        int newHeight = 150;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
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
            return degree;
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }
}
