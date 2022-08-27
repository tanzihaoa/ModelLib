package com.tzh.myapplication.ui.dto

class ListDTO(
    var title : String ?= null,
    var url : String ?= null
) {
    fun getList() : MutableList<ListDTO>{
        val list = mutableListOf<ListDTO>()
        list.add(ListDTO("1"))
        list.add(ListDTO("2"))
        list.add(ListDTO("3"))
        list.add(ListDTO("4"))
        list.add(ListDTO("5"))
        list.add(ListDTO("6"))
        list.add(ListDTO("7"))
        list.add(ListDTO("8"))
        list.add(ListDTO("9"))
        list.add(ListDTO("10"))
        return list
    }
}