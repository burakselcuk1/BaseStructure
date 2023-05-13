package com.example.basestructure.services

import com.example.basestructure.model.BtcResponse
import com.example.chatgptapp.model.CompletionRequest
import com.example.chatgptapp.model.CompletionResponse
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @GET("api/rates")
    suspend fun getBtc(
    ): Response<BtcResponse>

    @POST("v1/chat/completions")
    suspend fun getCompletions(@Body completionResponse: CompletionRequest) : Response<CompletionResponse>
}