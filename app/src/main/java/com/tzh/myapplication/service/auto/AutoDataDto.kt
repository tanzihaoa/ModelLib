package com.tzh.myapplication.service.auto

class AutoDataDto {
    /**
     * 商品名字
     */
    var name : String ?= null

    /**
     * 交易金额
     */
    var money : Float ?= null

    /**
     * 类型
     */
    var type : String ?= null

    /**
     * 交易时间
     */
    var time : String ?= null

    /**
     * 备注
     */
    var remark : String ?= null

    /**
     * 付款方式
     */
    var payment : String ?= null

    /**
     * 交易订单
     */
    var orderNo : String ?= null
}