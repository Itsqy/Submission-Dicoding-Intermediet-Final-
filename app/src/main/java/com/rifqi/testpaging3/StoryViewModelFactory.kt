package com.rifqi.testpaging3

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rifqi.testpaging3.data.injection.DependencyInjection
import com.rifqi.testpaging3.menu.MenuViewModel

class StoryViewModelFactory(val context: Context, val token: String) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(DependencyInjection.parameterStory(context, token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}