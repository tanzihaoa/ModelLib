package com.tzh.myapplication.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import com.huawei.hmf.tasks.Task
import com.huawei.hms.hihealth.HuaweiHiHealth
import com.huawei.hms.hihealth.data.DataType
import com.huawei.hms.hihealth.data.SampleSet
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityImageBinding
import com.tzh.myapplication.utils.FeatureParser
import com.tzh.mylibrary.util.GsonUtil
import com.tzh.mylibrary.util.LogUtils
import com.tzh.myapplication.utils.OnPermissionCallBackListener
import com.tzh.myapplication.utils.PermissionXUtil
import com.tzh.mylibrary.util.setOnClickNoDouble


class ImageActivity : AppBaseActivity<ActivityImageBinding>(R.layout.activity_image) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, ImageActivity::class.java))
        }
    }

    override fun initView() {
        binding.startView.post {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                PermissionXUtil.requestAnyPermission(this, Manifest.permission.ACTIVITY_RECOGNITION,object :
                    OnPermissionCallBackListener {
                    override fun onAgree() {
                        getXiaomi()
                    }

                    override fun onDisAgree() {

                    }
                })
            }else{
                getXiaomi()
            }
        }
    }

    override fun initData() {

    }

    @SuppressLint("SetTextI18n")
    fun getXiaomi(){
        val allSteps = FeatureParser.getAllSteps(this)
        var step = 0
        allSteps.forEach {
            step += it.mSteps
        }
        binding.tvText.text = "步数==$step"
        LogUtils.e("步数====",step.toString())
    }

    fun getHuawei(){
        val dataController = HuaweiHiHealth.getDataController(this)
        val todaySummationTask: Task<SampleSet> = dataController.readTodaySummation(DataType.DT_CONTINUOUS_STEPS_DELTA)
        todaySummationTask.addOnSuccessListener {
            LogUtils.e("步数====",GsonUtil.GsonString(it))
            binding.tvText.text = GsonUtil.GsonString(it)
        }
    }
}