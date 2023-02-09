package com.tzh.myapplication

import androidx.recyclerview.widget.LinearLayoutManager
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.ui.activity.ImageActivity
import com.tzh.myapplication.ui.activity.ListActivity
import com.tzh.myapplication.ui.dialog.MyDialog
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.mylibrary.view.pickerview.ScrollPickerAdapter.ScrollPickerAdapterBuilder


class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mDialog by lazy {
        MyDialog(this)
    }

    override fun initView() {
        binding.v = this
        binding.bmiView.setBmi(28f)

        // 体重标尺
        binding.mrvRuler.mVlaueListener = {
            binding.tvTz.text = it.toString()
        }
        binding.mrvRuler.setValue(60f)
    }

    override fun initData() {
        val list: MutableList<String> = ArrayList()
        for (i in 0..19) {
            val itemData = "item: $i"
            list.add(itemData)
        }
        binding.scrollPickerView.layoutManager = LinearLayoutManager(this)
        val builder = ScrollPickerAdapterBuilder<String>(this)
            .setDataList(list)
            .selectedItemOffset(1)
            .visibleItemNumber(3)
            .setDivideLineColor("#E5E5E5")
            .setItemViewProvider(null)
            .setOnClickListener { v ->
                val text = v.tag as String
                ToastUtil.show(text)
            }
        val mScrollPickerAdapter = builder.build()
        binding.scrollPickerView.adapter = mScrollPickerAdapter
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