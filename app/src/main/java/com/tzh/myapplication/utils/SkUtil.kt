package com.tzh.myapplication.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import androidx.core.content.ContextCompat.startActivity
import com.tzh.myapplication.service.SkAccessibilityService


object SkUtil {
    /**
     * 判断无障碍服务是否开启
     * @param mContext
     * @return
     */
    @JvmStatic
    fun isAccessibilitySettingsOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        val service =
            mContext.packageName + "/" + SkAccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {

        }
        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue: String = Settings.Secure.getString(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessibilityService = mStringColonSplitter.next()
                if (accessibilityService.equals(service, ignoreCase = true)) {
                    return true
                }
            }
        } else {
        }
        return false
    }

    /**
     * 去开启无障碍模式
     */
    fun start(mContext: Context){
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        mContext.startActivity(intent)
    }
}