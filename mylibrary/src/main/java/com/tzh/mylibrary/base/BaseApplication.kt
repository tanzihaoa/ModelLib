package com.tzh.mylibrary.base

import android.app.Application
import android.content.Context

abstract class BaseApplication : Application() {

    companion object {
        lateinit var sContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        sContext = this
    }


}