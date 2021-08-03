package com.azharkova.photoram.adapter

import android.view.View
import com.azharkova.photoram.R
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.databinding.ItemPostLayoutBinding
import com.azharkova.photoram.util.loadImage
import java.text.SimpleDateFormat
import java.util.*

class PostItemHolder(binding: ItemPostLayoutBinding) : BaseViewHolder<PostItem>(binding) {
    var onLikeClick: ((Int)->Unit)? = null
    override fun bindItem(item: PostItem) {
        (itemViewBinding as? ItemPostLayoutBinding)?.apply {
            tvUserName.text = item.userName
            tvDate.text = item.date
            tvText.text = item.postText
            if (item.editedTime != null) {
                tvEdited.text = if (item.editor == item.userId) "by author" else "not author"
            } else {
                tvEdited.text = ""
            }
            if (item.hasLike) {
                btnLike.setImageResource(R.drawable.liked)
            }else {
                btnLike.setImageResource(R.drawable.unliked)
            }
            btnLike.setOnClickListener {
                onLikeClick?.invoke(tag)
            }
            if (item.imageLink.isNotEmpty()) {
                image.visibility = View.VISIBLE
                image.loadImage(item.imageLink)
            } else {
                image.visibility = View.GONE
            }
        }
    }
}