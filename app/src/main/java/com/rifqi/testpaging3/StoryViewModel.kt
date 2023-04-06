package com.rifqi.testpaging3

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rifqi.testpaging3.data.QuoteRepository
import com.rifqi.testpaging3.data.remote.ListStoryData

class StoryViewModel(quoteRepository: QuoteRepository) :ViewModel(){

    val stories: LiveData<PagingData<ListStoryData>> =
        quoteRepository.getQuote().cachedIn(viewModelScope)
}