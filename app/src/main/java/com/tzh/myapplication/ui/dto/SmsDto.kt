package com.tzh.myapplication.ui.dto

class SmsDto(
    /**
     * 短信ID
     */
    var id : String ?= null,

    /**
     * 发送手机号码
     */
    var mobile : String ?= null,

    /**
     * 发送内容
     */
    var content : String ?= null,
) {
}