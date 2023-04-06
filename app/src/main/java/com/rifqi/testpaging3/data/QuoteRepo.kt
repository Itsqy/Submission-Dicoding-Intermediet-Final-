package com.rifqi.testpaging3.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.rifqi.testpaging3.data.local.QuoteDatabase
import com.rifqi.testpaging3.data.remote.ListStoryData
import com.rifqi.testpaging3.network.ApiService

class QuoteRepository(
    private val quoteDatabase: QuoteDatabase,
    private val apiService: ApiService,
    val token: String
) {

    fun getQuote(): LiveData<PagingData<ListStoryData>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = QuoteRemoteMediator(quoteDatabase, apiService, token),
            pagingSourceFactory = {
                quoteDatabase.quoteDao().getAllQuote()

            }
        ).liveData
    }
}