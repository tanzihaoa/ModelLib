package com.tzh.mylibrary.activity

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.tzh.mylibrary.R
import com.tzh.mylibrary.base.XBaseBindingActivity
import com.tzh.mylibrary.databinding.ActivityTranslateBinding
import com.tzh.mylibrary.dialog.SelectTranslateDialog
import com.tzh.mylibrary.dto.LanguageDto
import com.tzh.mylibrary.network.LibNetWorkApi
import com.tzh.mylibrary.util.toDefault

/**
 * 翻译页面
 */
class TranslateActivity : XBaseBindingActivity<ActivityTranslateBinding>(R.layout.activity_translate) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context,TranslateActivity::class.java))
        }
    }

    var mFrom = LanguageDto("中文","zh")

    var mTo = LanguageDto("英语","en")

    val mDialog by lazy {
        SelectTranslateDialog(this,object : SelectTranslateDialog.SelectTranslateListener{
            override fun sure(from: LanguageDto, to: LanguageDto) {
                mFrom = from
                mTo = to
                binding.tvTextForm.text = from.text
                binding.tvTextTo.text = to.text
            }
        })
    }

    override fun initView() {
        binding.activity = this

    }

    override fun initData() {

    }

    override fun onCloseActivity() {

    }

    /**
     * 翻译
     */
    fun translate(){
        val text = binding.etText1.text.toString()
        if(text.isEmpty()){
            Toast.makeText(this,"请输入要翻译的内容",Toast.LENGTH_LONG).show()
        }else{
            LibNetWorkApi.translate(this,text,mFrom.code.toDefault("zh"),mTo.code.toDefault("en")).subscribe({
                if(it.getDataDto().size > 0){
                    val dto = it.getDataDto()[0]
                    binding.etText2.setText(dto.dst)
                }
            },{
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            })
        }
    }

    fun change(){
        mDialog.show()
    }
}