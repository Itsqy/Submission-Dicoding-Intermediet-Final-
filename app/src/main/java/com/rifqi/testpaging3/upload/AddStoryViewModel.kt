package com.rifqi.testpaging3.upload

import androidx.lifecycle.ViewModel
import com.rifqi.testpaging3.data.StoryRepository
import okhttp3.MultipartBody

class AddStoryViewModel : ViewModel() {

    private val storyRepository = StoryRepository()



    val msgDialog = storyRepository.messageDialog
    val loading = storyRepository.isLoading
    fun insertStory(token: String, desc: String, img: MultipartBody.Part) =
        storyRepository.addStory(token, desc, img)


}