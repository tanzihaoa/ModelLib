package com.tzh.myapplication.ui.adapter

import com.tzh.myapplication.R
import com.tzh.mylibrary.adapter.XRvBindingHolder
import com.tzh.mylibrary.adapter.XRvBindingPureDataAdapter
import com.tzh.myapplication.databinding.AdapterListBinding
import com.tzh.myapplication.databinding.AdapterSmsListBinding
import com.tzh.myapplication.ui.dto.MasterShopListDto
import com.tzh.myapplication.ui.dto.SmsDto
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.util.toDefault

class SmsListAdapter : XRvBindingPureDataAdapter<SmsDto>(R.layout.adapter_sms_list){
    override fun onBindViewHolder(holder: XRvBindingHolder, position: Int, data: SmsDto) {
        holder.getBinding<AdapterSmsListBinding>().run {
            this.contentTv.text = data.mobile
            this.statusTv.text = data.content
        }
    }
}