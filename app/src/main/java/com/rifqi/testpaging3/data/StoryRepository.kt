package com.rifqi.testpaging3.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rifqi.testpaging3.data.remote.ListStoryData
import com.rifqi.testpaging3.data.remote.StoriesResponse
import com.rifqi.testpaging3.network.ApiConfig
import okhttp3.MultipartBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository {


    private val story = MutableLiveData<ArrayList<ListStoryData>>()
    private val _messageDialog = MutableLiveData<String>()
    val messageDialog: LiveData<String> = _messageDialog

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun setStory(token: String) {
        _isLoading.value = true
        ApiConfig.getService().getStories("Bearer $token")
            .enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        story.postValue(response.body()?.listStory)
                        Log.d("valueStory",response.body().toString())
                    } else {
                        _isLoading.value = false
                        val jsonObject =
                            JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
                        _messageDialog.value = jsonObject.getString("message")
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    _isLoading.value = false
                    _messageDialog.value = t.toString()
                }
            })
    }

    fun getStory(): LiveData<ArrayList<ListStoryData>> = story

    fun addStory(userToken: String, desc: String, image: MultipartBody.Part) {
        _isLoading.value = true
        ApiConfig.getService().addStories("Bearer $userToken", desc, image)
            .enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _messageDialog.value = response.body()?.error.toString()

                    } else {
                        _isLoading.value = false
                        val jsonObject =
                            JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                        _messageDialog.value = jsonObject.getString("message")

                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    _isLoading.value = false
                    _messageDialog.value = t.message.toString()
                }
            })
    }
}