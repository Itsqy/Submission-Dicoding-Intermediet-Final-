package com.rifqi.testpaging3.data.remote

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult : UserData?,
)

data class UserData(
    @field:SerializedName("userId") val userId: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("token") val token: String,

)