package com.rifqi.testpaging3.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rifqi.testpaging3.data.remote.LoginResponse
import com.rifqi.testpaging3.network.ApiConfig
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisViewModel() : ViewModel() {


    private val _showMessage = MutableLiveData<String>()
    val showMessage: LiveData<String> = _showMessage

    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading



    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        ApiConfig.getService().registerUser(name, email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _showMessage.value = response.body()?.message
                        _isError.value = response.body()?.error.toString()

                    } else {
                        _isLoading.value = false
                        val jsonObject =
                            JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
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

}