
package cn.core.mobile.library.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.net.Uri;

/**
 * Bitmap处理类
 * 
 * @author Will.Wu </br> Create at 2014年2月19日 下午5:11:08
 * @version 1.0
 */
public class BitmapUtils {

    /**
     * Bitmap转byte数组
     * 
     * @param bitmap 要转换的位图
     * @return 位图的byte数组
     * @author Will.Wu
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap转byte数组
     * 
     * @param bitmap 要转换的位图
     * @param quality 转换质量
     * @return 位图的byte数组
     * @author Will.Wu
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        return baos.toByteArray();
    }

    /**
     * 根据指定的byte数组解码一个不可变的位图。
     * 
     * @param bytes
     * @param opts
     * @return 位图，如果解码失败返回null
     * @author Will.Wu
     */
    public static Bitmap decodeByteArray(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    /**
     * 图片透明度处理
     * 
     * @param bitmap 原始图片
     * @param number 透明度
     * @return 处理后的位图
     */
    public static Bitmap setAlpha(Bitmap bitmap, int number) {
        if (null == bitmap)
            throw new RuntimeException("setAlpha:the source of bitmap is null");
        int[] argb = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(argb, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // 获得图片的ARGB值
        number = number * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            // 修改最高2位的值
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        return bitmap;
    }

    /**
     * 图片缩放(非等比例)
     * 
     * @param bitmap :资源图片
     * @param mWidth ：新图片的宽
     * @param mHeight ：新图片的高
     * @return Bitmap
     */
    public static Bitmap resizeImage(Bitmap bitmap, float mWidth, float mHeight) {
        if (null == bitmap)
            throw new RuntimeException("zoomImage:the source of bm is null");
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = (mWidth) / width;
        float scaleHeight = (mHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建新的图片
        try {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return bitmap;
        } catch (OutOfMemoryError e) {
        }
        return null;
    }

    /**
     * 图片缩放(等比例)（2个边长都不高于maxSize）
     * 
     * @param bitmap
     * @param newWidth
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float newWidth = maxSize;
        float newHeight = maxSize;
        if (width >= height) {
            float temp = ((float) height) / ((float) width);
            newWidth = maxSize;
            newHeight = (int) ((maxSize) * temp);
        } else {
            float temp = ((float) width) / ((float) height);
            newWidth = (int) ((maxSize) * temp);
            newHeight = maxSize;
        }
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    /**
     * 等比例缩放（长和宽都不小于指定的长和宽）
     * 
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = (float) width / (float) w >= (float) height / (float) h ? (float) width
                / (float) w : (float) height / (float) h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap cuteBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
        // Bitmap cuteBitmap = Bitmap.createBitmap(bitmap, (int)((w * scale -
        // width) / 2), (int)((h * scale - height) / 2), width, height, matrix,
        // true);
        bitmap.recycle();
        return cuteBitmap;
    }

    /**
     * 图片切割（先压缩大小 然后切割适合尺寸部分）
     * 
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap cuteBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        Bitmap resizeBitmap = resizeBitmap(bitmap, width, height);
        Bitmap cuteBitmap = Bitmap.createBitmap(resizeBitmap,
                (resizeBitmap.getWidth() - width) / 2, (resizeBitmap.getHeight() - height) / 2,
                width, height);
        resizeBitmap.recycle();
        return cuteBitmap;
    }

    /**
     * Bitmap转化为drawable
     * 
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    /**
     * Drawable 转 bitmap
     * 
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 旋转位图
     * 
     * @param bitmap
     * @param orientation
     * @return
     * @author Will.Wu
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180.0f, w / 2, h / 2);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270.0f, w / 2, h / 2);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90.0f, w / 2, h / 2);
                break;
            default:
                return bitmap;
        }
        Bitmap cuteBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
        bitmap.recycle();
        return cuteBitmap;
    }

    public static int[] getBitmapRealSize(int[] size, int orientation) {
        if (size == null || size.length != 2) {
            size = new int[2];
            return size;
        }
        int w = size[0];
        int h = size[1];
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
            case ExifInterface.ORIENTATION_ROTATE_90:
                size[0] = h;
                size[1] = w;
                break;
        }
        return size;
    }

    public static Bitmap getBitmapForUri(Context context, Uri uri, int width, int height) {
        try {
            String path = null;
            Cursor cursor = context.getContentResolver().query(uri, new String[] { "_data" }, null,
                    null, null);
            if (cursor != null && cursor.moveToFirst()) {
                path = cursor.getString(0);
            } else {
                path = uri.getPath();
            }
            if (path == null) {
                return null;
            }
            ExifInterface exif = new ExifInterface(path);
            int orientation = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            int[] size = getBitmapRealSize(new int[] { width, height }, orientation);
            int beX = (int) (options.outWidth / (float) size[0]);
            int beY = (int) (options.outHeight / (float) size[1]);
            int be = (beX > beY ? beY : beX);
            if (be <= 0)
                be = 1;
            options.inSampleSize = be;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, options);
            bitmap = rotateBitmap(bitmap, orientation);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapForUri(Context context, Uri uri) {
        try {
            String path = null;
            Cursor cursor = context.getContentResolver().query(uri, new String[] { "_data" }, null,
                    null, null);
            if (cursor != null && cursor.moveToFirst()) {
                path = cursor.getString(0);
            } else {
                path = uri.getPath();
            }
            if (path == null) {
                return null;
            }
            ExifInterface exif = new ExifInterface(path);
            int orientation = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            int[] size = getBitmapRealSize(new int[] { 960, 1560 }, orientation);
            // int beX = (int)(options.outWidth / (float)size[0]);
            int beY = (int) (options.outHeight / (float) size[1]);
            // int be = (beX > beY ? beY : beX);
            int be = beY;
            if (be <= 0)
                be = 1;
            options.inSampleSize = be;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, options);
            bitmap = rotateBitmap(bitmap, orientation);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
