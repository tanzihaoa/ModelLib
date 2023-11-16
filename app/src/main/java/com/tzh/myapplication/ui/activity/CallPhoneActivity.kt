package com.tzh.myapplication.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telecom.Call
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityCallPhoneBinding
import com.tzh.myapplication.utils.AndroidUtil
import com.tzh.myapplication.utils.OnPermissionCallBackListener
import com.tzh.myapplication.utils.PermissionXUtil


class CallPhoneActivity : AppBaseActivity<ActivityCallPhoneBinding>(R.layout.activity_call_phone) {
    companion object {
        @JvmStatic
        fun start(context: AppCompatActivity) {
            PermissionXUtil.requestAnyPermission(context, mutableListOf<String>().apply {
                add(Manifest.permission.MANAGE_OWN_CALLS)
                add(Manifest.permission.PROCESS_OUTGOING_CALLS)
                add(Manifest.permission.CALL_PHONE)
            },object : OnPermissionCallBackListener{
                override fun onAgree() {
                    context.startActivity(Intent(context, CallPhoneActivity::class.java))
                }

                override fun onDisAgree() {

                }
            })
        }
    }


    override fun initView() {
        binding.titleBar.setRightTitleTxt(AndroidUtil.getVersionName(this))
        binding.activity = this


    }

    override fun initData() {

    }


    fun sendSms(){
        val phone = binding.etPhone.text.toString()
        val phone2 = binding.etPhone2.text.toString()
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//            val call1: Call = createCall(phone)
//            val call2: Call = createCall(phone2)
//
//            startThreeWayCall(call1, call2)
        }
    }

//    private fun createCall(phoneNumber: String): Call {
//        val phoneUri = Uri.parse("tel:$phoneNumber")
//        return getTelecomManager().placeCall(phoneUri, CallIntentBuilder().build())
//    }

//    private fun startThreeWayCall(call1: Call, call2: Call) {
//        if (call1.isConferenceSupported() && call2.isConferenceSupported()) {
//            call1.conference(call2)
//        } else {
//            Toast.makeText(this, R.string.conference_not_supported, Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun getTelecomManager(): TelecomManager {
        return getSystemService(TELECOM_SERVICE) as TelecomManager
    }
}