package com.tzh.myapplication.ui.dto

class BaseCodeDto (
    /**
     * 手机品牌
     */
    var name : String ?= null,

    /**
     * 品牌集合
     */
    var models : MutableList<String> ?= null,

    /**
     * 品牌集合
     */
    var list : MutableList<PhoneCodeDto> ?= null
)