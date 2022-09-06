package com.tzh.mylibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class XRvBindingHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createHolder(parent: ViewGroup, @LayoutRes layoutId: Int): XRvBindingHolder {
            return XRvBindingHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false))
        }
    }

    fun <B : ViewDataBinding> getBinding(): B = binding as B

}