package com.tzh.mylibrary.util.img

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tzh.mylibrary.util.grid
import com.tzh.mylibrary.util.initAdapter

object ChoiceImageUtil {

    /**
     * 绑定适配器
     */
    fun setChoiceImage(activity: AppCompatActivity, recyclerView: RecyclerView, gridNum: Int, num: Int,isHaveVideo : Boolean = false,isBack : Boolean = false,isCamera : Boolean = true): ChoiceImageAdapter {
        val adapter = ChoiceImageAdapter(activity, num,isHaveVideo,isBack,isCamera)
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

    /**
     * 选择图片所需权限
     * @param isCamera 是否需要拍照权限
     */
    fun getPhotoPermissions(isCamera : Boolean = true): MutableList<String> {
        return mutableListOf<String>().apply {
            if(isCamera){
                add(Manifest.permission.CAMERA)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_IMAGES)
                add(Manifest.permission.READ_MEDIA_VIDEO)
            } else {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
}