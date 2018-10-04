package com.pansaraugmented.app.ui.views.base.customrecyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class CustomViewHolder<in T> protected constructor(val view: View) : RecyclerView.ViewHolder(view) {
    abstract fun update(data: T)

    abstract class Factory<in T, out VH : CustomViewHolder<T>> {
        abstract fun createViewHolder(view: View): VH
    }
}

