package com.azharkova.photoram.adapter

import android.view.View
import com.azharkova.photoram.data.CommentItem
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.databinding.ItemCommentLayoutBinding
import com.azharkova.photoram.databinding.ItemPostLayoutBinding
import com.azharkova.photoram.util.loadImage

class CommentItemHolder (binding: ItemCommentLayoutBinding) : BaseViewHolder<CommentItem>(binding) {
    override fun bindItem(item: CommentItem) {
        (itemViewBinding as? ItemCommentLayoutBinding)?.apply {
            tvUserName.text = "${item.userName}"//, ${item.date.toString()}"
            tvText.text = item.text
        }
    }
}