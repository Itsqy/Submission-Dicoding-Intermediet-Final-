package com.rifqi.testpaging3.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rifqi.testpaging3.data.local.QuoteDatabase
import com.rifqi.testpaging3.data.local.RemoteKeys
import com.rifqi.testpaging3.data.remote.ListStoryData
import com.rifqi.testpaging3.network.ApiService


@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
    private val database: QuoteDatabase,
    private val apiService: ApiService,
    private val tokenUser: String
) : RemoteMediator<Int, ListStoryData>() {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryData>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData =
                apiService.getStoryPaging("Bearer $tokenUser", page, state.config.pageSize).listStory
            val endOfPaginationReached = responseData?.isEmpty()
            Log.d("responseApi", responseData.toString())
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteDao().deleteRemoteKeys()
                    database.quoteDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached!!) null else page + 1
                val keys = responseData.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteDao().insertAll(keys)
                database.quoteDao().insertQuote(responseData)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached!!)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryData>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryData>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryData>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteDao().getRemoteKeysId(id)
            }
        }
    }
}