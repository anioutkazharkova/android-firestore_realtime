package com.azharkova.photoram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.azharkova.photoram.data.CommentItem
import com.azharkova.photoram.databinding.ItemCommentLayoutBinding

class CommentsAdapter : BaseAdapter<CommentItemHolder, CommentItem>() {
    override fun takeViewHolder(parent: ViewGroup): BaseViewHolder<CommentItem> {
        val binding = ItemCommentLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return CommentItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentItemHolder, position: Int) {
        super.onBindViewHolder(holder,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItemHolder {
        return takeViewHolder(parent) as CommentItemHolder
    }
}