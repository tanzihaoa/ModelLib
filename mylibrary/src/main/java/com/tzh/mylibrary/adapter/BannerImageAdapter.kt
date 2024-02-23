package com.tzh.mylibrary.adapter

import android.view.View
import com.tzh.mylibrary.util.LoadImageUtil
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.R
import com.tzh.mylibrary.databinding.BannerAdapterBinding

class BannerImageAdapter(data: List<String>,val listener : View.OnClickListener) : BaseBannerAdapter<String>(R.layout.banner_adapter,data) {

    override fun onBindView(holder: XRvBindingHolder, data: String, position: Int, size: Int) {
        holder.getBinding<BannerAdapterBinding>().apply {
            LoadImageUtil.loadImageUrl(image,data)
            image.setOnClickNoDouble {
                listener.onClick(it)
            }
        }
    }
}