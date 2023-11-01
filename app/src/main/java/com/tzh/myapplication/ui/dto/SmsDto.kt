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

    /**
     * 发送状态
     */
    var status : String ?= null,

    /**
     * 发送时间
     */
    var time : String ?= null,
) {
}