package com.speakwithai.basestructure.services

import com.speakwithai.basestructure.model.BtcResponse
import com.speakwithai.basestructure.model.CompletionRequest
import com.speakwithai.basestructure.model.CompletionResponse
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @GET("api/rates")
    suspend fun getBtc(
    ): Response<BtcResponse>

    @POST("v1/chat/completions")
    suspend fun getCompletions(@Body completionResponse: CompletionRequest) : Response<CompletionResponse>
}