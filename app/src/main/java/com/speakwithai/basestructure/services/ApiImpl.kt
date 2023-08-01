package com.speakwithai.basestructure.services

import com.speakwithai.basestructure.services.Api
import javax.inject.Inject

class ApiImpl @Inject constructor(private val api: Api) {

    suspend fun getBtc() = api.getBtc()

}