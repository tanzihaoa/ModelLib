package com.tzh.mylibrary.util.general

import androidx.appcompat.app.AppCompatActivity
import com.tzh.mylibrary.util.OnPermissionCallBackListener
import com.tzh.mylibrary.util.PermissionXUtil
import com.tzh.mylibrary.util.checkPhonePermission
import com.tzh.mylibrary.util.img.ChoiceImageUtil

object PermissionDetectionUtil {

    /**
     * @param isBack 没有权限的时候是否返回
     */
    fun getPermission(activity: AppCompatActivity,listener : DetectionListener,isBack : Boolean = false,isCamera : Boolean = true){
        if(isBack){
            if(ChoiceImageUtil.getPhotoPermissions(isCamera).checkPhonePermission(activity)){
                //有这个权限
                listener.ok()
            } else {
                //没有这个权限
                listener.cancel()
            }
        }else{
            getPermissionNow(activity,listener,isCamera)
        }
    }

    private fun getPermissionNow(activity: AppCompatActivity,listener : DetectionListener,isCamera : Boolean = true){
        PermissionXUtil.requestAnyPermission(activity, ChoiceImageUtil.getPhotoPermissions(isCamera),object : OnPermissionCallBackListener {
            override fun onAgree() {
                listener.ok()
            }

            override fun onDisAgree() {

            }
        })
    }

    interface DetectionListener{
        fun ok()

        fun cancel()
    }
}