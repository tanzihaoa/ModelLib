package com.tzh.mylibrary.util.img

import androidx.lifecycle.LifecycleOwner

object ImageUpLoadUtil {
    private val mList : MutableList<String> = mutableListOf()
    private val mImage : MutableList<ImageDTO> = mutableListOf()
    private val mDtoList : MutableList<ImageDTO> = mutableListOf()

    var mListener : UpLoadListener?= null

    var mDtoListener : UpLoadDtoListener?= null

    fun upLoad(owner: LifecycleOwner,image : List<ImageDTO>,listener : UpLoadListener){
        if(image.isNotEmpty()){
            mList.clear()
            mImage.clear()
            mImage.addAll(image)
            mListener = listener
            upLoadFile(owner)
        }else{
            mListener?.sure(mList)
        }
    }

    private fun upLoadFile(owner: LifecycleOwner){
        if(mList.size < mImage.size){
            val dto = mImage[mList.size]

        }else{
            mListener?.sure(mList)
        }
    }

    fun upLoadDto(owner: LifecycleOwner,image : List<ImageDTO>,listener : UpLoadDtoListener){
        if(image.isNotEmpty()){
            mDtoList.clear()
            mImage.clear()
            mImage.addAll(image)
            mDtoListener = listener
            upLoadFileDto(owner)
        }else{
            mDtoListener?.sure(mDtoList)
        }
    }

    private fun upLoadFileDto(owner: LifecycleOwner){
        if(mDtoList.size < mImage.size){
            val dto = mImage[mDtoList.size]
            if(dto.file != null){

            }else{
                mDtoList.add(dto)
                upLoadFileDto(owner)
            }
        }else{
            mDtoListener?.sure(mDtoList)
        }
    }

    interface UpLoadListener{
        fun sure(list : MutableList<String>)
    }


    interface UpLoadDtoListener{
        fun sure(list : MutableList<ImageDTO>)
    }
}