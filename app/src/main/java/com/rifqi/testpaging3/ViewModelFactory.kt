package com.rifqi.testpaging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rifqi.testpaging3.login.LoginViewModel

class ViewModelFactory(private val pref: UserPrefferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}