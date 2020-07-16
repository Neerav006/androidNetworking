package com.example.myapplication.retrofit

import com.example.myapplication.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("everything?q=sports&apiKey=aa67d8d98c8e4ad1b4f16dbd5f3be348")
    fun getNews(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ):Call<NewsResponse.Response>
}