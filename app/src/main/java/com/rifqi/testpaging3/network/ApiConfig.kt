package com.rifqi.testpaging3.network


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    val authInterceptor = Interceptor { chain ->
        val req = chain.request()
        val requestHeaders = req.newBuilder()
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXZFcXlraEk5VzdtaXA5UU8iLCJpYXQiOjE2NzkyODEzOTV9.fFtpCDOhdfwrkna5dm39CKe5z58SCMQVZWRBQgi6C9w")
            .build()
        chain.proceed(requestHeaders)
    }
    fun getService(): ApiService {

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
//            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)


    }
}