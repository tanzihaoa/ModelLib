package com.tzh.myapplication.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telecom.Call
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityBluetoothBinding
import com.tzh.myapplication.databinding.ActivityCallPhoneBinding
import com.tzh.myapplication.utils.AndroidUtil
import com.tzh.myapplication.utils.OnPermissionCallBackListener
import com.tzh.myapplication.utils.PermissionXUtil


class BluetoothActivity : AppBaseActivity<ActivityBluetoothBinding>(R.layout.activity_bluetooth) {
    companion object {
        @JvmStatic
        fun start(context : Context) {
            context.startActivity(Intent(context, BluetoothActivity::class.java))
        }
    }


    override fun initView() {
        binding.activity = this


    }

    override fun initData() {

    }


    fun sendSms(){

        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

        }
    }

}