package com.tzh.myapplication.utils.img

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.tzh.myapplication.utils.OnPermissionCallBackListener
import com.tzh.myapplication.utils.PermissionXUtil
import com.tzh.mylibrary.base.XAppActivityManager
import com.tzh.mylibrary.dialog.HintDialog
import com.tzh.mylibrary.util.checkPhonePermission
import com.tzh.mylibrary.util.img.ChoiceImageUtil

object PermissionDetectionUtil {

    fun detection(context : Context,listener : DetectionListener){
        if(ChoiceImageUtil.getPhotoPermissions().checkPhonePermission(context)){
            //有这个权限
            //人工传话
            getPermission(listener)
        } else {
            //没有这个权限
            HintDialog(context,object : HintDialog.HintDialogListener{
                override fun cancel() {

                }

                override fun ok() {
                    getPermission(listener)
                }
            }).show("该功能需要获取手机存储读取权限以及相机权限用于选择图片视频或者拍摄图片视频，是否继续?","获取","取消")
        }
    }

    fun getPermission(listener : DetectionListener){
        PermissionXUtil.requestAnyPermission(XAppActivityManager.getInstance().currentActivity(),ChoiceImageUtil.getPhotoPermissions(),object : OnPermissionCallBackListener {
            override fun onAgree() {
                listener.ok()
            }

            override fun onDisAgree() {

            }
        })
    }

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

    fun getPermission(activity: AppCompatActivity,list : MutableList<String>,listener : DetectionListener){
        PermissionXUtil.requestAnyPermission(activity,list,object : OnPermissionCallBackListener {
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