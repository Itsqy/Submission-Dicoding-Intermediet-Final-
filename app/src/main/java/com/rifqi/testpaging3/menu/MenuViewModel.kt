package com.rifqi.testpaging3.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rifqi.testpaging3.data.QuoteRepository
import com.rifqi.testpaging3.data.StoryRepository
import com.rifqi.testpaging3.data.remote.ListStoryData

class MenuViewModel : ViewModel() {

    private val storyRepository = StoryRepository()

    val loading = storyRepository.isLoading
    val getStoryData = storyRepository.getStory()
    fun setStory(token: String) = storyRepository.setStory(token)


//    val stories: LiveData<PagingData<ListStoryData>> =
//        quoteRepository.getQuote().cachedIn(viewModelScope)


}