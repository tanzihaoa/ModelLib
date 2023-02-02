package com.tzh.mylibrary.util

import android.util.Log
import com.tzh.mylibrary.BuildConfig

object LogUtils {
    private const val TAG = "LogUtils"

    @JvmStatic
    fun e(tag : String?,text : String){
        if(BuildConfig.DEBUG){
            Log.e(tag.toDefault(TAG),text)
        }
    }

    @JvmStatic
    fun d(tag : String?,text : String){
        if(BuildConfig.DEBUG){
            Log.d(tag.toDefault(TAG),text)
        }
    }

    @JvmStatic
    fun i(tag : String?,text : String){
        if(BuildConfig.DEBUG){
            Log.i(tag.toDefault(TAG),text)
        }
    }

    @JvmStatic
    fun w(tag : String?,text : String){
        if(BuildConfig.DEBUG){
            Log.w(tag.toDefault(TAG),text)
        }
    }

    @JvmStatic
    fun v(tag : String?,text : String){
        if(BuildConfig.DEBUG){
            Log.v(tag.toDefault(TAG),text)
        }
    }

    fun e(t: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, t.toString())
        }
    }
}