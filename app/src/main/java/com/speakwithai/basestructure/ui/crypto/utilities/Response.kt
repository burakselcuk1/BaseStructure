package com.speakwithai.basestructure.ui.crypto.utilities

sealed class Response<T>(
    val data: T? = null,
    val errorType: ResponseError? = null
) {
    class Success<T>(data: T): Response<T>(data)
    class Error<T>(type: ResponseError): Response<T>(null, type)
    class Waiting<T> : Response<T>()
}
