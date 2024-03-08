package com.tzh.mylibrary.util.general

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.tzh.mylibrary.base.XAppActivityManager
import com.tzh.mylibrary.util.OnPermissionCallBackListener
import com.tzh.mylibrary.util.PermissionXUtil
import com.tzh.mylibrary.dialog.HintDialog

object PermissionDetectionUtil {

    fun getPermission(activity: AppCompatActivity,listener : DetectionListener){
        PermissionXUtil.requestAnyPermission(activity, mutableListOf<String>().apply {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                add(Manifest.permission.READ_MEDIA_IMAGES)
                add(Manifest.permission.READ_MEDIA_VIDEO)
            }else{
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            add(Manifest.permission.CAMERA)
        },object : OnPermissionCallBackListener {
            override fun onAgree() {
                listener.ok()
            }

            override fun onDisAgree() {

            }
        })
    }

    interface DetectionListener{
        fun ok()
    }
}