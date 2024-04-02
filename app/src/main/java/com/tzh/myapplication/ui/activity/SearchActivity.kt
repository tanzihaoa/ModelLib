package com.tzh.myapplication.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivitySearchBinding
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.mylibrary.view.SearchView


class SearchActivity : AppBaseActivity<ActivitySearchBinding>(R.layout.activity_search) {
    companion object {
        @JvmStatic
        fun start(context: AppCompatActivity) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    }

    override fun initView() {
        binding.activity = this

        binding.searchView.setSvSearchListener(object : SearchView.SvSearchListener{
            override fun search(text: String): Boolean {
                ToastUtil.show(text)
                return true
            }

            override fun clear() {

            }
        })
    }

    override fun initData() {

    }
}