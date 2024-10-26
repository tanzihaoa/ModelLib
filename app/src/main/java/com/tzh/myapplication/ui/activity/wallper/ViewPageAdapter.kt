package com.tzh.myapplication.ui.activity.wallper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * ViewPage 适配器
 */
class ViewPageAdapter(manager: FragmentManager, val list : MutableList<Fragment>) : FragmentPagerAdapter(manager) {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }
}