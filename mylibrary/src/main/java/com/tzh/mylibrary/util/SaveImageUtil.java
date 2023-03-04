package com.tzh.mylibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 保存图片的工具类
 */
public class SaveImageUtil {
    /**
     * 保存bitmap
     * @param context 上下文
     * @param src 需要保存的bitmap
     * @param isWatermark 是否有水印
     */
    public static void saveBitmap(Context context,Bitmap src,boolean isWatermark,Bitmap watermark){
        if(isWatermark){
            Bitmap bitmap = createBitmap(src,watermark);
            BitmapUtil.saveImageToGallery(context,bitmap);
        }else{
            BitmapUtil.saveImageToGallery(context,src);
        }
    }

    /**
     * bitmap添加水印
     * @param src 原来的bitmap
     * @param watermark 水印
     */
    public static Bitmap createBitmap(Bitmap src,Bitmap watermark){
        return createWaterMaskBitmap(src,watermark);
    }

    /**
     * 将bitmap与水印合并
     * @param src 图片
     * @param watermark 水印
     */
    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newBitmap);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, 0, 0, null);
        // 保存
        canvas.save();
        // 存储
        canvas.restore();
        return newBitmap;
    }

    /**
     * 将bitmap转换为本地的图片
     * @param bitmap
     * @return
     */
    public static String bitmap2Path(Bitmap bitmap, String path) {
        try {
            OutputStream os = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {

        }
        return path;
    }
}
