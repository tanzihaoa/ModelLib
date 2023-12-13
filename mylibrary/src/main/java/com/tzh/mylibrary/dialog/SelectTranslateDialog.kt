package com.tzh.mylibrary.dialog

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.tzh.mylibrary.R
import com.tzh.mylibrary.adapter.MyWheelAdapter
import com.tzh.mylibrary.base.BaseBindingDialog
import com.tzh.mylibrary.databinding.DialogSelectTranslateBinding
import com.tzh.mylibrary.dto.LanguageDto

/**
 * 选择语种弹窗
 */
class SelectTranslateDialog(val mContext : Context, val listener : SelectTranslateListener) : BaseBindingDialog<DialogSelectTranslateBinding>(mContext, R.layout.dialog_select_translate){
    init {
        initBottomDialog()
    }

    override fun initView() {
        binding.wheelViewFrom.setDividerColor(ContextCompat.getColor(context, R.color.transparent))
        binding.wheelViewFrom.setCyclic(false)
        binding.wheelViewFrom.setTextSize(20f)
        binding.wheelViewFrom.setTextColorCenter(ContextCompat.getColor(mContext,R.color.color_86909C))
        binding.wheelViewFrom.adapter = MyWheelAdapter(getList(true))

        binding.wheelViewTo.setDividerColor(ContextCompat.getColor(context, R.color.transparent))
        binding.wheelViewTo.setCyclic(false)
        binding.wheelViewTo.setTextSize(20f)
        binding.wheelViewTo.setTextColorCenter(ContextCompat.getColor(mContext,R.color.color_86909C))
        binding.wheelViewTo.adapter = MyWheelAdapter(getList(false))

        //确定按钮点击
        binding.tvSure.setOnClickListener {
            val from = getList(true)[binding.wheelViewFrom.currentItem]
            val to = getList(false)[binding.wheelViewTo.currentItem]
            if(from.code == to.code){
                Toast.makeText(it.context,"请勿选择一样的语种",Toast.LENGTH_LONG).show()
            }else{
                listener.sure(from,to)
                dismiss()
            }
        }
    }

    override fun initData() {

    }

    interface SelectTranslateListener{
        fun sure(from : LanguageDto,to : LanguageDto)
    }


    private fun getList(isFrom : Boolean): List<LanguageDto> {
        val list: MutableList<LanguageDto> = ArrayList()
        if(isFrom){
            list.add(LanguageDto("自动检测","auto"))
        }
        list.add(LanguageDto("中文","zh"))
        list.add(LanguageDto("英语","en"))
        list.add(LanguageDto("粤语","yue"))
        list.add(LanguageDto("文言文","wyw"))
        list.add(LanguageDto("日语","jp"))
        list.add(LanguageDto("韩语","kor"))
        list.add(LanguageDto("法语","fra"))
        list.add(LanguageDto("西班牙语","spa"))
        list.add(LanguageDto("泰语","th"))
        list.add(LanguageDto("阿拉伯语","ara"))
        list.add(LanguageDto("俄语","ru"))
        list.add(LanguageDto("葡萄牙语","pt"))
        list.add(LanguageDto("德语","de"))
        list.add(LanguageDto("意大利语","it"))
        list.add(LanguageDto("希腊语","el"))
        list.add(LanguageDto("荷兰语","nl"))
        list.add(LanguageDto("波兰语","pl"))
        list.add(LanguageDto("保加利亚语","bul"))
        list.add(LanguageDto("爱沙尼亚语","est"))
        list.add(LanguageDto("丹麦语","dan"))
        list.add(LanguageDto("芬兰语","fin"))
        list.add(LanguageDto("捷克语","cs"))
        list.add(LanguageDto("罗马尼亚语","rom"))
        list.add(LanguageDto("斯洛文尼亚语","slo"))
        list.add(LanguageDto("瑞典语","swe"))
        list.add(LanguageDto("匈牙利语","hu"))
        list.add(LanguageDto("繁体中文","cht"))
        list.add(LanguageDto("越南语","vi"))
        return list
    }
}