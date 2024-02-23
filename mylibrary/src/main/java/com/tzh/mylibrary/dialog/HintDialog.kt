package com.tzh.mylibrary.dialog

import android.content.Context
import android.view.View
import com.tzh.mylibrary.R
import com.tzh.mylibrary.base.BaseBindingDialog
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.databinding.DialogHintBinding

class HintDialog(context : Context, val listener: HintDialogListener) : BaseBindingDialog<DialogHintBinding>(context,R.layout.dialog_hint){

    override fun initView() {
        //取消
        binding.tvCancel.setOnClickNoDouble {
            listener.cancel()
            dismiss()
        }

        //确定
        binding.tvOk.setOnClickNoDouble {
            listener.ok()
            dismiss()
        }
    }

    override fun initData() {

    }

    fun show(content : String) {
        binding.tvContent.text = content
        binding.tvCancel.text = "取消"
        binding.tvOk.text = "确定"
        show()
    }

    fun show(content : String,ok : String) {
        binding.tvTitle.text = "温馨提示"
        binding.tvContent.text = content
        binding.tvOk.text = ok
        binding.tvCancel.visibility = View.GONE
        show()
    }

    fun showError(content : String,ok : String) {
        binding.tvTitle.text = "错误提示"
        binding.tvContent.text = content
        binding.tvOk.text = ok
        binding.tvCancel.visibility = View.GONE
        show()
    }

    fun show(content : String,ok : String,cancel : String) {
        binding.tvContent.text = content
        binding.tvCancel.text = cancel
        binding.tvOk.text = ok
        show()
    }

    interface HintDialogListener{
        fun cancel()

        fun ok()
    }


}