package com.rifqi.testpaging3.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.testpaging3.UserPrefferences
import com.rifqi.testpaging3.data.local.UserModel
import com.rifqi.testpaging3.data.remote.LoginResponse
import com.rifqi.testpaging3.network.ApiConfig
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(val sharedPref: UserPrefferences) : ViewModel() {

    private val _showMessage = MutableLiveData<String>()
    val showMessage: LiveData<String> = _showMessage

    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun login(username: String, password: String) {
        _isLoading.value = true
        ApiConfig.getService().loginUser(username, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response != null) {
                            _isLoading.value = false
                            _showMessage.value = response.body()?.message
                            _isError.value = response.body()?.error.toString()
                            val body = response.body()?.loginResult
                            val dataUser = UserModel(
                                body?.name.toString(),
                                body?.userId.toString(),
                                body?.token.toString(),
                                true
                            )
                           val save =  saveUser(dataUser)
                            Log.d("saveUser", "onResponse: $dataUser")
                        } else {
                            _isLoading.value = false
                            _showMessage.value = response.body()?.message
                            _isError.value = response.body()?.error.toString()
                        }


                    } else {
                        _isLoading.value = false
                        val jsonObject =
                            JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                        _showMessage.value = jsonObject.getString("message")
                        _isError.value = jsonObject.getString("error")

                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    _showMessage.value = t.message.toString()
                }
            })
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            sharedPref.saveUser(user)
        }
    }


}