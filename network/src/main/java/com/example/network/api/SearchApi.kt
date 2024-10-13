package com.example.network.api

import com.example.network.BuildConfig
import com.example.network.model.SearchImageResponse
import com.example.network.model.SearchVideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET(BuildConfig.KAKAO_IMG_URL)
    suspend fun searchImage(
        @Query("query") query: String,
        @Query("page") page: Int?,
        @Query("sort") sort: String,
        @Query("size") size: Int,
    ): SearchImageResponse

    @GET(BuildConfig.KAKAO_VIDEO_URL)
    suspend fun searchVideo(
        @Query("query") query: String,
        @Query("page") page: Int?,
        @Query("sort") sort: String,
        @Query("size") size: Int,
    ): SearchVideoResponse
}