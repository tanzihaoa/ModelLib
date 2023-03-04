package com.tzh.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.ui.activity.ImageActivity
import com.tzh.myapplication.ui.activity.ListActivity
import com.tzh.myapplication.ui.dialog.MyDialog
import com.tzh.mylibrary.util.OnPermissionCallBackListener
import com.tzh.mylibrary.util.PermissionXUtil
import com.tzh.mylibrary.util.SensorManagerHelper


class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mDialog by lazy {
        MyDialog(this)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun initView() {
        binding.v = this


        PermissionXUtil.requestAnyPermission(this, Manifest.permission.ACTIVITY_RECOGNITION,object : OnPermissionCallBackListener{
            override fun onAgree() {
                initText()
            }

            override fun onDisAgree() {

            }
        })
    }

    override fun initData() {

    }

    fun initText(){
        var sensor = SensorManagerHelper(this)
        sensor.setOnSensorEventListener {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                if(it.values.isNotEmpty()){
                    Log.e(TAG, "onSensorChanged: 当前步数：" + it.values[0])
                }
            }
        }
        sensor.startListen()
    }

    fun toRecycler(){
        ListActivity.start(this)
    }

    fun openDialog(){
        mDialog.show()
    }

    fun toImage(){
        ImageActivity.start(this)
    }

    fun start(){

    }


}