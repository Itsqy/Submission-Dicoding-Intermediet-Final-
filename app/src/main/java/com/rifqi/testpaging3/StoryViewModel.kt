package com.rifqi.testpaging3

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rifqi.testpaging3.data.QuoteRepository
import com.rifqi.testpaging3.data.remote.ListStoryData

class StoryViewModel(private val quoteRepository: QuoteRepository) : ViewModel() {

    fun stories(token: String): LiveData<PagingData<ListStoryData>> {
        return quoteRepository.getQuote(token).cachedIn(viewModelScope)
    }




}