package com.tzh.mylibrary.util

import android.R.attr.path
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import androidx.core.content.ContextCompat
import com.tzh.mylibrary.R
import java.io.*


object BitmapUtil {
    /**
     * res资源文件转bitmap
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @JvmStatic
    fun resToBitmap(context: Context, resId: Int): Bitmap {
        val drawable: Drawable = context.resources.getDrawable(resId,null)
        val options : BitmapFactory.Options  =  BitmapFactory.Options()
        BitmapFactory.decodeResource(context.resources,resId,options)
        val bitmap: Bitmap = Bitmap.createBitmap(options.outWidth, options.outHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * 为 Bitmap 设置切割圆角
     *
     * @param bitmap 原图片
     * @param cornerRadius     圆角 px
     * @return 按照参数切割圆角后的 Bitmap
     */
    @JvmStatic
    fun getRoundBitmap(context: Context,bitmap: Bitmap, cornerRadius: Float): Bitmap? {
        var output: Bitmap? = null
        output = try {
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_4444)
        } catch (error: OutOfMemoryError) {
            try {
                Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_4444)
            } catch (e: OutOfMemoryError) {
                return null
            }
        }
        val canvas = output?.let { Canvas(it) }
        val paint = Paint()
        paint.isAntiAlias = true
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        canvas?.drawARGB(0, 0, 0, 0)
        canvas?.drawRoundRect(rectF, DpToUtil.dip2px(context,cornerRadius).toFloat(), DpToUtil.dip2px(context,cornerRadius).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas?.drawBitmap(bitmap, rect, rect, paint)
        return output?.let { getBitmapByBg(context,it, R.color.transparent) }
    }

    /**
     * bitmap转base64
     * */
    @JvmStatic
    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                baos.flush()
                baos.close()
                val bitmapBytes: ByteArray = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    @JvmStatic
    fun base64ToBitmap(base64Data: String?): Bitmap? {
        val bytes: ByteArray = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * 修改bitmap背景颜色
     */
    private fun getBitmapByBg(context: Context,bitmap: Bitmap, color: Int): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(ContextCompat.getColor(context,color))
        val paint = Paint()
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }

    /**
     * 修改bitmap背景颜色为透明
     */
    private fun getBitmapByBg(bitmap: Bitmap): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.TRANSPARENT)
        val paint = Paint()
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }

    /**
     * 偏移效果
     * @param origin 原图
     * @return 偏移后的bitmap
     */
    @JvmStatic
    fun skewBitmap(origin: Bitmap,kx : Float, ky : Float): Bitmap {
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.postSkew(kx, ky)
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        origin.recycle()
        return newBM
    }

    /**
     * 裁剪一定高度保留下面
     * @param srcBitmap
     * @return
     */
    @JvmStatic
    fun cropBitmapTop(srcBitmap: Bitmap,startX: Int,startY: Int,endX: Int,endY: Int): Bitmap{
        /**裁剪保留上部分的第一个像素的Y坐标 */
        val needY = 0

        /**裁剪关键步骤 */
        val cropBitmap = Bitmap.createBitmap(srcBitmap, startX, startY, endX, endY)
        /**回收之前的Bitmap*/
        if (srcBitmap != cropBitmap && !srcBitmap.isRecycled) {
//            GlideBitmapPool.putBitmap(srcBitmap);
        }
        return cropBitmap
    }

    /**
     * 保存bitmap到本地
     */
    @JvmStatic
    fun saveImageToGallery(context: Context, bmp: Bitmap) {
        // 首先保存图片
        val appDir = getVoiceCacheFolder(context)
        if (!appDir?.exists().toDefault(false)) {
            appDir?.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".png"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.contentResolver,
                    file.absolutePath, fileName, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        // 最后通知图库更新
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$path")))
    }

    /**
     * 将View保存为bitmap
     */
    @JvmStatic
    fun getCacheBitmapFromView(view: View): Bitmap? {
        val drawingCacheEnabled = true
        view.isDrawingCacheEnabled = drawingCacheEnabled
        //设置背景色  //view.setBackgroundColor(CommonUtils.getContext().getResources().getColor(R.color.half_white));
        view.buildDrawingCache(drawingCacheEnabled)
        val drawingCache = view.drawingCache
        val bitmap: Bitmap?
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache)
            view.isDrawingCacheEnabled = false
        } else {
            bitmap = null
        }
        return bitmap
    }

    /**
     * 获取 图片文件下载目录
     *
     */
    fun getVoiceCacheFolder(context: Context): File? {
        val file = context.applicationContext.getExternalFilesDir("mImage/")
        AppPathManager.ifFolderExit(file?.absolutePath)
        return file
    }
}