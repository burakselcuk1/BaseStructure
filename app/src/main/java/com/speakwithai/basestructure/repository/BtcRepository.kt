package com.speakwithai.basestructure.repository

import com.speakwithai.basestructure.common.handleRequestFlow
import com.speakwithai.basestructure.services.ApiImpl
import javax.inject.Inject


class BtcRepository @Inject constructor(private val btcApiImple: ApiImpl) {

    suspend fun getUsers() =  handleRequestFlow { btcApiImple.getBtc() }

}