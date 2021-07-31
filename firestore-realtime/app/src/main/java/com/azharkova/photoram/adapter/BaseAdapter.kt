package com.azharkova.photoram.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<K : BaseViewHolder<T>, T>(var onItemClick: ((Int) -> Unit)? = null) : RecyclerView.Adapter<K>() {
    var items: ArrayList<T> = arrayListOf()

    override fun getItemCount(): Int = items.count()

    open fun setupItems(items: List<T>) {
        this.items = arrayListOf()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    abstract fun takeViewHolder(parent: ViewGroup): BaseViewHolder<T>

    open fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = this.items[position]
        holder.bindItem(item)
        holder.tag = position
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }
}
