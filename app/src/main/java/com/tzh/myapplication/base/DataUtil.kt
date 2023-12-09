package com.tzh.myapplication.base

import com.tzh.myapplication.ui.dto.SmsListDto

object DataUtil {
    fun getData() : MutableList<SmsListDto>{
        val list = mutableListOf<SmsListDto>()
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        list.add(SmsListDto("哈哈哈","测试测试测试","2023-12-09 12:12:12"))
        return list
    }
}