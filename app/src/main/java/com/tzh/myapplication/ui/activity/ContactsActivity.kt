package com.tzh.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityContactsBinding
import com.tzh.myapplication.ui.adapter.ContactsAdapter
import com.tzh.myapplication.ui.dto.UserDto
import com.tzh.mylibrary.util.initAdapter
import com.tzh.mylibrary.util.linear
import java.util.Collections

/**
 * 联系人页面
 */
class ContactsActivity : AppBaseActivity<ActivityContactsBinding>(R.layout.activity_contacts){
    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,ContactsActivity::class.java))
        }
    }

    val mAdapter by lazy {
        ContactsAdapter()
    }

    override fun initView() {
        binding.recyclerView.linear().initAdapter(mAdapter)
        binding.sidebar.setOnStrSelectCallBack { _, selectStr ->
            for((index,dto) in mAdapter.listData.withIndex()){
                if(selectStr.equals(dto.start,true)){
                    binding.recyclerView.scrollToPosition(index)
                    referrer
                }
            }
        }
    }

    override fun initData() {
        val users = mutableListOf<UserDto>()
        users.add(UserDto("丽丽"))
        users.add(UserDto("妈妈"))
        users.add(UserDto("爸爸"))
        users.add(UserDto("#xx"))
        users.add(UserDto("虹猫"))
        users.add(UserDto("蓝兔"))
        users.add(UserDto("阿牛"))
        users.add(UserDto("CK"))
        users.add(UserDto("灯虎"))
        users.add(UserDto("尔康"))
        users.add(UserDto("凡哥"))
        users.add(UserDto("Gr"))
        users.add(UserDto("123阿斯顿"))
        users.add(UserDto("合格几个号"))
        users.add(UserDto("高合金钢"))
        users.add(UserDto("爸爸"))
        users.add(UserDto("士"))
        users.add(UserDto("圩"))
        users.add(UserDto("u图6"))
        users.add(UserDto("欧阳"))
        users.add(UserDto("的费用士"))
        users.sort()

        mAdapter.setDatas(users)
    }
}