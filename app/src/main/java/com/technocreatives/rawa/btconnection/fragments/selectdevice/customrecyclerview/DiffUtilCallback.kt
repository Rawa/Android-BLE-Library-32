package com.pansaraugmented.app.ui.views.base.customrecyclerview

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by David Goransson on 2018-04-11.
 * Copyright 2018 The Techno Creatives. All rights reserved
 */

class DiffUtilCallback<T>(
        private val newProvider: List<T>,
        private val oldProvider: List<T>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProvider[oldItemPosition] == newProvider[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldProvider.size
    }

    override fun getNewListSize(): Int {
        return newProvider.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProvider[oldItemPosition] == newProvider[newItemPosition]
    }

}