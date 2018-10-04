package com.pansaraugmented.app.ui.views.base.customrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class SortedRecyclerViewAdapter<T, VH : CustomViewHolder<T>>(
        private val viewHolderFactory: CustomViewHolder.Factory<T, VH>,
        private val viewLayout: Int,
        private val comparator: Comparator<T>
) : RecyclerView.Adapter<CustomViewHolder<T>>() {

    private val dataHolder: MutableList<T> = mutableListOf()

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
        dataHolder.clear()
        dataHolder.addAll(newList)
        dataHolder.sortWith(comparator)

        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        dataHolder.add(item)
        dataHolder.sortWith(comparator)
        notifyDataSetChanged()
    }

    fun replaceAddItem(item: T, comparator: Comparator<T>) {
        dataHolder.removeAll { comparator.compare(item, it) == 0 }
        dataHolder.add(item)
        dataHolder.sortWith(comparator)
        notifyDataSetChanged()
    }

    fun clear() {
        dataHolder.clear()
        notifyDataSetChanged()
    }
}
