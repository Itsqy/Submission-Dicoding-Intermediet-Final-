package com.rifqi.testpaging3

import androidx.recyclerview.widget.DiffUtil
import com.rifqi.testpaging3.data.remote.ListStoryData

class DiffCallback(
    private val mOldStoryList: ArrayList<ListStoryData>,
    private val mNewStoryList: List<ListStoryData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldStoryList.size

    override fun getNewListSize(): Int = mNewStoryList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        mOldStoryList[oldItemPosition].id == mNewStoryList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldStoryList[oldItemPosition]
        val newEmployee = mNewStoryList[newItemPosition]

        return oldEmployee.name == newEmployee.name
    }
}