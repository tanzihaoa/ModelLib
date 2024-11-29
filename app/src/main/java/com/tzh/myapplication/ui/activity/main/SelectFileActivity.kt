package com.tzh.myapplication.ui.activity.main

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.molihuan.pathselector.PathSelector
import com.molihuan.pathselector.entity.FileBean
import com.molihuan.pathselector.fragment.BasePathSelectFragment
import com.molihuan.pathselector.fragment.impl.PathSelectFragment
import com.molihuan.pathselector.listener.CommonItemListener
import com.molihuan.pathselector.service.IConfigDataBuilder
import com.molihuan.pathselector.utils.MConstants
import com.molihuan.pathselector.utils.Mtools
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivitySelectFileBinding
import com.tzh.mylibrary.util.toDefault


/**
 * 选择文件 页面
 */
class SelectFileActivity : AppBaseActivity<ActivitySelectFileBinding>(R.layout.activity_select_file) {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,SelectFileActivity::class.java))
        }
    }
    var selector : PathSelectFragment?= null
    override fun initView() {
        //获取PathSelectFragment实例然后在onBackPressed中处理返回按钮点击事件
        //获取PathSelectFragment实例然后在onBackPressed中处理返回按钮点击事件
        selector = PathSelector.build(this, MConstants.BUILD_FRAGMENT)
            .setFrameLayoutId(R.id.frame_layout) //加载位置,FrameLayout的ID
            .setTitlebarBG(ContextCompat.getColor(this,R.color.colorPrimary))
            .setRequestCode(635)
            .setShowFileTypes("xml", "cvs", "xls")
            .setSelectFileTypes("xml", "cvs", "xls")
            .setMaxCount(1)
            .setShowSelectStorageBtn(false)//设置是否显示内部存储选择按钮
            .setMorePopupItemListeners(
                object : CommonItemListener("完成") {
                    override fun onClick(
                        v: View,
                        tv: TextView,
                        selectedFiles: List<FileBean>,
                        currentPath: String,
                        pathSelectFragment: BasePathSelectFragment
                    ): Boolean {
                        val builder = StringBuilder()
                        builder.append("you selected:\n")
                        for (fileBean in selectedFiles) {
                            builder.append(fileBean.path + "\n")
                        }
                        finish()
                        Mtools.toast(builder.toString())
                        return false
                    }
                }
            )
            .show() //开始构建

    }

    override fun initData() {

    }

    override fun onBackPressed() {
        if (selector != null && selector?.onBackPressed().toDefault(true)) {
            return;
        }
        super.onBackPressed()
    }
}