package com.tzh.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import com.huawei.hmf.tasks.Task
import com.huawei.hms.hihealth.DataController
import com.huawei.hms.hihealth.HuaweiHiHealth
import com.huawei.hms.hihealth.data.DataType
import com.huawei.hms.hihealth.data.SampleSet
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityImageBinding
import com.tzh.myapplication.utils.FeatureParser
import com.tzh.mylibrary.util.GsonUtil
import com.tzh.mylibrary.util.LogUtils


class ImageActivity : AppBaseActivity<ActivityImageBinding>(R.layout.activity_image) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, ImageActivity::class.java))
        }
    }

    override fun initView() {
        binding.startView.setStartNum(4.5f)
        val allSteps = FeatureParser.getAllSteps(this)
        var step = 0
        allSteps.forEach {
            step += it.mSteps
        }
        LogUtils.e("步数====",step.toString())
    }

    override fun initData() {

    }

    fun getHuawei(){
        val dataController = HuaweiHiHealth.getDataController(this)
        val todaySummationTask: Task<SampleSet> = dataController.readTodaySummation(DataType.DT_CONTINUOUS_STEPS_DELTA)
        todaySummationTask.addOnSuccessListener {
            LogUtils.e("步数====",GsonUtil.GsonString(it))
        }
    }
}