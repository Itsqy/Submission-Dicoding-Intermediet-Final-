package com.rifqi.testpaging3

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rifqi.testpaging3.data.local.UserModel
import kotlinx.coroutines.launch

class AuthViewModel(private val userPrefferences: UserPrefferences) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return userPrefferences.getUser().asLiveData()
    }

    fun logOut() = viewModelScope.launch {userPrefferences.logout()}


}