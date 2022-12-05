package com.tzh.myapplication.ui.adapter

import com.tzh.myapplication.R
import com.tzh.myapplication.adapter.XRvBindingHolder
import com.tzh.myapplication.adapter.XRvBindingPureDataAdapter
import com.tzh.myapplication.databinding.AdapterListBinding
import com.tzh.myapplication.ui.dto.MasterShopListDto
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.myapplication.utils.setOnClickNoDouble

class ListAdapter : XRvBindingPureDataAdapter<MasterShopListDto>(R.layout.adapter_list){
    override fun onBindViewHolder(holder: XRvBindingHolder, position: Int, data: MasterShopListDto) {
        holder.getBinding<AdapterListBinding>().run {
            this.dto = data
            this.title.setOnClickNoDouble {
                ToastUtil.show(data.title)
            }
        }
    }
}