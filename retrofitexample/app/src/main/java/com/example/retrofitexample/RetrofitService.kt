package com.example.retrofitexample

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitService {

    const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    fun getPostApi(): PostApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PostApi::class.java)
    }
}

interface PostApi {
    @GET("posts")
    fun getPostList(): Call<List<Post>>
}
