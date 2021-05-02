package com.raghvendra.foodyapp.util

import androidx.recyclerview.widget.DiffUtil
import com.raghvendra.foodyapp.models.Result

class RecipesDiffutil(
    private val oldList: List<Result>,
    private val newList: List<Result>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
       return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList.get(oldItemPosition) === newList.get(newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition)
    }
}