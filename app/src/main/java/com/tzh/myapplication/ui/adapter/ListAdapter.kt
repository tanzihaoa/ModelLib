package com.tzh.myapplication.ui.adapter

import com.tzh.myapplication.R
import com.tzh.myapplication.databinding.AdapterListBinding
import com.tzh.myapplication.ui.dto.ListDTO
import com.tzh.mylibrary.adapter.XRvBindingHolder
import com.tzh.mylibrary.adapter.XRvBindingPureDataAdapter

class ListAdapter : XRvBindingPureDataAdapter<ListDTO>(R.layout.adapter_list){
    override fun onBindViewHolder(holder: XRvBindingHolder, position: Int, data: ListDTO) {
        holder.getBinding<AdapterListBinding>().run {
            this.dto = data
        }
    }
}