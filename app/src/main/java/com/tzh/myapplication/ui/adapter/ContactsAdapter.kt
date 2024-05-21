package com.tzh.myapplication.ui.adapter

import android.view.View
import com.tzh.myapplication.R
import com.tzh.myapplication.databinding.AdapterContactsBinding
import com.tzh.mylibrary.adapter.XRvBindingHolder
import com.tzh.mylibrary.adapter.XRvBindingPureDataAdapter
import com.tzh.myapplication.databinding.AdapterListBinding
import com.tzh.myapplication.ui.dto.SmsListDto
import com.tzh.myapplication.ui.dto.UserDto
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.util.toDefault

class ContactsAdapter : XRvBindingPureDataAdapter<UserDto>(R.layout.adapter_contacts){
    override fun onBindViewHolder(holder: XRvBindingHolder, position: Int, data: UserDto) {
        holder.getBinding<AdapterContactsBinding>().run {
            this.name.text = data.name
            if(position == getPosition(data.start)){
                letterLayout.visibility = View.VISIBLE
                letter.text = data.start
            }else{
                letterLayout.visibility = View.GONE
            }
        }
    }

    private fun getPosition(mark : String) : Int{
        for ((index,dto) in listData.withIndex()){
            if (dto.start.equals(mark,true)){
                return index
            }
        }
        return -1;
    }
}