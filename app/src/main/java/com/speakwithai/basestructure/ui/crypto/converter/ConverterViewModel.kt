package com.speakwithai.basestructure.ui.crypto.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speakwithai.basestructure.data.model.response.coin.ExchangeRate
import com.speakwithai.basestructure.ui.crypto.repository.CoinsRepository
import com.speakwithai.basestructure.ui.crypto.utilities.Response
import com.speakwithai.basestructure.ui.crypto.utilities.handleIfNotSuccessful
import com.speakwithai.basestructure.ui.crypto.utilities.handleNetworkCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
): ViewModel() {

    private var _exchangeRates: Map<String, ExchangeRate> = mapOf()
    private val _response = MutableLiveData<Response<Map<String, ExchangeRate>>>()
    val response: LiveData<Response<Map<String, ExchangeRate>>> = _response

    var selectedFromExchangeRate: String = "eur"
    var selectedToExchangeRate: String = "btc"
    private var fromPrice = ""

    private val _displayToPrice = MutableLiveData<String>()
    val displayToPrice: LiveData<String> = _displayToPrice


    fun fetchExchangeRates() = viewModelScope.launch {
        _response.postValue(Response.Waiting())

        handleNetworkCall(_response) {
            val rates = coinsRepository.getExchangeRates()

            if (!handleIfNotSuccessful(rates, _response)) {
                return@handleNetworkCall
            }

            _response.postValue(Response.Success(rates.body()!!.rates))
            _exchangeRates = rates.body()!!.rates
        }

    }

    fun changeFromExchangeRate(id: String) {
        this.selectedFromExchangeRate = id.lowercase()
        this.recalculatePrice()
    }

    fun changeToExchangeRate(id: String) {
        this.selectedToExchangeRate = id.lowercase()
        this.recalculatePrice()
    }

    fun onUserFromInputTextChanged(newText: CharSequence, start: Int, before: Int, count: Int) {
        this.fromPrice = newText.toString()
        this.recalculatePrice()
    }


    private fun recalculatePrice() {
        if (fromPrice.isEmpty() || fromPrice == ".") {
            _displayToPrice.postValue("")
            return
        }

        if (
            !_exchangeRates.containsKey(selectedFromExchangeRate) ||
            !_exchangeRates.containsKey(selectedToExchangeRate)
        ) {
            return
        }

        val exchangeRateFrom = _exchangeRates[selectedFromExchangeRate]!!
        val exchangeRateTo = _exchangeRates[selectedToExchangeRate]!!

        val newPrice = this.fromPrice.toFloat() * exchangeRateTo.value / exchangeRateFrom.value

        val decimalFormat = DecimalFormat("#.########")
        _displayToPrice.postValue(decimalFormat.format(newPrice))
    }
}