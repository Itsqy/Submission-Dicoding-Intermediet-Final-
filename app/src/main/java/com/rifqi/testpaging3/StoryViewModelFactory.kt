package com.rifqi.testpaging3

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rifqi.testpaging3.data.injection.DependencyInjection

class StoryViewModelFactory(val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(DependencyInjection.parameterStory(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}