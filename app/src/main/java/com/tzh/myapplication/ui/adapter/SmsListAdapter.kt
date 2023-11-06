package com.tzh.myapplication.ui.adapter

import com.tzh.myapplication.R
import com.tzh.mylibrary.adapter.XRvBindingHolder
import com.tzh.mylibrary.adapter.XRvBindingPureDataAdapter
import com.tzh.myapplication.databinding.AdapterSmsListBinding
import com.tzh.myapplication.ui.dto.SmsDto

class SmsListAdapter : XRvBindingPureDataAdapter<SmsDto>(R.layout.adapter_sms_list){
    override fun onBindViewHolder(holder: XRvBindingHolder, position: Int, data: SmsDto) {
        holder.getBinding<AdapterSmsListBinding>().run {
            this.tvPhone.text = data.mobile
            this.tvContent.text = data.content
            this.tvStatus.text = data.status + "    " + data.time
        }
    }

    fun addData20(dto : SmsDto){
        if(listData.size >= 20){
            removeData(listData.size-1)
        }
        addData(0,dto)
    }
}