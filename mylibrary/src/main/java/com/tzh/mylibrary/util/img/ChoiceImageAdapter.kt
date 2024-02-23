package com.tzh.mylibrary.util.img

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tzh.mylibrary.adapter.XRvBindingHolder
import com.tzh.mylibrary.adapter.XRvBindingPureDataAdapter
import com.tzh.mylibrary.util.GsonUtil
import com.tzh.mylibrary.util.LoadImageUtil
import com.tzh.mylibrary.util.toDefault
import com.tzh.mylibrary.R
import com.tzh.mylibrary.databinding.AdapterChoiceImageBinding
import com.tzh.mylibrary.util.general.PermissionDetectionUtil

class ChoiceImageAdapter(val activity : AppCompatActivity, private val num : Int = 9,val isHaveVideo : Boolean = false) : XRvBindingPureDataAdapter<ImageDTO>(R.layout.adapter_choice_image)  {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: XRvBindingHolder, position: Int, data: ImageDTO) {
        val b = holder.getBinding<AdapterChoiceImageBinding>()

        LoadImageUtil.loadImageUrl(b.ivImg,data.file?.absolutePath.toDefault(data.image),4f)

        b.data.visibility = if (data.status == 1) View.GONE else View.VISIBLE
        b.tvNumber.text = if(isHaveVideo) "照片或视频" else "最多上传" + num + "张"
        b.llImgChoose.visibility = if (data.status == 1) View.VISIBLE else View.GONE
        b.rlDel.setOnClickListener { remove(position) }

        b.llImgChoose.setOnClickListener {
            PermissionDetectionUtil.detection(activity,object : PermissionDetectionUtil.DetectionListener{
                override fun ok() {
                    if (listData.size >= num) {
                        if (listData[num - 1].status == 1) {
                            if(isHaveVideo){
                                CameraUtil.createAlbumComplex(activity, num - listData.size + 1,
                                    object : CameraUtil.onSelectCallback{
                                        override fun onResult(photos: List<ImageDTO>?) {
                                            addDataList(photos)
                                        }

                                        override fun onCancel() {}
                                    })
                            }else{
                                CameraUtil.createAlbum(
                                    activity,
                                    num - listData.size + 1,
                                    object : CameraUtil.onSelectCallback{
                                        override fun onResult(photos: List<ImageDTO>?) {
                                            addDataList(photos)
                                        }

                                        override fun onCancel() {}
                                    })
                            }
                        }
                    } else {
                        if(isHaveVideo){
                            CameraUtil.createAlbumComplex(activity, num - listData.size + 1,
                                object : CameraUtil.onSelectCallback {
                                    override fun onResult(photos: List<ImageDTO>?) {
                                        addDataList(photos)
                                    }

                                    override fun onCancel() {}
                                })
                        }else{
                            CameraUtil.createAlbum(
                                activity,
                                num - listData.size + 1,
                                object : CameraUtil.onSelectCallback{
                                    override fun onResult(photos: List<ImageDTO>?) {
                                        addDataList(photos)
                                    }

                                    override fun onCancel() {}
                                })
                        }
                    }
                }
            })
        }
    }

    var mListener : ImageChangeListener?= null
    fun setListener(listener : ImageChangeListener){
        mListener = listener
    }

    fun getList(): MutableList<ImageDTO> {
        return listData
    }

    fun remove(position: Int) {
        listData.removeAt(position)
        initView()
        mListener?.change()
    }

    fun removeAll() {
        listData.clear()
        initView()
        mListener?.change()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initView() {
        if (listData.size < num) {
            if (listData.size > 0) {
                if(listData[0].type == 2){

                }else if (listData[listData.size - 1].status != 1) {
                    listData.add(ImageDTO(null, 1))
                }
            } else {
                listData.add(ImageDTO(null, 1))
            }
        }
        Log.e("mList====", GsonUtil.GsonString(listData))
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(list: List<ImageDTO>?) {
        if (list != null) {
            this.listData.clear()
            this.listData.addAll(list)
            initView()
            notifyDataSetChanged()
        }
    }

    fun addDataList(list: List<ImageDTO>?) {
        if (list != null) {
            if (listData.size > 0) {
                if (listData[listData.size - 1].status == 1) {
                    listData.removeAt(listData.size - 1)
                }
            }
            val lastIndex: Int = this.listData.size - 1
            if (listData.addAll(list)) {
                initView()
            }

            mListener?.change()
        }
    }

    interface ImageChangeListener{
        fun change()
    }
}