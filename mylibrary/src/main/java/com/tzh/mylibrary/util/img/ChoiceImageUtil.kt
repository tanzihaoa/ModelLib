package com.tzh.mylibrary.util.img

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tzh.mylibrary.util.grid
import com.tzh.mylibrary.util.initAdapter

object ChoiceImageUtil {

    /**
     * 绑定适配器
     */
    fun setChoiceImage(activity: AppCompatActivity, recyclerView: RecyclerView, gridNum: Int, num: Int,isHaveVideo : Boolean = false,isBack : Boolean = false): ChoiceImageAdapter {
        val adapter = ChoiceImageAdapter(activity, num,isHaveVideo,isBack)
        adapter.initView()
        recyclerView.grid(gridNum).initAdapter(adapter)
        return adapter
    }

    fun getList(adapter : ChoiceImageAdapter) : MutableList<ImageDTO>{
        val list: MutableList<ImageDTO> = adapter.getList()
        if (list.isNotEmpty()) {
            if (list[list.size - 1].status == 1) {
                list.removeAt(list.size - 1)
            }
        }

        return list
    }
}