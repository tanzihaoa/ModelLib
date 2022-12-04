package com.tzh.myapplication.ui.dto

/**
 * 好物推荐dto ，
 */
class MasterShopListDto(
    /**
     * 评论数
     */
    var comment: String? = null,
    /**
     * 商品折扣
     */
    var cut: String? = null,
    /**
     * 折扣结束时间
     */
    var end: String? = null,
    /**
     * 商品功效
     */
    var func: String? = null,
    /**
     *  物品 id
     */
    var id: Int = 0,
    /**
     * 是否活动商品
     */
    var isactive: Int = 0,
    /**
     * 是否新品
     */
    var isnew: Int = 0,
    /**
     * 是否制定
     */
    var istop: Int = 0,
    /**
     * 大图
     */
    var mainImage: String? = null,
    /**
     * 月销量
     */
    var monthnum: String? = null,
    /**
     * 价格
     */
    var price: String? = null,
    /**
     * 总销量
     */
    var salenum: String? = null,
    /**
     * 商品原价
     */
    var saleprice: String? = null,
    var solevar: String? = null,
    /**
     * 小图
     */
    var spreadImage: String? = null,
    /**
     * 折扣开始时间
     */
    var start: String? = null,
    /**
     * 商品名称
     */
    var title: String? = null,

    /**
     * 商品图片
     */
    var backImage: MutableList<BackImageDto>? = null
) {
    class BackImageDto(
        var filename: String? = null,
        var imgwidth: String? = null,
        var imgheight: String? = null
    )
}