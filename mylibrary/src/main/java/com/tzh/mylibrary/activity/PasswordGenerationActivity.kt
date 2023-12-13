package com.tzh.mylibrary.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.tzh.mylibrary.R
import com.tzh.mylibrary.base.XBaseBindingActivity
import com.tzh.mylibrary.databinding.ActivityPasswordGenerationBinding
import com.tzh.mylibrary.util.setOnClickNoDouble
import kotlin.random.Random

/**
 * 字符生成器
 */
class PasswordGenerationActivity : XBaseBindingActivity<ActivityPasswordGenerationBinding>(R.layout.activity_password_generation)  {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, PasswordGenerationActivity::class.java))
        }
    }
    //数字
    val num = "0123456789"
    //小写字母
    val smallLetter = "abcdefghijklmnopqrstuvwxyz"
    //大写字母
    val bigLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    //特殊符号
    val specialCharacter = "!@#¥%&*()"
    /**
     * 默认字符长度
     */
    var length = 8

    override fun initView() {
        //默认选中数字 大小写字母
        binding.tvNum.isSelected = true
        binding.tvSmallLetter.isSelected = true
        binding.tvBigLetter.isSelected = true
        //数字选中事件
        binding.tvNum.setOnClickListener {
            binding.tvNum.isSelected = !binding.tvNum.isSelected
        }
        //小写字母选中事件
        binding.tvSmallLetter.setOnClickListener {
            binding.tvSmallLetter.isSelected = !binding.tvSmallLetter.isSelected
        }
        //大写字母选中事件
        binding.tvBigLetter.setOnClickListener {
            binding.tvBigLetter.isSelected = !binding.tvBigLetter.isSelected
        }
        //特殊符号选中事件
        binding.tvSpecialCharacter.setOnClickListener {
            binding.tvSpecialCharacter.isSelected = !binding.tvSpecialCharacter.isSelected
        }

        //复制字符
        binding.ivCopy.setOnClickNoDouble {
            val pwd = findViewById<TextView>(R.id.tv_psw).text.toString()
            if(TextUtils.isEmpty(pwd)){
                Toast.makeText(this,"请先生成字符", Toast.LENGTH_SHORT).show()
            }else{
                val str: ClipData = ClipData.newPlainText("Label", pwd)
                val cm: ClipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(str)
                Toast.makeText(this,"复制成功",Toast.LENGTH_LONG).show()
            }
        }

        //生成字符
        binding.tvCreate.setOnClickNoDouble {
            var passwordText = ""
            //如果选中了数字
            if(binding.tvNum.isSelected){
                passwordText += num
            }
            //如果选中了小写字母
            if(binding.tvSmallLetter.isSelected){
                passwordText += smallLetter
            }
            //如果选中了大写字母
            if(binding.tvBigLetter.isSelected){
                passwordText += bigLetter
            }
            //如果选中了特殊符号
            if(binding.tvSpecialCharacter.isSelected){
                passwordText += specialCharacter
            }
            //如果没选
            if(TextUtils.isEmpty(passwordText)){
                Toast.makeText(this,"请选择字符组合类型",Toast.LENGTH_LONG).show()
            }

            //字符
            var password = ""
            //循环取
            for(index in 1..length){
                val num : Int = Random.nextInt(passwordText.length - 1)
                password += passwordText.substring(num,num+1)
            }

            binding.tvPsw.text = password

        }

        /**
         * 字符长度监听
         */
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar : SeekBar?, progress : Int, p2: Boolean) {
                length = progress
                findViewById<TextView>(R.id.tv_length).text = progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    override fun initData() {

    }

    override fun onCloseActivity() {

    }
}