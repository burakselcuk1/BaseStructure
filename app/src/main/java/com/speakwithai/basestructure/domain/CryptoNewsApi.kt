package com.speakwithai.basestructure.domain

import com.speakwithai.basestructure.data.model.response.cryptoNews.NewsApiModel
import retrofit2.Response
import retrofit2.http.GET

interface CryptoNewsApi {

    @GET("news/")
    suspend fun getLatestNews(): NewsApiModel

}