package com.tzh.myapplication.utils.xls

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File


object ShareImgUtil {
    /**
     * 分享图片
     */
    fun shareImg(context: Context, file : File){
        // 创建分享意图
        // 分享Excel文件到微信
        val intent = Intent()
        intent.setAction(Intent.ACTION_SEND)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setType("*/*")
        val uri = FileProvider.getUriForFile(context, context.packageName+".fileprovider", file)
        intent.putExtra(Intent.EXTRA_STREAM, uri) // 设置文件路径为Uri
        context.startActivity(Intent.createChooser(intent, "分享")) // 启动分享意图
    }
}