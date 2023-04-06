package com.rifqi.testpaging3.network

import com.rifqi.testpaging3.data.remote.LoginResponse
import com.rifqi.testpaging3.data.remote.StoriesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): Call<StoriesResponse>

    @GET("stories")
    suspend fun getStoryPaging(
        @Header("Authorization") token: String,
        @Query("page") page: Int ,
        @Query("size") size: Int = 10
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun addStories(
        @Header("Authorization") token: String,
        @Part("description") description: String,
        @Part file: MultipartBody.Part
    ): Call<StoriesResponse>

}