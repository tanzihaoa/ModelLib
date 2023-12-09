package com.tzh.myapplication.ui.adapter

import com.tzh.myapplication.R
import com.tzh.mylibrary.adapter.XRvBindingHolder
import com.tzh.mylibrary.adapter.XRvBindingPureDataAdapter
import com.tzh.myapplication.databinding.AdapterListBinding
import com.tzh.myapplication.ui.dto.SmsListDto
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.util.toDefault

class ListAdapter : XRvBindingPureDataAdapter<SmsListDto>(R.layout.adapter_list){
    override fun onBindViewHolder(holder: XRvBindingHolder, position: Int, data: SmsListDto) {
        holder.getBinding<AdapterListBinding>().run {
            this.controlIv.setOnClickListener {
                this.swipeView.smoothExpand()
            }
            this.contentTv.text = data.content.toDefault("")
            this.updateIv.setOnClickNoDouble {
                if (holder.bindingAdapterPosition >= 0) {

                }
            }
            this.deleteIv.setOnClickNoDouble {
                if (holder.bindingAdapterPosition >= 0) {
                    removeData(data)
                }
            }
        }
    }
}