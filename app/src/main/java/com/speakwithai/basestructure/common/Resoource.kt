package com.speakwithai.basestructure.common

sealed class Resoource<out T : Any> {
    object Loading : Resoource<Nothing>()
    data class Success<out T : Any>(val data: T) : Resoource<T>()
    data class Error(val throwable: Throwable) : Resoource<Nothing>()
}