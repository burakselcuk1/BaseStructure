package com.speakwithai.basestructure.ui.crypto.utilities

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.speakwithai.basestructure.R
import java.lang.Exception
import java.net.UnknownHostException


fun <T> getErrorMessage(response: Response.Error<T>, context: Context): String {
    return when (response.errorType) {
        ResponseError.TOO_MANY_REQUESTS -> context.getString(R.string.too_many_requests)
        ResponseError.NO_INTERNET_CONNECTION -> context.getString(R.string.no_internet_connection)
        else -> context.getString(R.string.an_error_occurred)
    }
}


fun <T> displayErrorSnackBar(response: Response.Error<T>, view: View, context: Context, onClick: () -> Unit) {
    Snackbar.make(view, getErrorMessage(response,context), Snackbar.LENGTH_INDEFINITE)
        .setAction("refresh") { onClick() }.show()
}


suspend fun <T> handleNetworkCall(response: MutableLiveData<Response<T>>, networkCall: (suspend () -> Unit)) {
    try {
        networkCall()
    } catch (e: UnknownHostException) {
        e.printStackTrace();
        response.postValue(Response.Error(ResponseError.NO_INTERNET_CONNECTION))
    } catch (e: Exception) {
        e.printStackTrace();
        response.postValue(Response.Error(ResponseError.GENERAL_ERROR))
    }
}


fun <T, S> handleIfNotSuccessful(retrofitResponse: retrofit2.Response<T>, response: MutableLiveData<Response<S>>): Boolean {
    if (!retrofitResponse.isSuccessful) {
        if (retrofitResponse.code() == 429) {
            response.postValue(Response.Error(ResponseError.TOO_MANY_REQUESTS))
        } else {
            response.postValue(Response.Error(ResponseError.GENERAL_ERROR))
        }
    }

    return retrofitResponse.isSuccessful;
}

