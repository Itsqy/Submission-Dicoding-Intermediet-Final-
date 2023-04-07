package com.rifqi.testpaging3

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rifqi.testpaging3.data.remote.ListStoryData

class PageDataSource :
    PagingSource<Int, LiveData<List<ListStoryData>>>() {
    companion object {
        fun snapshot(items: List<ListStoryData>): PagingData<ListStoryData> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryData>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryData>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}