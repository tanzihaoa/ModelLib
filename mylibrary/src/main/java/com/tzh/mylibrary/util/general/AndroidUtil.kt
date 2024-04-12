package com.tzh.mylibrary.util.general

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat


object AndroidUtil {

    /**
     * 获取当前版本名
     */
    fun getVersionName(context: Context): String? {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }

    /**
     * 获取当前版本号
     */
    fun getVersionCode(context: Context): Int {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        } catch (e: Exception) {
            0
        }
    }

    /**
     * 复制文字
     */
    fun copy(context: Context,text : String){
        val cm = context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
        cm.text = text
        Toast.makeText(context,"复制成功", Toast.LENGTH_LONG).show()
    }

    //检测应用是否安装
    fun checkMapAppsIsExist(context: Context, packageName: String): Boolean {
        var packageInfo: PackageInfo?
        try {
            packageInfo = context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: java.lang.Exception) {
            packageInfo = null
            e.printStackTrace()
        }
        return packageInfo != null
    }

    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    fun isInstalled(context: Context, packageName: String): Boolean {
        val manager: PackageManager = context.packageManager
        //获取所有已安装程序的包信息
        val installedPackages = manager.getInstalledPackages(0)
        for (info in installedPackages) {
            Log.e("packageName===",info.packageName)
            if (info.packageName == packageName) return true
        }
        return false
    }

    /**
     * 检查是否开启通知权限
     */
    fun checkNotifySetting(context: Context) : Boolean{
        val manager = NotificationManagerCompat.from(context)
        return manager.areNotificationsEnabled()
    }

    /**
     * 开启通知权限
     */
    fun openNotifySetting(context: Context){
        val intent: Intent = Intent()
        try {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS

            //8.0及以后版本使用这两个extra.  >=API 26
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.applicationInfo.uid)

            //5.0-7.1 使用这两个extra.  <= API 25, >=API 21
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)

            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()

            //其他低版本或者异常情况，走该节点。进入APP设置界面
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.putExtra("package", context.packageName)
            context.startActivity(intent)
        }
    }
}