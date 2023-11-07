package com.tzh.myapplication.utils.img

import android.content.Context
import com.tzh.mylibrary.util.AppPathManager
import java.io.File

object KtFileUtil {

    /**
     * 获取 图片文件下载目录
     *
     */
    fun getImageCacheFolder(context : Context): File? {
        val file = context.getExternalFilesDir("image/")
        AppPathManager.ifFolderExit(file?.absolutePath)
        return file
    }
}