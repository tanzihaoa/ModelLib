package com.tzh.myapplication.utils

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.ForwardScope

object PermissionXUtil {
    /**
     *  文件权限
     */
    const val PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

    /**
     * 音频权限
     */
    const val PERMISSION_AUDIO = Manifest.permission.RECORD_AUDIO

    /**
     * 相机权限
     */
    const val PERMISSION_CAMERA = Manifest.permission.CAMERA


    /**
     * 日历权限
     */
    const val PERMISSION_CALENDAR_WRITE = Manifest.permission.WRITE_CALENDAR
    const val PERMISSION_CALENDAR_READ = Manifest.permission.READ_CALENDAR

    /**
     * 获取录音权限
     */
    @JvmStatic
    fun requestRecordPermission(fragment: Fragment?=null,callBack: OnPermissionCallBackListener?=null) {
        fragment?:return
        requestPermission(fragment, mutableListOf<String>().apply {
            add(PERMISSION_AUDIO)
        }, callBack)
    }

    /**
     * 获取录音权限
     */
    @JvmStatic
    fun requestRecordPermission(activity: AppCompatActivity?=null,callBack: OnPermissionCallBackListener?= null) {
        requestPermission(activity, mutableListOf<String>().apply {
           add(PERMISSION_AUDIO)
        }, callBack)
    }

    /**
     * 获取文件权限
     */
    @JvmStatic
    fun requestStoragePermission(fragment: Fragment?=null,callBack: OnPermissionCallBackListener?=null) {
        requestPermission(fragment, mutableListOf<String>().apply {
            add(PERMISSION_STORAGE)
        }, callBack)
    }

    /**
     * 获取文件权限
     */
    @JvmStatic
    fun requestStoragePermission(activity: AppCompatActivity?=null,callBack: OnPermissionCallBackListener?= null) {
        requestPermission(activity, mutableListOf<String>().apply {
            add(PERMISSION_STORAGE)
        }, callBack)
    }

    /**
     * 获取相机权限
     */
    @JvmStatic
    fun requestCameraPermission(fragment: Fragment?=null,callBack: OnPermissionCallBackListener?=null) {
        requestPermission(fragment, mutableListOf<String>().apply {
            add(PERMISSION_CAMERA)
        }, callBack)
    }

    /**
     * 获取相机权限
     */
    @JvmStatic
    fun requestCameraPermission(activity: AppCompatActivity?=null,callBack: OnPermissionCallBackListener?= null) {
        requestPermission(activity, mutableListOf<String>().apply {
            add(PERMISSION_CAMERA)
        }, callBack)
    }

    /**
     * 获取日历权限
     */
    @JvmStatic
    fun requestCalendarPermission(fragment: Fragment?=null,callBack: OnPermissionCallBackListener?=null) {
        fragment?:return
        requestPermission(fragment, mutableListOf<String>().apply {
            add(PERMISSION_CALENDAR_WRITE)
            add(PERMISSION_CALENDAR_READ)
        }, callBack)
    }

    /**
     * 获取日历权限
     */
    @JvmStatic
    fun requestCalendarPermission(activity: AppCompatActivity?=null,callBack: OnPermissionCallBackListener?= null) {
        requestPermission(activity, mutableListOf<String>().apply {
            add(PERMISSION_CALENDAR_WRITE)
            add(PERMISSION_CALENDAR_READ)
        }, callBack)
    }

    /**
     * 获取所有权限
     */
    @JvmStatic
    fun requestAllPermission(activity: AppCompatActivity?=null,callBack: OnPermissionCallBackListener?= null) {
        requestPermission(activity, mutableListOf<String>().apply {
            add(PERMISSION_STORAGE)
            add(PERMISSION_CAMERA)
            add(PERMISSION_AUDIO)
        }, callBack)
    }

    /**
     * 获取相册权限，文件读写以及相机
     */
    @JvmStatic
    fun requestPhotoPermission(activity: AppCompatActivity?=null,callBack: OnPermissionCallBackListener?= null) {
        requestPermission(activity, mutableListOf<String>().apply {
            add(PERMISSION_STORAGE)
            add(PERMISSION_CAMERA)
        }, callBack)
    }

    /**
     * 获取某一个权限
     */
    @JvmStatic
    fun requestAnyPermission(activity: AppCompatActivity?=null,permission : String,callBack: OnPermissionCallBackListener?= null) {
        requestPermission(activity, mutableListOf<String>().apply {
            add(permission)
        }, callBack)
    }

    /**
     * 获取多个个权限
     */
    @JvmStatic
    fun requestAnyPermission(activity: AppCompatActivity?=null, permission : MutableList<String>, callBack: OnPermissionCallBackListener?= null) {
        requestPermission(activity,permission, callBack)
    }

    private fun requestPermission(activity: AppCompatActivity?=null, permission: MutableList<String>, callBack: OnPermissionCallBackListener?= null){
        PermissionX.init(activity).permissions(permission).onExplainRequestReason { scope, deniedList ->
            val message = "需要您同意以下权限才能正常使用"
            scope.showRequestReasonDialog(deniedList, message, "确定", "取消")
        }.onForwardToSettings { scope: ForwardScope, deniedList: List<String> ->
            scope.showForwardToSettingsDialog(deniedList, "您需要手动在设置中允许必要的权限", "确定", "取消")
        }.request { allGranted: Boolean, _: List<String?>?, _: List<String?>? ->
            if (allGranted) {
                callBack?.onAgree()
            } else {
                callBack?.onDisAgree()
            }
        }
    }

    private fun requestPermission(activity: Fragment?=null, permission: MutableList<String>, callBack: OnPermissionCallBackListener?=null){
        PermissionX.init(activity).permissions(permission).onExplainRequestReason { scope, deniedList ->
            val message = "需要您同意以下权限才能正常使用"
            scope.showRequestReasonDialog(deniedList, message, "确定", "取消")
        }.onForwardToSettings { scope: ForwardScope, deniedList: List<String> ->
            scope.showForwardToSettingsDialog(deniedList, "您需要手动在设置中允许必要的权限", "确定", "取消")
        }.request { allGranted: Boolean, _: List<String?>?, _: List<String?>? ->
            if (allGranted) {
                callBack?.onAgree()
            } else {
                callBack?.onDisAgree()
            }
        }
    }

}

/**
 * 权限回调
 */
interface OnPermissionCallBackListener {
    /**
     * 同意权限
     */
    fun onAgree()

    /**
     * 拒绝权限
     */
    fun onDisAgree()
}