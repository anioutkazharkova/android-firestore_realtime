package com.azharkova.photoram.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Базовый класс ViewHolder
 * @property itemViewBinding байндинг
 * @property tag номер (для определения индекса элемента)
 * */
abstract class BaseViewHolder<T>(val itemViewBinding: ViewBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {

    /**
     * Наполнение viewholder
     * */
    abstract fun bindItem(item: T)

    open var tag: Int = 0
}