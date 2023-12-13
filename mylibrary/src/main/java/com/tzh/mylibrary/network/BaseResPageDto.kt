package com.tzh.mylibrary.network

open class BaseResPageDto<T> {
    /**
     * 总条数
     */
    var count: Int = 0

    /**
     * 总页码
     */

    var maxPage: Int = 0

    /**
     * 数据
     */
    var list: MutableList<T>? = null


    fun getListDto(): MutableList<T> {
        return list ?: mutableListOf()
    }
}