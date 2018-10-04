package com.pansaraugmented.app.ui.views.base.customrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewAdapter<T, VH : CustomViewHolder<T>>(
        private val viewHolderFactory: CustomViewHolder.Factory<T, VH>,
        private val viewLayout: Int,
        initialList: List<T> = emptyList()
) : RecyclerView.Adapter<CustomViewHolder<T>>() {

    private var dataHolder = mutableListOf<T>()

    init {
        dataHolder.addAll(initialList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(viewLayout, parent, false)
        return viewHolderFactory.createViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder<T>, position: Int) {
        val currentItem = dataHolder[position]
        holder.update(currentItem)

    }

    override fun getItemCount(): Int {
        return dataHolder.size
    }

    fun replaceData(newList: List<T>) {

        val diffUtilCallback = DiffUtilCallback(newList, dataHolder)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        // Update list
        dataHolder.clear()
        dataHolder.addAll(newList)

        // Dispatch updates to views
        diffResult.dispatchUpdatesTo(this)
    }

    fun addItem(item: T) {
        dataHolder.add(item)
        notifyDataSetChanged()
    }
}
