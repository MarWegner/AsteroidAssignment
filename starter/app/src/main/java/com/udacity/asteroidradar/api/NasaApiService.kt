package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface NasaApiService {

    @GET("planetary/apod")
    suspend fun getPicOfTheDay(
        @Query("api_key") api_key: String
    ):PictureOfDay

    @GET("neo/rest/v1/feed")
    suspend fun getAstroids(
        @Query("api_key") api_key: String
    ): String
}


object NasaApi{
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: NasaApiService by lazy { retrofit.create(NasaApiService::class.java) }
}