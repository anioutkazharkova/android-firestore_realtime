package com.azharkova.photoram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.databinding.ItemPostLayoutBinding

class PostsAdapter(var onLikeClick: ((Int)->Unit)? = null) : BaseAdapter<PostItemHolder, PostItem>() {
    override fun takeViewHolder(parent: ViewGroup): BaseViewHolder<PostItem> {
        val binding = ItemPostLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return PostItemHolder(binding)
    }

    override fun onBindViewHolder(holder: PostItemHolder, position: Int) {
       super.onBindViewHolder(holder,position)
        holder.onLikeClick = onLikeClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemHolder {
        return takeViewHolder(parent) as PostItemHolder
    }
}