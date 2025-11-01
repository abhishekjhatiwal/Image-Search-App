package com.example.imagesearchapp.data

import com.example.imagesearchapp.data.remote.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService{
    @GET("api/")
    suspend fun getImages(
        @Query("key") apiKey : String = api_key,
        @Query("q") q : String,
        @Query("page") page : Int
    ) : ImageResponse
}